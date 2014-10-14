package controllers

import play.api._
import play.api.mvc._
import models.Location
import java.util.UUID
import play.api.libs.json.Json
import com.google.inject.Singleton
import com.google.inject.Inject
import play.api.libs.json._
import play.api.libs.functional.syntax._
import dao.Dao
import java.sql.Timestamp
import scala.util.Random
import models.Implicits._

@Singleton
class Application @Inject() (val dao: Dao) extends Controller {

  val dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  def index = Action {
    Ok("LMP is shining!")
  }
  
  private def getAverageCharge(list: List[Location]) : Double = {
    if (list.length > 0) {
       list.map(_.battery).sum / list.length
    } else {
      0.0
    }
      
  }
  
  def get(user: String, device: String) = Action {
    
    val locationList = dao.getInfo(user, device)
    
    val locations = Json.obj("locations" -> locationList)
    
    // val avgCharge = getAverageCharge(locationList)
    // Return the average charge    
    Ok(locations)
  }
  
  def getChargeAllOtherUsers(user: String) = Action {
    val locationList = dao.getInfoForAllOtherUsers(user)
    
    //val avgCharge = getAverageCharge(locationList)
    // Return the average charge
    val locations = Json.obj("locations" -> locationList)
    Ok(locations)
  }
  
  def getLocationOfAllOtherUsers(device: String) = Action {
    val locationList = dao.getAllDevices(device)
    val locations = Json.obj("locations" -> locationList)
    Ok(locations)
  }
  
  implicit val rds = (
    (__ \ 'userId).read[String] and
    (__ \ 'latitude).read[Double] and
    (__ \ 'longitude).read[Double] and
    (__ \ 'deviceId).read[String] and
    (__ \ 'deviceName).read[String] and
    (__ \ 'battery).read[Long] and
    (__ \ 'pingTime).read[Long]
  ) tupled

  def post = Action { request =>
    request.body.asJson.map { json =>
      json.validate[(String, Double, Double, String, String, Long, Long)].map{ 
        case (userId, latitude, longitude, deviceId, deviceName, battery, pingTime) => {
          val location = Location(Random.nextInt, userId, latitude, longitude, deviceId, deviceName, battery, pingTime)
	      dao.insert(location)
	      Ok("Success")
        }
      }.recoverTotal{
        e => BadRequest("Detected error:"+ JsError.toFlatJson(e))
      }
    }.getOrElse {
      BadRequest("Expecting Json data")
    }
  }
}