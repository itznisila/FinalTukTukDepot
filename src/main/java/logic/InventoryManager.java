package logic;

import model.Part;

import java.util.ArrayList;
import java.util.List;

public class InventoryManager {

    private List<Part> parts;
    public InventoryManager() {
        this.parts = new ArrayList<Part>();
    }

    public InventoryManager(List<Part> parts) {
        this.parts = parts;
    }

    public List<Part> getParts() {
        return parts;
    }

    public boolean addPart(Part p) {
        if (p==null) {
            System.out.println("Cannot add null part");
            return false;
        }

        if (p.getPrice() <= 0) {
            System.out.println("Rejected: price must be greater than 0");
            return false;
        }

        if (p.getQuantity() < 0) {
            System.out.println("Rejected: quantity cannot be negative");
            return false;
        }
    }
}
