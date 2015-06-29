package controllers

import akka.actor.{Actor, ActorRef, Terminated}
import boilerplate.Subscribe
import wunderx.Task

/**
 * Created by Milan Satala
 * Date: 4.6.2015
 * Time: 22:04
 */
class TaskServerActor extends Actor {
  var tasksMap = Map(
    1 -> Task(1, "Foo", completed = true),
    2 -> Task(2, "Bar", completed = false)
  )

  var subscribers = Set.empty[ActorRef]

  override def receive: Receive = {
    case Subscribe =>
      context watch sender()
      subscribers += sender()
      tasksMap.valuesIterator.foreach(sender() ! _)

    case Terminated(actor) => subscribers -= actor

    case task: Task =>
      tasksMap = tasksMap withTask task
      subscribers.filterNot(_ == sender()).foreach(_ ! task)
  }
}