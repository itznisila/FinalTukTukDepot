package logic;

import model.Part;
import java.util.ArrayList;
import java.util.List;

public class LowStockMonitor {

    public static List<Part> getLowStockItems(List<Part> parts) {
        List<Part> lowStock = new ArrayList<>();

        for (Part p : parts) {
            if (p.getQuantity() < p.getLowStockThreshold()) {
                lowStock.add(p);
            }
        }

        return lowStock;
    }
}
