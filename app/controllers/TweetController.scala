package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class TweetController @Inject()(cc: ControllerComponents) (implicit assetsFinder: AssetsFinder)
  extends AbstractController(cc) {

  def list = Action {
    NotImplemented
  }

  def create = Action {
    NotImplemented
  }

  def details(id: Long) = Action {
    NotImplemented
  }

  def update(id: Long) = Action {
    NotImplemented
  }

  def delete(id: Long) = Action {
    NotImplemented
  }
}