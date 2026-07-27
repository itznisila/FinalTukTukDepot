package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Part;

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