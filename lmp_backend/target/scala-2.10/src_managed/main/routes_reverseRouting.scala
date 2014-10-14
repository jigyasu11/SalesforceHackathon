// @SOURCE:/Users/rarya/android/lmp_backend/conf/routes
// @HASH:4e5066f0ae8aa75deddfb0f735734d818466e202
// @DATE:Sun Oct 12 01:09:04 PDT 2014

import Routes.{prefix => _prefix, defaultPrefix => _defaultPrefix}
import play.core._
import play.core.Router._
import play.core.j._

import play.api.mvc._


import Router.queryString


// @LINE:18
// @LINE:15
// @LINE:12
// @LINE:10
// @LINE:8
// @LINE:6
package controllers {

// @LINE:18
class ReverseAssets {
    

// @LINE:18
def at(file:String): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[PathBindable[String]].unbind("file", file))
}
                                                
    
}
                          

// @LINE:15
// @LINE:12
// @LINE:10
// @LINE:8
// @LINE:6
class ReverseApplication {
    

// @LINE:15
def post(): Call = {
   Call("POST", _prefix + { _defaultPrefix } + "save")
}
                                                

// @LINE:10
def getChargeAllOtherUsers(user:String): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "otherUsers" + queryString(List(Some(implicitly[QueryStringBindable[String]].unbind("user", user)))))
}
                                                

// @LINE:12
def getLocationOfAllOtherUsers(device:String): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "location" + queryString(List(Some(implicitly[QueryStringBindable[String]].unbind("device", device)))))
}
                                                

// @LINE:8
def get(user:String, device:String): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "userInfo" + queryString(List(Some(implicitly[QueryStringBindable[String]].unbind("user", user)), Some(implicitly[QueryStringBindable[String]].unbind("device", device)))))
}
                                                

// @LINE:6
def index(): Call = {
   Call("GET", _prefix)
}
                                                
    
}
                          
}
                  


// @LINE:18
// @LINE:15
// @LINE:12
// @LINE:10
// @LINE:8
// @LINE:6
package controllers.javascript {

// @LINE:18
class ReverseAssets {
    

// @LINE:18
def at : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Assets.at",
   """
      function(file) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "assets/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("file", file)})
      }
   """
)
                        
    
}
              

// @LINE:15
// @LINE:12
// @LINE:10
// @LINE:8
// @LINE:6
class ReverseApplication {
    

// @LINE:15
def post : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.post",
   """
      function() {
      return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "save"})
      }
   """
)
                        

// @LINE:10
def getChargeAllOtherUsers : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.getChargeAllOtherUsers",
   """
      function(user) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "otherUsers" + _qS([(""" + implicitly[QueryStringBindable[String]].javascriptUnbind + """)("user", user)])})
      }
   """
)
                        

// @LINE:12
def getLocationOfAllOtherUsers : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.getLocationOfAllOtherUsers",
   """
      function(device) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "location" + _qS([(""" + implicitly[QueryStringBindable[String]].javascriptUnbind + """)("device", device)])})
      }
   """
)
                        

// @LINE:8
def get : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.get",
   """
      function(user,device) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "userInfo" + _qS([(""" + implicitly[QueryStringBindable[String]].javascriptUnbind + """)("user", user), (""" + implicitly[QueryStringBindable[String]].javascriptUnbind + """)("device", device)])})
      }
   """
)
                        

// @LINE:6
def index : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.index",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + """"})
      }
   """
)
                        
    
}
              
}
        


// @LINE:18
// @LINE:15
// @LINE:12
// @LINE:10
// @LINE:8
// @LINE:6
package controllers.ref {


// @LINE:18
class ReverseAssets {
    

// @LINE:18
def at(path:String, file:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Assets.at(path, file), HandlerDef(this, "controllers.Assets", "at", Seq(classOf[String], classOf[String]), "GET", """ Map static resources from the /public folder to the /assets URL path""", _prefix + """assets/$file<.+>""")
)
                      
    
}
                          

// @LINE:15
// @LINE:12
// @LINE:10
// @LINE:8
// @LINE:6
class ReverseApplication {
    

// @LINE:15
def post(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   play.api.Play.maybeApplication.map(_.global).getOrElse(play.api.DefaultGlobal).getControllerInstance(classOf[controllers.Application]).post(), HandlerDef(this, "controllers.Application", "post", Seq(), "POST", """""", _prefix + """save""")
)
                      

// @LINE:10
def getChargeAllOtherUsers(user:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   play.api.Play.maybeApplication.map(_.global).getOrElse(play.api.DefaultGlobal).getControllerInstance(classOf[controllers.Application]).getChargeAllOtherUsers(user), HandlerDef(this, "controllers.Application", "getChargeAllOtherUsers", Seq(classOf[String]), "GET", """""", _prefix + """otherUsers""")
)
                      

// @LINE:12
def getLocationOfAllOtherUsers(device:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   play.api.Play.maybeApplication.map(_.global).getOrElse(play.api.DefaultGlobal).getControllerInstance(classOf[controllers.Application]).getLocationOfAllOtherUsers(device), HandlerDef(this, "controllers.Application", "getLocationOfAllOtherUsers", Seq(classOf[String]), "GET", """""", _prefix + """location""")
)
                      

// @LINE:8
def get(user:String, device:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   play.api.Play.maybeApplication.map(_.global).getOrElse(play.api.DefaultGlobal).getControllerInstance(classOf[controllers.Application]).get(user, device), HandlerDef(this, "controllers.Application", "get", Seq(classOf[String], classOf[String]), "GET", """""", _prefix + """userInfo""")
)
                      

// @LINE:6
def index(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   play.api.Play.maybeApplication.map(_.global).getOrElse(play.api.DefaultGlobal).getControllerInstance(classOf[controllers.Application]).index(), HandlerDef(this, "controllers.Application", "index", Seq(), "GET", """ Home page""", _prefix + """""")
)
                      
    
}
                          
}
        
    