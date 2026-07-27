package logic;

import model.Part;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    public static class CartLine {
        private Part part;
        private int quantity;

        public CartLine(Part part, int quantity) {
            this.part = part;
            this.quantity = quantity;
        }

        public Part getPart() { return part; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

    private List<CartLine> lines;

    public Cart() {
        this.lines = new ArrayList<>();
    }

    public List<CartLine> getLines() {
        return lines;
    }

    public boolean addToCart(Part p, int qty) {
        if (p == null) {
            System.out.println("Cannot add to cart: part is null");
            return false;
        }

        if (qty <= 0) {
            System.out.println("Rejected: quantity must be greater than 0");
            return false;
        }

        if (qty > p.getQuantity()) {
            System.out.println("Rejected: quantity exceeds available stock (" + p.getQuantity() + " available)");
            return false;
        }


    }