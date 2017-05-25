package BankAccount

/**
  * Created by axhm3a on 23.05.17.
  */
sealed trait Query

case class AllBankAccountsQuery()
case class BankAccountQuery(bankAccountId: BankAccountId)