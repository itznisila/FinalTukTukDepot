package model;

import java.time.LocalDate;

public class Part {

    private String code;
    private String name;
    private String dealerName;
    private double price;
    private int quantity;
    private String category;
    private LocalDate dateAdded;
    private String imagePath;

    public Part(String code, String name, String dealerName, double price,
                int quantity, String category, LocalDate dateAdded, String imagePath) {
        this.code = code;
        this.name = name;
        this.dealerName = dealerName;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.dateAdded = dateAdded;
        this.imagePath = imagePath;
    }
}
