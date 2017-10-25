sealed trait BankingError
sealed trait CreditError extends BankingError

case object NegativeAmount extends CreditError
case object AccountDisabled extends BankingError

sealed trait DebitError extends BankingError
case object NotEnoughMoney extends DebitError

object BankingService {
  def debit(account: Account, amount: BigDecimal): Either[BankingError, (Account, Money)] = {
    if (!account.enabled) {
      Left(AccountDisabled)
    }
    else if (account.money.amount < amount) Left(NotEnoughMoney)
    else {
      val newMoney = account.money.copy(amount = account.money.amount - amount)
      val newAccount = account.copy(money = newMoney)
      Right(newAccount, Money(amount, account.money.currency))
    }
  }

  def credit(account: Account, amount: BigDecimal): Either[BankingError, (Account, Money)] = {
    if (!account.enabled) {
      Left(AccountDisabled)
    }
    else if (amount < 0) Left(NegativeAmount)
    else {
      val newMoney = account.money.copy(amount = account.money.amount + amount)
      val newAccount = account.copy(money = newMoney)
      Right(newAccount, Money(amount, account.money.currency))
    }
  }

}