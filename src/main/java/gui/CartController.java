package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CartController {

    @FXML private TextField codeField;
    @FXML private TextField quantityField;

    @FXML private TableView<CartLine> cartTable;
    @FXML private TableColumn<CartLine, String> codeColumn;
    @FXML private TableColumn<CartLine, String> nameColumn;
    @FXML private TableColumn<CartLine, Integer> quantityColumn;
    @FXML private TableColumn<CartLine, Double> subTotalColumn;

    @FXML private Label feedbackLabel;

    private final ObservableList<CartLine> cartLines = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        codeColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("code"));
        nameColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("quantity"));
        subTotalColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("subTotal"));

        cartTable.setItems(cartLines);
    }

    @FXML
    private void handleAddToCart() {
        String code = codeField.getText().trim();
        String qtyText = quantityField.getText().trim();

        if (code.isEmpty() || qtyText.isEmpty()) {
            feedbackLabel.setText("Enter both code and quantity.");
            return;
        }

        try {
            int qty = Integer.parseInt(qtyText);
            if (qty <= 0) {
                feedbackLabel.setText("Quantity must be greater than 0.");
                return;
            }

            cartLines.add(new CartLine(code, "TBD (Day 8)", qty, 0.0));
            feedbackLabel.setText("Added to cart (placeholder — real logic comes Day 8).");
            codeField.clear();
            quantityField.clear();

        } catch (NumberFormatException e) {
            feedbackLabel.setText("Quantity must be a valid number.");
        }
    }

    @FXML
    private void handleEmptyCart() {
        cartLines.clear();
        feedbackLabel.setText("Cart emptied.");
    }

    @FXML
    private void handleCheckout() {
        if (cartLines.isEmpty()) {
            feedbackLabel.setText("Cart is empty — nothing to checkout.");
            return;
        }
        feedbackLabel.setText("Checkout clicked (discount/total logic comes Day 8).");
    }

    public static class CartLine {
        private final String code;
        private final String name;
        private final int quantity;
        private final double subTotal;

        public CartLine(String code, String name, int quantity, double subTotal) {
            this.code = code;
            this.name = name;
            this.quantity = quantity;
            this.subTotal = subTotal;
        }

        public String getCode() { return code; }
        public String getName() { return name; }
        public int getQuantity() { return quantity; }
        public double getSubTotal() { return subTotal; }
    }
}
