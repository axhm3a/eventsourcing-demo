/**
  * Created by axhm3a on 22.05.17.
  */

import org.json4s.{Extraction, NoTypeHints}
import org.json4s.jackson.{JsonMethods, Serialization}

object JsonHelper {
  implicit val formats = Serialization.formats(NoTypeHints)

  implicit class BetterJsonHelper(x: Any) {
    def asJson(): String = {
      JsonMethods.mapper.writeValueAsString(Extraction.decompose(x)(formats))
    }
  }
}
