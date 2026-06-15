package moneytransfer;

import moneytransfer.controller.TransferController;
import moneytransfer.repository.AccountRepository;
import moneytransfer.repository.ReportRepository;
import moneytransfer.service.ParserService;
import moneytransfer.service.ReportService;
import moneytransfer.service.TransferService;

/**
 * @author Elena Chinarina
 *
 **/

public class MoneyTransferApp {
    public static void main(String[] args) {
        // Пути к файлам и директориям
        String accountsFile = "src/moneytransfer/resources/accounts.txt";
        String reportFile = "src/moneytransfer/report/report.txt";
        String inputDir = "src/moneytransfer/resources/input";
        String archiveDir = "src/moneytransfer/resources/archive";

        // Инициализация репозиториев
        AccountRepository accountRepository = new AccountRepository(accountsFile);
        ReportRepository reportRepository = new ReportRepository(reportFile);

        // Инициализация сервисов
        TransferService transferService = new TransferService(accountRepository, reportRepository);
        ParserService parserService = new ParserService(inputDir, archiveDir, transferService);
        ReportService reportService = new ReportService(reportRepository);

        // Запуск контроллера
        TransferController controller = new TransferController(parserService, reportService);
        controller.start();
    }
}