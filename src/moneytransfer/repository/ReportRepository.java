package moneytransfer.repository;

import moneytransfer.model.TransferRecord;
import moneytransfer.model.TransferStatus;
import moneytransfer.util.DateUtils;
import moneytransfer.util.FileUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Elena Chinarina
 *
 **/

public class ReportRepository {
    private final String reportFile;

    public ReportRepository(String reportFile) {
        this.reportFile = reportFile;
    }

    public void appendRecord(TransferRecord record) {
        FileUtils.appendLine(reportFile, record.toFileString());
    }

    public List<TransferRecord> loadAllRecords() {
        List<String> lines = FileUtils.readLines(reportFile);
        List<TransferRecord> records = new ArrayList<>();

        for (String line : lines) {
            TransferRecord record = parseLine(line);
            if (record != null) {
                records.add(record);
            }
        }
        return records;
    }

    public List<TransferRecord> loadRecordsByDateRange(LocalDate from, LocalDate to) {
        return loadAllRecords().stream()
                .filter(record -> DateUtils.isWithinRange(record.getTimestamp(), from, to))
                .collect(Collectors.toList());
    }

    private TransferRecord parseLine(String line) {
        try {
            String[] parts = line.split("\\s+\\|\\s+");
            if (parts.length < 6) return null;

            String sourceFile = parts[0];
            TransferStatus status = TransferStatus.valueOf(parts[1]);
            String fromAccount = parts[2];
            String toAccount = parts[3];
            BigDecimal amount = new BigDecimal(parts[4].replace(",", "."));
            LocalDateTime timestamp = DateUtils.parseDateTime(parts[5]);

            if (timestamp == null) {
                System.err.println("Ошибка парсинга даты в строке отчета: " + parts[5]);
                return null;
            }

            TransferRecord record = new TransferRecord(
                    sourceFile, status, fromAccount, toAccount, amount, timestamp
            );

            if (parts.length > 6) {
                record.setErrorMessage(parts[6]);
            }
            return record;

        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка парсинга строки отчета: " + line + " — " + e.getMessage());
            return null;
        }
    }
}
