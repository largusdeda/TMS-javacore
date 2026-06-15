package moneytransfer.service;

import moneytransfer.dto.ParsedTransferData;
import moneytransfer.exception.FileProcessingException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Elena Chinarina
 *
 **/

public class ParserService {
    private final String inputDir;
    private final String archiveDir;
    private final TransferService transferService;

    public ParserService(String inputDir, String archiveDir, TransferService transferService) {
        this.inputDir = inputDir;
        this.archiveDir = archiveDir;
        this.transferService = transferService;
    }

    public void parseAndProcessFiles() {
        File inputDirectory = new File(inputDir);

        if (!inputDirectory.exists() || !inputDirectory.isDirectory()) {
            System.out.println("Директория input не существует. Создано: " + inputDir);
            inputDirectory.mkdirs();
            return;
        }

        File[] files = inputDirectory.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("Нет файлов для обработки в директории: " + inputDir);
            return;
        }

        List<File> txtFiles = Arrays.stream(files)
                .filter(file -> file.isFile() && file.getName().toLowerCase().endsWith(".txt"))
                .toList();

        if (txtFiles.isEmpty()) {
            System.out.println("Нет подходящих txt файлов в директории: " + inputDir);
            return;
        }

        System.out.println("Найдено " + txtFiles.size() + " файлов для обработки");

        for (File file : txtFiles) {
            processFile(file);
        }
    }

    private void processFile(File file) {
        System.out.println("Обработка файла: " + file.getName());

        try {
            ParsedTransferData data = parseFile(file);
            transferService.executeTransfer(data);
            moveToArchive(file);
            System.out.println("Файл " + file.getName() + " успешно обработан");
        } catch (Exception e) {
            System.err.println("Ошибка обработки файла " + file.getName() + ": " +
                    e.getMessage());
            // Создаем запись об ошибке, даже если не удалось распарсить
            ParsedTransferData errorData = new ParsedTransferData(
                    "unknown", "unknown", BigDecimal.ZERO, file.getName());
            transferService.executeTransfer(errorData);
            moveToArchive(file);
        }
    }

    private ParsedTransferData parseFile(File file) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    lines.add(line);
                }
            }
        }

        if (lines.isEmpty()) {
            throw new IOException("Файл пуст");
        }

        return parseLine(lines.get(0), file.getName());
    }

    private ParsedTransferData parseLine(String line, String fileName) {
        String[] parts = line.split("\\s+");

        if (parts.length < 3) {
            throw new FileProcessingException(
                    "Недостаточно данных в файле. Ожидается минимум 3 поля: " +
                            "счет_отправителя счет_получателя сумма");
        }

        String fromAccount = parts[0].trim();
        String toAccount = parts[1].trim();
        BigDecimal amount;

        try {
            amount = new BigDecimal(parts[2].trim().replace(",", "."));
        } catch (NumberFormatException e) {
            throw new FileProcessingException(
                    "Неверный формат суммы: " + parts[2]);
        }

        return new ParsedTransferData(fromAccount, toAccount, amount, fileName);
    }

    private void moveToArchive(File file) {
        File archiveDirectory = new File(archiveDir);
        if (!archiveDirectory.exists()) {
            archiveDirectory.mkdirs();
        }

        File destFile = new File(archiveDir, file.getName());
        if (destFile.exists()) {
            // Добавление временной метки к имени файла
            String name = file.getName();
            String baseName = name.substring(0, name.lastIndexOf('.'));
            String extension = name.substring(name.lastIndexOf('.'));
            destFile = new File(archiveDir,
                    baseName + "_" + System.currentTimeMillis() + extension);
        }

        if (file.renameTo(destFile)) {
            System.out.println("Файл перемещен в архив: " + destFile.getPath());
        } else {
            System.err.println("Не удалось переместить файл в архив: " + file.getName());
        }
    }
}

