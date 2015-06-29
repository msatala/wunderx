package boilerplate

import jsactor.bridge.protocol.BridgeProtocol
import jsactor.bridge.protocol.BridgeProtocol.MessageRegistry
import upickle._
import wunderx.Task

import scala.reflect.ClassTag

/**
 * Created by Milan Satala
 * Date: 7.6.2015
 * Time: 20:00
 */

case object Subscribe

object WunderxProtocol extends BridgeProtocol {

  override def registerMessages(registry: MessageRegistry): Unit = {
    def add[A: Reader : Writer : ClassTag] = {
      registry.add[A]
    }

    def addObj[A <: Singleton : Reader : Writer : ClassTag](obj: A) = {
      registry.addObj(obj)
    }
    add[Task]
    addObj(Subscribe)
  }


}
