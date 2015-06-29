package controllers

import akka.actor.Props
import boilerplate.WunderxProtocol
import jsactor.bridge.server.ServerBridgeActor
import play.api.libs.concurrent.Akka
import play.api.mvc._

object WundeRxCtrl extends Controller {

  import play.api.Play.current

  val taskServerActor = Akka.system.actorOf(Props(new TaskServerActor), "TaskServerActor")

  def index = Action { implicit request ⇒
    Ok(views.html.index())
  }

  def ws = WebSocket.acceptWithActor[String, String] { req ⇒ websocket ⇒
    implicit val protocol = WunderxProtocol
    ServerBridgeActor.props(websocket)
  }

}
