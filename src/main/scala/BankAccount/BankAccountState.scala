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

  def getBankAccountBalance(bankAccountId: BankAccountId): Balance =
    Balance(
      events
        .par
        .filter(_.bankAccountId == bankAccountId)
        .collect({case t: TransactionEvent => t})
        .map(getTransactionAmount)
        .sum
    )

  def getBankAccountOwner(bankAccountId: BankAccountId): BankAccountOwner =
    events
      .filter(_.bankAccountId == bankAccountId)
      .collectFirst({case bc: BankAccountCreated => bc})
      .get
      .bankAccountOwner

  def getBankAccount(bankAccountId: BankAccountId): BankAccount =
    BankAccount(
      bankAccountId,
      getBankAccountOwner(bankAccountId),
      getBankAccountBalance(bankAccountId)
    )

  private def getTransactionAmount = (event: TransactionEvent) =>
    event match {
      case event: AmountWithdrawn =>
        -event.amount
      case event: AmountDeposited =>
        event.amount
    }
}
