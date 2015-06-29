package wunderx

import biz.enef.angulate._
import boilerplate.NgOps._
import boilerplate.{WebsocketJsActors, ProxyActor, Subscribe}
import jsactor.{JsActor, JsProps}
import rx._
import wunderx.Task._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

/**
 * Created by Milan Satala
 * Date: 4.6.2015
 * Time: 16:47
 */

class TasksClientActor(tasksMapVar: Var[TasksMap]) extends JsActor {
  val proxy = context.actorOf(JsProps(new ProxyActor("/user/TaskServerActor", WebsocketJsActors.wsManager)))

  override def preStart(): Unit = {
    proxy ! Subscribe
  }

  override def receive: Receive = {
    case task: Task =>
      tasksMapVar() = tasksMapVar() withTask task

      if (sender() != proxy) proxy ! task
  }
}

class TasksCtrl($scope: Scope) extends Controller {
  private implicit val s = $scope

  private val tasksMapVar: Var[TasksMap] = Var(Map())

  val tasksActorRef = WebsocketJsActors.actorSystem.actorOf(JsProps(new TasksClientActor(tasksMapVar)))

  val partitionedTasksRx = Rx(tasksMapVar().values.partition(_.completed))

  val completedRx = Rx(partitionedTasksRx()._1.to[js.Array]).toNg
  val todoRx = Rx(partitionedTasksRx()._2.to[js.Array]).toNg

  def taskToggle(task: Task) = tasksActorRef ! task.toggle

  def addTask(newTaskText: String) = tasksActorRef ! Task.create(newTaskText)

}

object WunderxClient extends js.JSApp {
  @JSExport
  override def main(): Unit = {
    val module = angular.createModule("wunderx", Seq("ionic"))
    module.controllerOf[TasksCtrl]("TasksCtrl")
  }
}