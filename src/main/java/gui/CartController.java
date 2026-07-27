package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Part;
import logic.Cart;
import logic.Cart.CartLine;
import logic.CheckoutCalculator;
import logic.CheckoutCalculator.CheckoutResult;
import logic.InventoryManager;
import util.AuditLogger;
import java.util.List;

public class CartController {

    @FXML private TextField codeField;
    @FXML private TextField quantityField;

    @FXML private TableView<CartLine> cartTable;
    @FXML private TableColumn<CartLine, String> codeColumn;
    @FXML private TableColumn<CartLine, String> nameColumn;
    @FXML private TableColumn<CartLine, Integer> quantityColumn;
    @FXML private TableColumn<CartLine, Double> subTotalColumn;

    @FXML private Label feedbackLabel;

    private final Cart cart = new Cart();
    private InventoryManager inventoryManager;
    private InventoryController inventoryController;

    @FXML
    public void initialize() {
        codeColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getPart().getCode()));
        nameColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getPart().getName()));
        quantityColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getQuantity()).asObject());
        subTotalColumn.setCellValueFactory(data -> {
            double subtotal = CheckoutCalculator.calculateLineSubtotal(
                    data.getValue().getPart(), data.getValue().getQuantity());
            return new javafx.beans.property.SimpleDoubleProperty(subtotal).asObject();
        });

        refreshCartTable();
    }

    public void setInventoryManager(InventoryManager manager) {
        this.inventoryManager = manager;
    }

    public void setInventoryController(InventoryController controller) {
        this.inventoryController = controller;
    }

    private void refreshCartTable() {
        cartTable.setItems(FXCollections.observableArrayList(cart.getLines()));
    }

    @FXML
    private void handleAddToCart() {
        String code = codeField.getText().trim();
        String qtyText = quantityField.getText().trim();

        if (code.isEmpty() || qtyText.isEmpty()) {
            feedbackLabel.setText("Enter both code and quantity.");
            return;
        }

        int qty;
        try {
            qty = Integer.parseInt(qtyText);
        } catch (NumberFormatException e) {
            feedbackLabel.setText("Quantity must be a valid number.");
            return;
        }

        Part part = findPartByCode(code);
        if (part == null) {
            feedbackLabel.setText("No part found with code: " + code);
            return;
        }

        boolean success = cart.addToCart(part, qty);
        if (success) {
            refreshCartTable();
            feedbackLabel.setText("Added to cart.");
            codeField.clear();
            quantityField.clear();
        } else {
            feedbackLabel.setText("Could not add — check quantity or available stock.");
        }
    }

    @FXML
    private void handleEmptyCart() {
        cart.clear();
        refreshCartTable();
        feedbackLabel.setText("Cart emptied.");
    }

    @FXML
    private void handleCheckout() {
        if (cart.isEmpty()) {
            feedbackLabel.setText("Cart is empty — nothing to checkout.");
            return;
        }

        CheckoutResult result = CheckoutCalculator.calculate(cart);

        for (CartLine line : cart.getLines()) {
            Part p = line.getPart();
            p.setQuantity(p.getQuantity() - line.getQuantity());
            AuditLogger.log("CHECKOUT", p.getCode(), line.getQuantity());
        }

        String message = String.format(
                "Checkout complete. Subtotal: %.2f | After item discounts: %.2f | Final total: %.2f%s",
                result.getSubtotalBeforeDiscounts(),
                result.getTotalAfterLineDiscounts(),
                result.getFinalTotal(),
                result.isComboDiscountApplied() ? " (10% combo discount applied)" : ""
        );
        feedbackLabel.setText(message);

        cart.clear();
        refreshCartTable();

        if (inventoryController != null) {
            inventoryController.refreshAfterExternalChange();
        }
    }

    private Part findPartByCode(String code) {
        if (inventoryManager == null) return null;
        for (Part p : inventoryManager.getParts()) {
            if (p.getCode().equalsIgnoreCase(code)) {
                return p;
            }
        }
        return null;
    }
}
