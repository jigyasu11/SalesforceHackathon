package dao

import play.api.db.slick.Config.driver.simple._
import java.util.UUID
import models._
import play.api.libs.json._
import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.ProvenShape.proveShapeOf

class LocationTable(tag: Tag) extends Table[Location](tag, "location") {
  
  def _id = column[Long]("_id", O.PrimaryKey, O.AutoInc)
  def userId = column[String]("user_id")
  def latitude = column[Double]("latitude")
  def longitude = column[Double]("longitude")
  def deviceId = column[String]("device_id")
  def deviceName = column[String]("device_name")
  def battery = column[Long]("battery")
  def pingTime = column[Long]("ping_time")
  
  def * = (_id, userId, latitude, longitude, deviceId, deviceName, battery, pingTime) <> (Location.tupled, Location.unapply)

}



