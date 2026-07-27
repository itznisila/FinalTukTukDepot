package gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import logic.LowStockMonitor;
import logic.SearchFilter;
import model.Part;

import java.io.File;
import java.util.List;

public class InventoryController {

    @FXML private TextField searchCategoryField;
    @FXML private TextField minPriceField;
    @FXML private TextField maxPriceField;
    @FXML private TextField keywordField;

    @FXML private TableView<Part> inventoryTable;
    @FXML private TableColumn<Part, String> codeColumn;
    @FXML private TableColumn<Part, String> nameColumn;
    @FXML private TableColumn<Part, Double> priceColumn;
    @FXML private TableColumn<Part, Integer> quantityColumn;
    @FXML private TableColumn<Part, String> categoryColumn;
    @FXML private TableColumn<Part, Integer> thresholdColumn;
    @FXML private TableColumn<Part, String> imageColumn;

    @FXML private Label totalQuantityLabel;
    @FXML private Label totalPriceLabel;
    @FXML private Label lowStockLabel;

    @FXML private TextField codeField;
    @FXML private TextField nameField;
    @FXML private TextField brandField;
    @FXML private TextField formCategoryField;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;
    @FXML private DatePicker datePicker;
    @FXML private TextField imagePathField;
    @FXML private TextField thresholdField;
    @FXML private Label statusLabel;

    private InventoryManager inventoryManager;

    @FXML
    public void initialize() {
        inventoryManager = new InventoryManager();

        codeColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("code"));
        nameColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("quantity"));
        categoryColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("category"));
        thresholdColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("lowStockThreshold"));
        imageColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("imagePath"));


        imageColumn.setCellFactory(col -> new TableCell<Part, String>() {
            private final ImageView imageView = new ImageView();
            @Override
            protected void updateItem(String path, boolean empty) {
                super.updateItem(path, empty);
                if (empty || path == null || path.isEmpty()) {
                    setGraphic(null);
                } else {
                    File file = new File(path);
                    if (file.exists()) {
                        imageView.setImage(new Image(file.toURI().toString(), 40, 40, true, true));
                        setGraphic(imageView);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });

        refreshTable();
    }

    private void refreshTable() {
        List<Part> parts = inventoryManager.getParts();
        inventoryManager.sortByCategoryThenCode(parts);
        inventoryTable.setItems(FXCollections.observableArrayList(parts));
        updateSummary(parts);
    }

    private void updateSummary(List<Part> parts) {
        totalQuantityLabel.setText("Total Quantity: " + inventoryManager.getTotalItemCount());
        totalPriceLabel.setText("Total Price: " + inventoryManager.getTotalInventoryValue());

        List<Part> lowStock = LowStockMonitor.getLowStockItems(parts);
        StringBuilder sb = new StringBuilder();
        for (Part p : lowStock) {
            sb.append(p.getCode()).append("(").append(p.getName())
                    .append(",Qty: ").append(p.getQuantity()).append(")\n");
        }
        lowStockLabel.setText(sb.toString());
    }

    @FXML
    private void handleSearch() {
        String category = searchCategoryField.getText().trim();
        String keyword = keywordField.getText().trim();
        Double minPrice = null, maxPrice = null;
        try {
            if (!minPriceField.getText().trim().isEmpty())
                minPrice = Double.parseDouble(minPriceField.getText().trim());
            if (!maxPriceField.getText().trim().isEmpty())
                maxPrice = Double.parseDouble(maxPriceField.getText().trim());
        } catch (NumberFormatException e) {
            statusLabel.setText("Min/Max price must be valid numbers.");
            return;
        }
        List<Part> results = SearchFilter.search(inventoryManager.getParts(), category, minPrice, maxPrice, keyword);
        inventoryTable.setItems(FXCollections.observableArrayList(results));
        updateSummary(results);
    }

    @FXML
    private void handleClearSearch() {
        searchCategoryField.clear();
        minPriceField.clear();
        maxPriceField.clear();
        keywordField.clear();
        refreshTable();
    }
