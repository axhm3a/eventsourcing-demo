package BankAccount

/**
  * Created by axhm3a on 23.05.17.
  */
case class BankAccountState(events : List[Event] = List()) {

  def update(event: Event) : BankAccountState =
    BankAccountState(event :: events)

  def getAllBankAccountIds: List[BankAccountId] = events
      .collect({case bc: BankAccountCreated => bc})
      .map(bankAccounts => bankAccounts.bankAccountId)

  def getBankAccountBalance(bankAccountId: BankAccountId): Amount = {
    val mapToAmount = (event: Event) => { //maybe with try and filter?
      event match {
        case event: AmountWithdrawn => {
          if(event.bankAccountId == bankAccountId)
            -event.amount
          else
            0
        }
        case event: AmountDeposited => {
          if(event.bankAccountId == bankAccountId)
            event.amount
          else
            0
        }
        case _ => 0.0
      }
    }: Amount

    events
      .map(mapToAmount)
      .reduce(_ + _)
  }
}
