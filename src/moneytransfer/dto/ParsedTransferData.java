package moneytransfer.dto;

import java.math.BigDecimal;

/**
 * @author Elena Chinarina
 *
 **/

public class ParsedTransferData {
    private final String fromAccount;
    private final String toAccount;
    private final BigDecimal amount;
    private final String sourceFileName;

    public ParsedTransferData(String fromAccount, String toAccount,
                              BigDecimal amount, String sourceFileName) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.sourceFileName = sourceFileName;
    }

    // Геттеры
    public String getFromAccount() { return fromAccount; }
    public String getToAccount() { return toAccount; }
    public BigDecimal getAmount() { return amount; }
    public String getSourceFileName() { return sourceFileName; }
}