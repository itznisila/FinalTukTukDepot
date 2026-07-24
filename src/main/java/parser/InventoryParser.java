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
}