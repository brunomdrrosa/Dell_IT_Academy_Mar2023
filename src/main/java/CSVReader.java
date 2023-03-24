import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVReader {

    public static List<List<String>> readAll() {
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/DNIT-Distancias.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                records.add(Arrays.asList(values));
            }
            return records;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> readCapitais() {
        List<String> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/DNIT-Distancias.csv"))) {
            String line;
            if ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                records.add(Arrays.toString(values));
            }
            return records;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}