package parser;
import model.Dealer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class DealerParser {
    public List<Dealer> parseFile(String filePath) {
        List<Dealer> dealers = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Dealer dealer = parseLine(line);
                dealers.add(dealer);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return dealers;
    }

    private Dealer parseLine(String line) {
        String delimiter = detectDelimiter(line);
        String[] fields = line.split(java.util.regex.Pattern.quote(delimiter));
}
