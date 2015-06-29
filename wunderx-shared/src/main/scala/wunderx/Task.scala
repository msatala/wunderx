package wunderx

import scala.scalajs.js.annotation.JSExportAll

/**
 * Created by Milan Satala
 * Date: 4.6.2015
 * Time: 17:06
 */
@JSExportAll
case class Task(id: Int, text: String, completed: Boolean) {
  def toggle = this.copy(completed = !this.completed)
}


object Task {
  type TasksMap = Map[Int, Task]

  implicit class TasksMapOps(tasksMap: TasksMap) {
    def withTask(task: Task) = tasksMap + (task.id -> task)
  }

  def create(text: String): Task =
    Task(
      id = (Math.random() * Int.MaxValue).toInt,
      text = text,
      completed = false
    )
}