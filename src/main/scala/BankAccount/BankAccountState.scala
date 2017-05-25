package BankAccount

/**
  * Created by axhm3a on 23.05.17.
  */
case class BankAccountState(events : List[Event] = List()) {

  def update(event: Event) : BankAccountState =
    BankAccountState(event :: events)

  def getAllBankAccountIds: List[BankAccountId] = events
      .collect({case bc: BankAccountCreated => bc})
      .map({bankAccounts => bankAccounts.bankAccountId})

  def getBankAccountBalance(bankAccountId: BankAccountId): Balance = {
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
        case _ => 0
      }
    }: Amount

    Balance(
      events
        .par
        .map(mapToAmount)
        .sum
    )
  }

  def getBankAccountOwner(bankAccountId: BankAccountId): BankAccountOwner = {
    events
      .filter(_.bankAccountId == bankAccountId)
      .collectFirst({case bc: BankAccountCreated => bc})
      .get
      .bankAccountOwner
  }

  def getBankAccount(bankAccountId: BankAccountId): BankAccount = {
    BankAccount(
      bankAccountId,
      getBankAccountOwner(bankAccountId),
      getBankAccountBalance(bankAccountId)
    )
  }
}
