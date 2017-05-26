/**
  * Created by axhm3a on 22.05.17.
  */

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import org.json4s.{Extraction, NoTypeHints}
import org.json4s.jackson.{JsonMethods, Serialization}

object JsonHelper {
  implicit val formats = Serialization.formats(NoTypeHints)

  implicit class BetterJsonHelper(x: Any) {
    def asJson(): String = {
      JsonMethods.mapper.writeValueAsString(Extraction.decompose(x)(formats))
    }

    def asJsonEntity(): HttpEntity.Strict = {
      HttpEntity(
        ContentTypes.`application/json`,
        x.asJson()
      )
    }
  }
}
