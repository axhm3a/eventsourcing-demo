package BankAccount

import org.scalatest.FunSuite

/**
  * Created by axhm3a on 29.05.17.
  */
class BankAccountFactorySuite extends FunSuite {
  test("test BankAccountCreated") {
    assert(
      Event(BankAccountCreateCommand("bar"), BankAccountState()) ==
      BankAccountCreated(0, "bar")
    )
  }

  test("test BankAccountDeposit") {
    assert(
      Event(DepositCommand(0, 55.55),
        BankAccountState(List(BankAccountCreated(0, "bar")))
      ) == AmountDeposited(0, 55.55)
    )
  }

  test("test BankAccountWithdraw") {
    assert(
      Event(WithdrawCommand(0, 10.0),
        BankAccountState(List(
          BankAccountCreated(0, "bar"),
          AmountDeposited(0, 25.0)))
      ) == AmountWithdrawn(0, 10.0)
    )
  }
}
