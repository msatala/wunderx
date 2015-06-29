package boilerplate

import jsactor.bridge.client.SocketManager
import jsactor.bridge.client.util.RemoteActorListener
import jsactor.logging.impl.JsPrintlnActorLoggerFactory
import jsactor.{JsActorRef, JsActorSystem}
import org.scalajs.dom

import scala.scalajs.js.Dynamic

/**
 * Created by Milan Satala
 * Date: 7.6.2015
 * Time: 19:57
 */
class ProxyActor(remoteActorPath: String, val wsManager: JsActorRef) extends RemoteActorListener {
  override def actorPath: String = remoteActorPath

  override def onConnect(serverActor: JsActorRef): Unit = {}

  override def whenConnected(serverActor: JsActorRef): Receive = {
    case msg =>
      if (sender() == context.parent) {
        serverActor ! msg
      } else {
        context.parent ! msg
      }
  }
}


object WebsocketJsActors {
  val actorSystem = JsActorSystem("WunderxClient", JsPrintlnActorLoggerFactory)

  val wsManager = {
    val webSocketUrl = dom.window.asInstanceOf[Dynamic].webSocketUrl.asInstanceOf[String]

    implicit val protocol = WunderxProtocol
    actorSystem.actorOf(SocketManager.props(SocketManager.Config(webSocketUrl)), "socketManager")
  }
}
