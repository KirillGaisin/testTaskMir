package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Random;
import java.util.stream.Stream;

public class FileUtils {

    private final Path p = Path.of("src/main/resources/testFile.txt");

    /**
     * Генерирует файл и записывает в него значения в зависимости от входных параметров.
     * Строки записываются в alphanumeric формате, спецсимволы выфильтровываются, каждая строка записывается с новой строки.
     *
     * @param lineCount  - количество строк, которые будут написаны в сгенерированный файл
     * @param lineLength - количество символов в одной строке
     */
    public void generateFileAlphanumeric(int lineCount, int lineLength) {
        Random random = new Random();
        try {
            Files.deleteIfExists(p);
            Files.createFile(p);
            for (int i = 0; i < lineCount; i++) {
                Files.writeString(p,
                        random.ints(48, 122 + 1)
                                .filter(n -> (n <= 57 || n >= 65) && (n <= 90 || n >= 97))
                                .limit(lineLength)
                                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).append("\n")
                                .toString(), StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод, сортирующий файл и перезаписывающий его на основе отсортированных значений
     */
    public void sortFile() {
        try (Stream<String> fileToSort = Files.lines(p)) {
            Files.writeString(p, fileToSort
                    .sorted()
                    .collect(StringBuilder::new, (a, b) -> a.append(b).append("\n"), StringBuilder::append)
                    .toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
