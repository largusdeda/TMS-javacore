package moneytransfer.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Elena Chinarina
 *
 **/

public class FileUtils {

    public static List<String> readLines(String filePath) {
        List<String> lines = new ArrayList<>();
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            return lines;
        }
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line.trim());
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Ошибка чтения файла: " + filePath, e);
        }
        return lines;
    }

    public static void writeLines(String filePath, List<String> lines) {
        Path path = Paths.get(filePath);
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, lines);
        } catch (IOException e) {
            throw new UncheckedIOException("Ошибка записи в файл: " + filePath, e);
        }
    }

    public static void appendLine(String filePath, String line) {
        Path path = Paths.get(filePath);
        try {
            Files.createDirectories(path.getParent());
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            Files.write(path, (line + System.lineSeparator()).getBytes(),
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new UncheckedIOException("Ошибка добавления в файл: " + filePath, e);
        }
    }

    public static void moveFile(File source, String targetDir) {
        Path targetPath = Paths.get(targetDir, source.getName());
        try {
            Files.createDirectories(Paths.get(targetDir));
            Files.move(source.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException(
                    "Ошибка перемещения файла: " + source.getName(), e);
        }
    }
}
