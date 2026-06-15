package moneytransfer.validator;

import moneytransfer.exception.InvalidAccountException;
import moneytransfer.exception.InvalidAmountException;
import moneytransfer.exception.SameAccountException;
import moneytransfer.model.Account;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Elena Chinarina
 *
 **/

public class TransferValidator {
    private static final String ACCOUNT_PATTERN = "^\\d{5}-\\d{5}$";

    public static void validateAccountNumber(String accountNumber) {
        if (accountNumber == null || !accountNumber.matches(ACCOUNT_PATTERN)) {
            throw new InvalidAccountException("Неверный формат счета: " + accountNumber +
                    ". Ожидается формат: XXXXX-XXXXX (10 цифр)");
        }
    }

    public static void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new InvalidAmountException("Сумма не может быть null");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Сумма перевода должна быть положительной. Получено: " + amount);
        }
    }

    public static void validateDifferentAccounts(String fromAccount, String toAccount) {
        if (fromAccount.equals(toAccount)) {
            throw new SameAccountException("Счета отправителя и получателя не должны совпадать: " + fromAccount);
        }
    }

    public static void validateAccountExists(String accountNumber, Map<String, Account> accounts) {
        if (!accounts.containsKey(accountNumber)) {
            throw new InvalidAccountException("Счет не найден: " + accountNumber);
        }
    }
}

