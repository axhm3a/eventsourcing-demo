/**
  * Created by axhm3a on 28.05.17.
  */
package BankAccount

import org.scalatest.FlatSpec

class BankAccountSuite extends FlatSpec {
  "A new bankaccount" should "have zero balance" in {
    assert(
      BankAccountState(
        List(BankAccountCreated(
          1,
          "foo"
        ))
      ).getBankAccountBalance(1) == Balance(0))
  }

  "A deposit of 500" should "should be credited on the balance" in  {
    val events = List (
      BankAccountCreated(1, "foo"),
      AmountDeposited(1, 500.0)
    )

    assert(
      BankAccountState(events).getBankAccountBalance(1) ==
      Balance(500)
    )
  }
}
