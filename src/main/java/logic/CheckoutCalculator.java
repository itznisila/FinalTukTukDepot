package logic;

import model.Part;
import logic.Cart.CartLine;
import java.util.List;

public class CheckoutCalculator {

    public static final double LINE_DISCOUNT_THRESHOLD_QTY = 3;
    public static final double LINE_DISCOUNT_RATE = 0.05;      // 5%
    public static final double COMBO_DISCOUNT_RATE = 0.10;     // 10%

    public static class CheckoutResult {
        private double subtotalBeforeDiscounts;
        private double totalAfterLineDiscounts;
        private double finalTotal;
        private boolean comboDiscountApplied;

        public CheckoutResult(double subtotalBeforeDiscounts, double totalAfterLineDiscounts,
                              double finalTotal, boolean comboDiscountApplied) {
            this.subtotalBeforeDiscounts = subtotalBeforeDiscounts;
            this.totalAfterLineDiscounts = totalAfterLineDiscounts;
            this.finalTotal = finalTotal;
            this.comboDiscountApplied = comboDiscountApplied;
        }

        public double getSubtotalBeforeDiscounts() { return subtotalBeforeDiscounts; }
        public double getTotalAfterLineDiscounts() { return totalAfterLineDiscounts; }
        public double getFinalTotal() { return finalTotal; }
        public boolean isComboDiscountApplied() { return comboDiscountApplied; }
    }

    public static CheckoutResult calculate(Cart cart) {
        List<CartLine> lines = cart.getLines();

        double rawSubtotal = 0.0;
        double totalAfterLineDiscounts = 0.0;
        boolean hasEngine = false;
        boolean hasElectrical = false;

        for (CartLine line : lines) {
            Part p = line.getPart();
            int qty = line.getQuantity();
            double lineSubtotal = p.getPrice() * qty;

            rawSubtotal += lineSubtotal;

            if (qty >= LINE_DISCOUNT_THRESHOLD_QTY) {
                lineSubtotal = lineSubtotal - (lineSubtotal * LINE_DISCOUNT_RATE);
            }
            totalAfterLineDiscounts += lineSubtotal;

            if (p.getCategory().equalsIgnoreCase("Engine")) {
                hasEngine = true;
            }
            if (p.getCategory().equalsIgnoreCase("Electrical")) {
                hasElectrical = true;
            }
        }

        double finalTotal = totalAfterLineDiscounts;
        boolean comboApplied = false;

        if (hasEngine && hasElectrical) {
            finalTotal = finalTotal - (finalTotal * COMBO_DISCOUNT_RATE);
            comboApplied = true;
        }

        return new CheckoutResult(rawSubtotal, totalAfterLineDiscounts, finalTotal, comboApplied);
    }

    public static double calculateLineSubtotal(Part p, int qty) {
        double subtotal = p.getPrice() * qty;
        if (qty >= LINE_DISCOUNT_THRESHOLD_QTY) {
            subtotal = subtotal - (subtotal * LINE_DISCOUNT_RATE);
        }
        return subtotal;
    }
}
