package controllers

import javax.inject._
import play.api.Configuration
import play.api.mvc._
import play.api.libs.oauth._
import play.api.libs.ws._

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(configuration: Configuration, cc: ControllerComponents, wsc: WSClient) (implicit assetsFinder: AssetsFinder)
  extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def tweets = Action.async {
    credentials.map { case (consumerKey, requestToken) =>
      wsc
        .url("https://api.twitter.com/1.1/search/tweets.json")
//        .url("https://api.twitter.com/1.1/statuses/home_timeline.json")
//          .url("https://stream.twitter.com/1.1/statuses/filter.json")
        .sign(OAuthCalculator(consumerKey, requestToken))
        .withQueryStringParameters("q" -> "Trump")
        .get()
        .map { response =>
          Ok(response.body)
        }
      } getOrElse {
        Future.successful(InternalServerError("Twitter credentials missing"))
      }
  }

  def credentials: Option[(ConsumerKey, RequestToken)] = for {
    consumerKey <- configuration.getOptional[String]("twitter.consumerKey")
    consumerSecret <- configuration.getOptional[String]("twitter.consumerSecret")
    accessToken <- configuration.getOptional[String]("twitter.accessToken")
    accessTokenSecret <- configuration.getOptional[String]("twitter.accessTokenSecret")
  } yield (
    ConsumerKey(consumerKey, consumerSecret),
    RequestToken(accessToken, accessTokenSecret)
  )

}
