import play.api.GlobalSettings

import com.google.inject.{ AbstractModule, Guice }
import scala.collection.mutable.Buffer
import scala.collection.mutable.ArrayBuffer
import play.api.mvc.Results._
import scala.concurrent.Future
import play.api.GlobalSettings
import play.api.mvc.RequestHeader
import play.api.Logger
import dao.slick.SlickDaoModule

object Global extends GlobalSettings {

  /**
   * Bind traits to concrete types. The concrete types will be used at runtime.
   */
  val injector = Guice.createInjector(SlickDaoModule)

  /**
   * Ask Guice to supply the controllers based on bindings and annotations.
   */
  override def getControllerInstance[A](controllerClass: Class[A]): A = injector.getInstance(controllerClass)

}