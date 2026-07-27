package gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import logic.InventoryManager;
import logic.LowStockMonitor;
import logic.SearchFilter;
import model.Part;
import parser.InventoryParser;
import util.AuditLogger;

import java.io.File;
import java.time.LocalDate;
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
        List<Part> loadedParts = new InventoryParser().parseFile("data/inventory_legacy.txt");
        inventoryManager = new InventoryManager(loadedParts);

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

    public void refreshAfterExternalChange() {
        refreshTable();
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

    @FXML
    private void handleChooseImage() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File file = chooser.showOpenDialog(imagePathField.getScene().getWindow());
        if (file != null) {
            imagePathField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void handleAdd() {
        try {
            String code = codeField.getText().trim();
            String name = nameField.getText().trim();
            String brand = brandField.getText().trim();
            String category = formCategoryField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            int qty = Integer.parseInt(quantityField.getText().trim());
            LocalDate date = datePicker.getValue() != null ? datePicker.getValue() : LocalDate.now();
            String imagePath = imagePathField.getText().trim();
            int threshold = thresholdField.getText().trim().isEmpty()
                    ? Part.DEFAULT_LOW_STOCK_THRESHOLD
                    : Integer.parseInt(thresholdField.getText().trim());
            if (code.isEmpty() || name.isEmpty() || category.isEmpty()) {
                statusLabel.setText("Code, name and category are required.");
                return;
            }

            Part newPart = new Part(code, name, brand, price, qty, category, date, imagePath, threshold);
            boolean success = inventoryManager.addPart(newPart);

            if (success) {
                refreshTable();
                statusLabel.setText("Part added successfully.");
                AuditLogger.log("ADD", newPart.getCode(), newPart.getQuantity());
                clearForm();
            } else {
                statusLabel.setText("Add failed — check console (duplicate code, invalid price/quantity, etc).");
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Price, quantity and threshold must be valid numbers.");
        }
    }

    @FXML
    private void handleUpdate() {
        Part selected = inventoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Select a part in the table first.");
            return;
        }
        try {
            String code = selected.getCode();
            String name = nameField.getText().trim();
            String brand = brandField.getText().trim();
            String category = formCategoryField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            int qty = Integer.parseInt(quantityField.getText().trim());
            LocalDate date = datePicker.getValue() != null ? datePicker.getValue() : selected.getDateAdded();
            String imagePath = imagePathField.getText().trim();
            int threshold = thresholdField.getText().trim().isEmpty()
                    ? Part.DEFAULT_LOW_STOCK_THRESHOLD
                    : Integer.parseInt(thresholdField.getText().trim());

            Part updated = new Part(code, name, brand, price, qty, category, date, imagePath, threshold);
            boolean success = inventoryManager.updatePart(code, updated);

            if (success) {
                refreshTable();
                statusLabel.setText("Part updated successfully.");
            } else {
                statusLabel.setText("Update failed — check console (invalid price/quantity or code not found).");
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Price, quantity and threshold must be valid numbers.");
        }
    }

    @FXML
    private void handleDelete() {
        Part selected = inventoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Select a part in the table first.");
            return;
        }
        boolean success = inventoryManager.deletePart(selected.getCode());
        if (success) {
            refreshTable();
            statusLabel.setText("Part deleted.");
            AuditLogger.log("DELETE", selected.getCode(), selected.getQuantity());
        } else {
            statusLabel.setText("Delete failed.");
        }
    }

    private void clearForm() {
        codeField.clear();
        nameField.clear();
        brandField.clear();
        formCategoryField.clear();
        priceField.clear();
        quantityField.clear();
        datePicker.setValue(null);
        imagePathField.clear();
        thresholdField.clear();
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }
}