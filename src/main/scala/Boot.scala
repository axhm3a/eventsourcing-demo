import akka.actor.Props
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import JsonHelper._
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.Await
import BankAccount._

object Boot extends App with HttpTrait {
  val bankAccount = system.actorOf(Props[BankAccountService])
  implicit val timeout = Timeout(500 millis)

  val routes = (get & path("account")) {
    complete(
      Await.result(
        bankAccount ask AllBankAccountsQuery(),
        timeout.duration
      ).asJsonEntity()
    )
  } ~ (post & path("account")) {
    parameters('accountOwner.as[String]) {
      (accountOwner) => complete(
        Await.result(
          bankAccount ask BankAccountCreateCommand(accountOwner),
          timeout.duration
        ).asJsonEntity()
      )
    }
  } ~ (post & pathPrefix("account" / LongNumber / "withdraw")) {
    id => parameters('amount.as[Double]) {
      (amount) => complete(
        Await.result(
          bankAccount ask WithdrawCommand(id, BigDecimal.valueOf(amount)),
          timeout.duration
        ).asJsonEntity()
      )
    }
  } ~ (post & pathPrefix("account" / LongNumber / "deposit")) {
    id => parameters('amount.as[Double]) {
      (amount) => complete(
        Await.result(
          bankAccount ask DepositCommand(id, BigDecimal.valueOf(amount)),
          timeout.duration
        ).asJsonEntity()
      )
    }
  } ~ (get & pathPrefix("account" / LongNumber)) {
    id => complete(
      Await.result(
        bankAccount ask BankAccountQuery(id),
        timeout.duration
      ).asJsonEntity()
    )
  }

  val bindingFuture = Http().bindAndHandleAsync(
    Route.asyncHandler(routes),
    "127.0.0.1",
    8080
  )
}
