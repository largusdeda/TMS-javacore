package moneytransfer.controller;

import moneytransfer.service.ParserService;
import moneytransfer.service.ReportService;
import moneytransfer.util.DateUtils;

import java.time.LocalDate;
import java.util.Scanner;

/**
 * @author Elena Chinarina
 *
 **/

public class TransferController {
    private final ParserService parserService;
    private final ReportService reportService;
    private final Scanner scanner;

    public TransferController(ParserService parserService, ReportService reportService) {
        this.parserService = parserService;
        this.reportService = reportService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== Система денежных переводов ===");

        while (true) {
            System.out.println("\nВыберите действие:");
            System.out.println("1 - Парсинг файлов перевода из input");
            System.out.println("2 - Вывод списка всех переводов");
            System.out.println("3 - Вывод переводов за период");
            System.out.println("0 - Выход");
            System.out.print("Введите номер операции: ");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> handleParseFiles();
                case "2" -> handleShowAllTransfers();
                case "3" -> handleShowTransfersByPeriod();
                case "0" -> {
                    System.out.println("Завершение работы...");
                    return;
                }
                default -> System.out.println("Неверный ввод. Попробуйте снова.");
            }
        }
    }

    private void handleParseFiles() {
        System.out.println("\nЗапуск парсинга файлов...");
        try {
            parserService.parseAndProcessFiles();
            System.out.println("Парсинг завершен.");
        } catch (Exception e) {
            System.err.println("Ошибка при парсинге файлов: " + e.getMessage());
        }
    }

    private void handleShowAllTransfers() {
        System.out.println("\n=== Все переводы ===");
        try {
            var records = reportService.getAllRecords();
            reportService.printRecords(records);
        } catch (Exception e) {
            System.err.println("Ошибка при чтении отчета: " + e.getMessage());
        }
    }

    private void handleShowTransfersByPeriod() {
        try {
            System.out.println("\n=== Переводы за период ===");
            System.out.print("Введите начальную дату (yyyy-MM-dd): ");
            String fromStr = scanner.nextLine().trim();
            LocalDate from = DateUtils.parseDate(fromStr);

            System.out.print("Введите конечную дату (yyyy-MM-dd): ");
            String toStr = scanner.nextLine().trim();
            LocalDate to = DateUtils.parseDate(toStr);

            var records = reportService.getRecordsByDateRange(from, to);
            reportService.printRecords(records);
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}