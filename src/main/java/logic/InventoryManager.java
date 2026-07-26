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

    }
}
