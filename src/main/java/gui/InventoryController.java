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