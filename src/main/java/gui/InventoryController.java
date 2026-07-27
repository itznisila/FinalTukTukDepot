package gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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