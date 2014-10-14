// @SOURCE:/Users/rarya/android/lmp_backend/conf/routes
// @HASH:4e5066f0ae8aa75deddfb0f735734d818466e202
// @DATE:Sun Oct 12 01:09:04 PDT 2014


import play.core._
import play.core.Router._
import play.core.j._

import play.api.mvc._


import Router.queryString

object Routes extends Router.Routes {

private var _prefix = "/"

def setPrefix(prefix: String) {
  _prefix = prefix
  List[(String,Routes)]().foreach {
    case (p, router) => router.setPrefix(prefix + (if(prefix.endsWith("/")) "" else "/") + p)
  }
}

def prefix = _prefix

lazy val defaultPrefix = { if(Routes.prefix.endsWith("/")) "" else "/" }


// @LINE:6
private[this] lazy val controllers_Application_index0 = Route("GET", PathPattern(List(StaticPart(Routes.prefix))))
        

// @LINE:8
private[this] lazy val controllers_Application_get1 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("userInfo"))))
        

// @LINE:10
private[this] lazy val controllers_Application_getChargeAllOtherUsers2 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("otherUsers"))))
        

// @LINE:12
private[this] lazy val controllers_Application_getLocationOfAllOtherUsers3 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("location"))))
        

// @LINE:15
private[this] lazy val controllers_Application_post4 = Route("POST", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("save"))))
        

// @LINE:18
private[this] lazy val controllers_Assets_at5 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("assets/"),DynamicPart("file", """.+""",false))))
        
def documentation = List(("""GET""", prefix,"""@controllers.Application@.index"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """userInfo""","""@controllers.Application@.get(user:String, device:String)"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """otherUsers""","""@controllers.Application@.getChargeAllOtherUsers(user:String)"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """location""","""@controllers.Application@.getLocationOfAllOtherUsers(device:String)"""),("""POST""", prefix + (if(prefix.endsWith("/")) "" else "/") + """save""","""@controllers.Application@.post"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """assets/$file<.+>""","""controllers.Assets.at(path:String = "/public", file:String)""")).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
  case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
  case l => s ++ l.asInstanceOf[List[(String,String,String)]] 
}}
      

def routes:PartialFunction[RequestHeader,Handler] = {

// @LINE:6
case controllers_Application_index0(params) => {
   call { 
        invokeHandler(play.api.Play.maybeApplication.map(_.global).getOrElse(play.api.DefaultGlobal).getControllerInstance(classOf[controllers.Application]).index, HandlerDef(this, "controllers.Application", "index", Nil,"GET", """ Home page""", Routes.prefix + """"""))
   }
}
        

// @LINE:8
case controllers_Application_get1(params) => {
   call(params.fromQuery[String]("user", None), params.fromQuery[String]("device", None)) { (user, device) =>
        invokeHandler(play.api.Play.maybeApplication.map(_.global).getOrElse(play.api.DefaultGlobal).getControllerInstance(classOf[controllers.Application]).get(user, device), HandlerDef(this, "controllers.Application", "get", Seq(classOf[String], classOf[String]),"GET", """""", Routes.prefix + """userInfo"""))
   }
}
        

// @LINE:10
case controllers_Application_getChargeAllOtherUsers2(params) => {
   call(params.fromQuery[String]("user", None)) { (user) =>
        invokeHandler(play.api.Play.maybeApplication.map(_.global).getOrElse(play.api.DefaultGlobal).getControllerInstance(classOf[controllers.Application]).getChargeAllOtherUsers(user), HandlerDef(this, "controllers.Application", "getChargeAllOtherUsers", Seq(classOf[String]),"GET", """""", Routes.prefix + """otherUsers"""))
   }
}
        

// @LINE:12
case controllers_Application_getLocationOfAllOtherUsers3(params) => {
   call(params.fromQuery[String]("device", None)) { (device) =>
        invokeHandler(play.api.Play.maybeApplication.map(_.global).getOrElse(play.api.DefaultGlobal).getControllerInstance(classOf[controllers.Application]).getLocationOfAllOtherUsers(device), HandlerDef(this, "controllers.Application", "getLocationOfAllOtherUsers", Seq(classOf[String]),"GET", """""", Routes.prefix + """location"""))
   }
}
        

// @LINE:15
case controllers_Application_post4(params) => {
   call { 
        invokeHandler(play.api.Play.maybeApplication.map(_.global).getOrElse(play.api.DefaultGlobal).getControllerInstance(classOf[controllers.Application]).post, HandlerDef(this, "controllers.Application", "post", Nil,"POST", """""", Routes.prefix + """save"""))
   }
}
        

// @LINE:18
case controllers_Assets_at5(params) => {
   call(Param[String]("path", Right("/public")), params.fromPath[String]("file", None)) { (path, file) =>
        invokeHandler(controllers.Assets.at(path, file), HandlerDef(this, "controllers.Assets", "at", Seq(classOf[String], classOf[String]),"GET", """ Map static resources from the /public folder to the /assets URL path""", Routes.prefix + """assets/$file<.+>"""))
   }
}
        
}

}
     