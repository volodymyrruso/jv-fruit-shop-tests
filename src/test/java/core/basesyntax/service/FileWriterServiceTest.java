package core.basesyntax.service;

import core.basesyntax.service.impl.FileWriterServiceImpl;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FileWriterServiceTest {
    private static final String FILE_PATH = "src/test/java/resources/report.csv";
    private static List<String> expected;
    private static FileWriterService fileWriterService;

    @BeforeClass
    public static void beforeAll() {
        fileWriterService = new FileWriterServiceImpl();
        expected = List.of("fruit,quantity", "banana,152", "apple,90");
    }

    @Test
    public void writeToFile_ok() {
        fileWriterService.writeToFile(FILE_PATH, expected);
        File file = new File(FILE_PATH);
        List<String> actualData = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                actualData.add(line);
                line = bufferedReader.readLine();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found. " + e);
        } catch (IOException e) {
            throw new RuntimeException("Can't read file. " + e);
        }
        assertEquals(expected, actualData);
    }

    @Test(expected = RuntimeException.class)
    public void filePathNull_notOK() {
        fileWriterService.writeToFile(null, expected);
    }

    @Test(expected = RuntimeException.class)
    public void dataInvalid_notOk() {
        fileWriterService.writeToFile(FILE_PATH, null);
    }
}
