package BankAccount

/**
  * Created by axhm3a on 23.05.17.
  */
sealed trait Command {

}

case class BankAccountCreateCommand(bankAccountOwner: BankAccountOwner) extends Command
case class WithdrawCommand(bankAccountId: BankAccountId, amount: Amount) extends Command
case class DepositCommand(bankAccountId: BankAccountId, amount: Amount) extends Command
