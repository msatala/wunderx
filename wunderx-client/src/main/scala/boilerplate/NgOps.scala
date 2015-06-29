package boilerplate

import biz.enef.angulate.Scope
import rx._
import rx.ops._

import scala.scalajs.js

/**
 * Created by Milan Satala
 * Date: 15.6.2015
 * Time: 13:06
 */
object NgOps {

  implicit class ScopeOps(s: Scope) {
    def dynamic = s.asInstanceOf[js.Dynamic]

    def safeApply(any: => Any) = {
      if (dynamic.$$phase == null) {
        s.$apply(() => any)
      } else any
    }

  }

  implicit class LunaVarOps[T](val source: Var[T]) extends AnyVal {
    def asRx: Rx[T] = source.asInstanceOf[Rx[T]]
  }

  implicit class LunaRxOps[+T](val rx: Rx[T]) extends AnyVal {
    /**
     * Converts Rx() into a plain javascript function, that can be called by angular.
     * Update of underlying Rx() triggers $scope.$apply() to ensure that template is re-rendered..
     * @return No-parameter javascript function
     */
    def toNg(implicit $scope: Scope): js.Function0[T] = {
      var value: T = rx()
      val obs = rx.foreach(newValue => $scope.safeApply(value = newValue), skipInitial = true)
      $scope.$on("$destroy", () => obs.kill())

      () => value
    }
  }

}