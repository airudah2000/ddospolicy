package models

import play.api.libs.json.{JsValue, Json}
import play.api.libs.json.Json._

import java.util.logging.Logger
import scala.io.Source
import scala.util.{Try, Using}

trait DdosJson {
  val logger: Logger = Logger.getLogger(this.getClass.getSimpleName)
  def getJsonString(jsonFileLocation: String): String = Using(Source.fromFile(jsonFileLocation))(_.mkString).fold(throw _, str => str)
  def getAsJSValue(jsonFileLocation: String): JsValue = Json.parse(getJsonString(jsonFileLocation))

  def getEventFromJsonString(jsonString: String): Seq[Event] =
    Try(parse(jsonString)
      .validate[List[Event]]
      .getOrElse(Nil)
    ).fold((error: Throwable) => {
      logger.log(java.util.logging.Level.INFO, s"Cant parse json string: $jsonString ")
      throw error
    },
      (events: Seq[Event]) => events
    )
}
