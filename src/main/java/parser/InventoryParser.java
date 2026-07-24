package parser;

import model.Part;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class InventoryParser {

    public List<Part> parseFile(String filePath) {
        List<Part> parts = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Part part = parseLine(line);
                parts.add(part);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return parts;
    }

    private Part parseLine(String line) {
        String protectedLine = line.replaceAll("(?<=[A-Za-z]{3}\\s\\d{1,2}),(?=\\s*\\d{4})", "§");
        String[] fields = protectedLine.split("[,|;]", -1);

        for (int i = 0; i < fields.length; i++) {
            fields[i] = fields[i].trim();
        }
    }



}