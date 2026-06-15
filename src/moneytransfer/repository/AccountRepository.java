package moneytransfer.repository;

import moneytransfer.model.Account;
import moneytransfer.util.FileUtils;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Elena Chinarina
 *
 **/

public class AccountRepository {
    private final String accountsFile;

    public AccountRepository(String accountsFile) {
        this.accountsFile = accountsFile;
    }

    public Map<String, Account> loadAccounts() {
        List<String> lines = FileUtils.readLines(accountsFile);
        Map<String, Account> accounts = new LinkedHashMap<>();

        for (String line : lines) {
            try {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String accountNumber = parts[0].trim();
                    BigDecimal balance = new BigDecimal(parts[1].trim());
                    accounts.put(accountNumber, new Account(accountNumber, balance));
                }
            } catch (NumberFormatException e) {
                System.err.println("Ошибка парсинга строки в файле счетов: " + line);
            }
        }
        return accounts;
    }

    public void saveAccounts(Map<String, Account> accounts) {
        List<String> lines = accounts.values().stream()
                .map(Account::toString)
                .collect(Collectors.toList());
        FileUtils.writeLines(accountsFile, lines);
    }
}
