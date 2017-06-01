package BankAccount

import scala.util.{Failure, Success, Try}

/**
  * Created by axhm3a on 23.05.17.
  */
sealed trait Event {
  def bankAccountId: BankAccountId
}

sealed trait TransactionEvent extends Event {
  def amount: Amount
}

case class BankAccountCreated(bankAccountId: BankAccountId, bankAccountOwner: BankAccountOwner) extends Event
case class AmountDeposited(bankAccountId: BankAccountId, amount: Amount) extends TransactionEvent
case class AmountWithdrawn(bankAccountId: BankAccountId, amount: Amount) extends TransactionEvent

object Event {
  def apply(command: Command, state: BankAccountState): Try[Event] = command match {
    case bcc : BankAccountCreateCommand =>
      Success(BankAccountCreated(
        state.getAllBankAccountIds.length,
        bcc.bankAccountOwner
      ))

    case bwc: WithdrawCommand =>
      if(state.getBankAccountBalance(bwc.bankAccountId).amount - bwc.amount >= 0)
        Success(AmountWithdrawn(
          bwc.bankAccountId,
          bwc.amount
        ))
      else
        Failure(new Exception("not enough money"))

    case bwc: DepositCommand =>
      if(state.getAllBankAccountIds.contains(bwc.bankAccountId))
        Success(AmountDeposited(
          bwc.bankAccountId,
          bwc.amount
        ))
      else
        Failure(new Exception("unknown account"))
  }
}
