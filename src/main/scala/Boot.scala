import akka.actor.Props
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._
import scala.concurrent.Await
import BankAccount._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.ExceptionHandler
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import spray.json.{DefaultJsonProtocol, PrettyPrinter}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val printer = PrettyPrinter

  implicit val balanceFormat = jsonFormat1(Balance)
  implicit val bankAccountFormat = jsonFormat3(BankAccount)
  implicit val amountDepositedFormat = jsonFormat2(AmountDeposited)
  implicit val amountWithdrawnFormat = jsonFormat2(AmountWithdrawn)
  implicit val bankAccountCreatedFormat = jsonFormat2(BankAccountCreated)
}


object Boot extends App with HttpTrait with JsonSupport{
  val bankAccount = system.actorOf(Props[BankAccountService])
  implicit val timeout = Timeout(500 millis)

  val routes = (get & path("account")) {
    complete(
      Await.result(
        bankAccount ask AllBankAccountsQuery(),
        timeout.duration
      ).asInstanceOf[List[BankAccountId]]
    )
  } ~ (post & path("account")) {
    parameters('accountOwner.as[String]) {
      (accountOwner) => complete(
        Await.result(
          bankAccount ask BankAccountCreateCommand(accountOwner),
          timeout.duration
        ).asInstanceOf[BankAccountCreated]
      )
    }
  } ~ (post & pathPrefix("account" / LongNumber / "withdraw")) {
    id => parameters('amount.as[Double]) {
      (amount) => complete(
        Await.result(
          bankAccount ask WithdrawCommand(id, BigDecimal.valueOf(amount)),
          timeout.duration
        ).asInstanceOf[AmountWithdrawn]
      )
    }
  } ~ (post & pathPrefix("account" / LongNumber / "deposit")) {
    id => parameters('amount.as[Double]) {
      (amount) => complete(
        Await.result(
          bankAccount ask DepositCommand(id, BigDecimal.valueOf(amount)),
          timeout.duration
        ).asInstanceOf[AmountDeposited]
      )
    }
  } ~ (get & pathPrefix("account" / LongNumber)) {
    id => complete(
      Await.result(
        bankAccount ask BankAccountQuery(id),
        timeout.duration
      ).asInstanceOf[BankAccount]
    )
  }

  implicit def myExceptionHandler: ExceptionHandler =
    ExceptionHandler {
      case e: ClassCastException =>
        extractUri { uri =>
          complete(
            HttpResponse(
              StatusCodes.InternalServerError,
              entity = e.getMessage
            )
          )
        }
    }

  val bindingFuture = Http().bindAndHandleAsync(
    Route.asyncHandler(routes),
    "127.0.0.1",
    8080
  )
}
