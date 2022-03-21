

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilityTest {

    private void assertFileContent(String inputName, String expectedContent) throws IOException {
        StringBuilder res = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputName))) {
            String str = reader.readLine();
            while (str != null) {
                res.append(str).append("\n");
                str = reader.readLine();
            }
        }
        assertEquals(expectedContent, res.toString().trim());
    }

    @Test
    void test() throws IOException {
        Utility.main("-n 1 -o out.txt input/in1.txt".trim().split("\s+"));
        assertFileContent("out.txt", "aefweg");
        Utility.main("-c 1 -o out.txt input/in1.txt".trim().split("\s+"));
        assertFileContent("out.txt", "g");
        new File("out.txt").delete();
    }
}
