package parser;

import model.Part;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
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
            name = "Unnamed Part";
        }

        String dealerName;
        if (fields.length > 2 && !fields[2].isEmpty()) {
            dealerName = fields[2];
        } else {
            dealerName = "Unknown Dealer";
        }

        double price;
        if (fields.length > 3) {
            price = parsePrice(fields[3]);
        } else {
            price = 0.0;
        }

        int quantity;
        if (fields.length > 4) {
            quantity = parseQuantity(fields[4]);
        } else {
            quantity = 0;
        }

        String category;
        if (fields.length > 5) {
            category = normalizeCategory(fields[5]);
        } else {
            category = "Uncategorized";
        }

        LocalDate dateAdded;
        if (fields.length > 6) {
            dateAdded = parseDate(fields[6].replace("§", ","));
        } else {
            dateAdded = LocalDate.now();
        }

        String imagePath;
        if (fields.length > 7) {
            imagePath = fields[7];
        } else {
            imagePath = "";
        }
        return new Part(code, name, dealerName, price, quantity, category, dateAdded, imagePath);
    }

    private double parsePrice(String rawPrice) {
        String cleaned = rawPrice.replace("Rs.", "").replace("Rs", "").trim();
        if (cleaned.isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }



}