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

        for (int i = 0; i < fields.length; i++) {
            fields[i] = fields[i].trim();
        }

        String code;
        if (fields.length > 0) {
            code = fields[0];
        } else {
            code = "UNKNOWN";
        }

        String name;
        if (fields.length > 1) {
            name = fields[1];
        } else {
            name = "Unnamed Dealer";
        }

        String phone;
        if (fields.length > 2 && !fields[2].isEmpty()) {
            phone = fields[2];
        } else {
            phone = "No Phone";
        }

        String location;
        if (fields.length > 3) {
            location = fields[3];
        } else {
            location = "Unknown Location";
        }
        return new Dealer(code, name, phone, location);
    }

    private String detectDelimiter(String line) {
        if (line.contains("|")) {
            return "|";
        } else if (line.contains(";")) {
            return ";";
        } else {
            return ",";
        }
    }
}
