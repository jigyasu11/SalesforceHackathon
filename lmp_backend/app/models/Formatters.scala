package models

import java.util.UUID

import scala.util.Failure
import scala.util.Success
import scala.util.Try

import play.api.data.validation.ValidationError
import play.api.libs.json._



/**
 * Formatter utilities which are required for JSON parsing
 */
object Formatters {

  def uuidFormat: Format[UUID] = new Format[UUID] {
    def writes(uuid: UUID): JsString = JsString(uuid.toString)
    def reads(value: JsValue): JsResult[UUID] = value match {
      case JsString(x) => Try(UUID.fromString(x)) match {
        case Success(uuid) => JsSuccess(uuid)
        case Failure(msg) => JsError(__ \ 'UUID, ValidationError("validate.error.invalidUUID", msg))
      }
      case _ => JsError(__ \ 'UUID, ValidationError("validate.error.invalidUUID", "Missing UUID String"))
    }
  }

}

object Implicits {

  implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext
  implicit val defaultUUIDFormat = Formatters.uuidFormat
  
  implicit val locationFormatter = Json.format[Location]

}