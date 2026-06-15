package moneytransfer.service;

import moneytransfer.model.TransferRecord;
import moneytransfer.repository.ReportRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Elena Chinarina
 *
 **/

public class ReportService {
    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public List<TransferRecord> getAllRecords() {
        return reportRepository.loadAllRecords();
    }

    public List<TransferRecord> getRecordsByDateRange(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Дата 'с' должна быть раньше или равна дате 'по'");
        }
        return reportRepository.loadRecordsByDateRange(from, to);
    }

    public void printRecords(List<TransferRecord> records) {
        if (records.isEmpty()) {
            System.out.println("Записи не найдены");
            return;
        }

        System.out.println("-".repeat(150));
        System.out.printf("%-20s %-30s %-20s %-20s %-20s %-20s%n",
                "Файл", "Статус", "Отправитель", "Получатель", "Сумма", "Дата/Время");
        System.out.println("-".repeat(150));

        for (TransferRecord record : records) {
            System.out.printf("%-20s %-30s %-20s %-20s %-20.2f %-20s%n",
                    truncate(record.getSourceFile(), 18),
                    record.getStatus().getDescription(),
                    record.getFromAccount(),
                    record.getToAccount(),
                    record.getAmount(),
                    record.getTimestamp().toString()
            );
        }
        System.out.println("-".repeat(150));
        System.out.println("Всего записей: " + records.size());
    }

    private String truncate(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }
}