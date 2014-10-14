package dao

import java.util.UUID
import models.Location

trait Dao {
  
  def getInfo(user: String, device: String) : List[Location]
  
  def getInfoForAllOtherUsers(user: String) : List[Location]
    
  def insert(data: Location)
  
  def getAllDevices(deviceName: String) : List[Location]
  
}