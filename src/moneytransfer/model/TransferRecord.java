package moneytransfer.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Elena Chinarina
 *
 **/

public class TransferRecord {
    private static final DateTimeFormatter FILE_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String sourceFile;
    private final TransferStatus status;
    private final String fromAccount;
    private final String toAccount;
    private final BigDecimal amount;
    private final LocalDateTime timestamp;
    private String errorMessage;

    public TransferRecord(String sourceFile, TransferStatus status,
                          String fromAccount, String toAccount,
                          BigDecimal amount, LocalDateTime timestamp) {
        this.sourceFile = sourceFile;
        this.status = status;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    // Геттеры
    public String getSourceFile() { return sourceFile; }
    public TransferStatus getStatus() { return status; }
    public String getFromAccount() { return fromAccount; }
    public String getToAccount() { return toAccount; }
    public BigDecimal getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getErrorMessage() { return errorMessage; }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String toFileString() {
        String dateStr = timestamp.format(FILE_DATE_FORMATTER);
        String amountStr = amount.setScale(2, RoundingMode.HALF_UP).toPlainString();

        StringBuilder sb = new StringBuilder();
        sb.append(sourceFile)
                .append(" | ")
                .append(status.name())
                .append(" | ")
                .append(fromAccount)
                .append(" | ")
                .append(toAccount)
                .append(" | ")
                .append(amountStr)
                .append(" | ")
                .append(dateStr);

        if (errorMessage != null && !errorMessage.isEmpty()) {
            sb.append(" | ").append(errorMessage);
        }

        return sb.toString();
    }
}
