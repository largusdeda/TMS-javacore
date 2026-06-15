package moneytransfer.service;

import moneytransfer.dto.ParsedTransferData;
import moneytransfer.exception.InsufficientFundsException;
import moneytransfer.exception.InvalidAccountException;
import moneytransfer.exception.InvalidAmountException;
import moneytransfer.exception.SameAccountException;
import moneytransfer.model.Account;
import moneytransfer.model.TransferRecord;
import moneytransfer.model.TransferStatus;
import moneytransfer.repository.AccountRepository;
import moneytransfer.repository.ReportRepository;
import moneytransfer.validator.TransferValidator;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author Elena Chinarina
 *
 **/

public class TransferService {
    private final AccountRepository accountRepository;
    private final ReportRepository reportRepository;

    public TransferService(AccountRepository accountRepository, ReportRepository reportRepository) {
        this.accountRepository = accountRepository;
        this.reportRepository = reportRepository;
    }

    public void executeTransfer(ParsedTransferData data) {
        TransferRecord record;

        try {
            // Валидация номеров счетов
            TransferValidator.validateAccountNumber(data.getFromAccount());
            TransferValidator.validateAccountNumber(data.getToAccount());

            // Валидация суммы
            TransferValidator.validateAmount(data.getAmount());

            // Загрузка актуальных счетов
            Map<String, Account> accounts = accountRepository.loadAccounts();

            // Проверка существования счетов
            TransferValidator.validateAccountExists(data.getFromAccount(), accounts);
            TransferValidator.validateAccountExists(data.getToAccount(), accounts);

            // Проверка, что счета разные
            TransferValidator.validateDifferentAccounts(data.getFromAccount(), data.getToAccount());

            // Проверка достаточности средств
            Account fromAccount = accounts.get(data.getFromAccount());
            if (fromAccount.getBalance().compareTo(data.getAmount()) < 0) {
                throw new InsufficientFundsException(
                        "Недостаточно средств на счете " + data.getFromAccount() +
                                ". Баланс: " + fromAccount.getBalance() +
                                ", требуется: " + data.getAmount());
            }

            // Выполнение перевода
            Account toAccount = accounts.get(data.getToAccount());
            fromAccount.debit(data.getAmount());
            toAccount.credit(data.getAmount());

            // Сохранение изменений
            accountRepository.saveAccounts(accounts);

            // Формирование успешной запись
            record = new TransferRecord(
                    data.getSourceFileName(),
                    TransferStatus.SUCCESS,
                    data.getFromAccount(),
                    data.getToAccount(),
                    data.getAmount(),
                    LocalDateTime.now()
            );

        } catch (InvalidAccountException e) {
            record = createErrorRecord(data, TransferStatus.INVALID_ACCOUNT, e.getMessage());
        } catch (InvalidAmountException e) {
            record = createErrorRecord(data, TransferStatus.INVALID_AMOUNT, e.getMessage());
        } catch (InsufficientFundsException e) {
            record = createErrorRecord(data, TransferStatus.INSUFFICIENT_FUNDS, e.getMessage());
        } catch (SameAccountException e) {
            record = createErrorRecord(data, TransferStatus.SAME_ACCOUNT, e.getMessage());
        } catch (Exception e) {
            record = createErrorRecord(data, TransferStatus.FILE_ERROR,
                    "Неожиданная ошибка: " + e.getMessage());
        }

        // Запись в отчет
        reportRepository.appendRecord(record);
    }

    private TransferRecord createErrorRecord(ParsedTransferData data,
                                             TransferStatus status,
                                             String errorMessage) {
        TransferRecord record = new TransferRecord(
                data.getSourceFileName(),
                status,
                data.getFromAccount(),
                data.getToAccount(),
                data.getAmount(),
                LocalDateTime.now()
        );
        record.setErrorMessage(errorMessage);

        return record;
    }
}