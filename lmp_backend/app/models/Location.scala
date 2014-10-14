package models

import play.api.Play.current
import java.util.UUID
import java.sql.Timestamp
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import Implicits._

case class Location(_id: Long, userId: String, latitude: Double, longitude: Double, 
    deviceId: String, deviceName: String, battery: Long, pingTime: Long) {
  
  def toJson: JsValue = Json.toJson(this)

  override def toString() = toJson.toString
}

