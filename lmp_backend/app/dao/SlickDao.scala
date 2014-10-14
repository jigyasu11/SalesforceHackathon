package dao

import models.Location
import play.api.Play.current
import play.api.libs.json._
import java.util.UUID
import play.api.db.slick._
import play.api.db.slick.Config.driver.simple._
import play.api.Logger
import javax.inject.Singleton
import scala.slick.lifted.TableQuery
import scala.compat.Platform

@Singleton
class SlickDao extends Dao {
  
  private val location = TableQuery[LocationTable]
  
  def getInfo(user: String, device: String) : List[Location] = DB.withSession { implicit session =>
    val time = Platform.currentTime
    val tMinusTwoHours = time - 7200000
    //location.list
    location.filter(_.userId === user).filter(_.deviceId === device).filter(
        _.pingTime <= time).
        sortBy(_.pingTime.desc.nullsLast).list.slice(0, 20)
  }
  
  def getInfoForAllOtherUsers(user: String) : List[Location] = DB.withSession { implicit session =>
    val time = Platform.currentTime
    val tMinusTwoHours = time - 7200000
    //location.list
    location.filter(_.userId =!= user).filter(
        _.pingTime <= time).
        sortBy(_.pingTime.desc.nullsLast).list.slice(0, 20)
  }
  
  def insert(data: Location) = DB.withSession { implicit session =>
    location.insert(data)
  }
  
  def getAllDevices(deviceName: String) : List[Location] = DB.withSession {implicit session =>
    val time = Platform.currentTime
    val tMinusTwoHours = time - 7200000
    location.filter(_.deviceName === deviceName).filter(
        _.pingTime <= time).
        sortBy(_.pingTime.desc.nullsLast).list.slice(0, 20)
  }

}