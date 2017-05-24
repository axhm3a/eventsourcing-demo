import BankAccount.{AllBankAccountsQuery, BankAccountCreateCommand, BankAccountService}
import akka.actor.Props
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import JsonHelper._
import akka.util.Timeout
import counter.{CountCommand, CountQuery, Counter}

import scala.concurrent.duration._
import entities.DemoEntity

import scala.concurrent.Await

import BankAccount._

object Boot extends App with HttpTrait {
  val counter = system.actorOf(Props[Counter])
  val bankAccount = system.actorOf(Props[BankAccountService])
  implicit val timeout = Timeout(1 second)

  val routes = get {
    pathPrefix("id" / LongNumber) {
      id => {
        complete(
          HttpEntity(
            ContentTypes.`application/json`,
            {
              new DemoEntity(id, s"Name is $id") asJson
            }
          )
        )
      }
    } ~ path("ping") {
      complete(
        HttpEntity(
          ContentTypes.`text/html(UTF-8)`,
          "<h1>ping endpoint</h1><p>pong</p>"
        )
      )
    }
  } ~ (path("counter")) {
    post {
        complete(
        HttpEntity(
          ContentTypes.`application/json`, {
            counter ! CountCommand()
            "accepted" asJson
          }
        )
      )
    } ~ get {
      complete(
        HttpEntity(
          ContentTypes.`application/json`, {
            val future = counter ask CountQuery()
            (Await.result(future, timeout.duration).asInstanceOf[Integer]) asJson
          }
        )
      )
    }
  } ~ (path("account") & get) {
    complete(
      {
        val future = bankAccount ask AllBankAccountsQuery()
        (Await.result(future, timeout.duration).asInstanceOf[List[BankAccountId]]) asJson
      }
    )
  } ~ (path("account") & post) {
    parameters('accountOwner.as[String]) {
      (accountOwner) => complete(
        {
          val future = bankAccount ask BankAccountCreateCommand(
            accountOwner
          )
          (Await.result(future, timeout.duration).asInstanceOf[Event]) asJson
        }
      )
    }
  } ~ post {
    pathPrefix("account" / LongNumber / "withdraw") {
      id => {
        parameters('amount.as[Int]) {
          (amount) => complete(
            {
              val future = bankAccount ask WithdrawCommand(
                id,
                amount
              )
              (Await.result(future, timeout.duration).asInstanceOf[Event]) asJson
            }
          )
        }
      }
    }
  } ~ post {
    pathPrefix("account" / LongNumber / "deposit") {
      id => {
        parameters('amount.as[Int]) {
          (amount) => complete(
            {
              val future = bankAccount ask DepositCommand(
                id,
                amount
              )
              (Await.result(future, timeout.duration).asInstanceOf[Event]) asJson
            }
          )
        }
      }
    }
  } ~ get {
    pathPrefix("account" / LongNumber) {
      id => {
        complete(
            {
              val future = bankAccount ask BankAccountQuery(
                id
              )
              (Await.result(future, timeout.duration).asInstanceOf[Amount]) asJson
            }
          )
        }
      }
    }


  val bindingFuture = Http().bindAndHandleAsync(
    Route.asyncHandler(routes),
    "127.0.0.1",
    8080
  )
}
