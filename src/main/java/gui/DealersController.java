package gui;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Dealer;

import java.util.List;

public class DealersController {

    @FXML
    private TableView<Dealer> dealersTable;
    @FXML private TableColumn<Dealer, String> idColumn;
    @FXML private TableColumn<Dealer, String> nameColumn;
    @FXML private TableColumn<Dealer, String> locationColumn;

    private List<Dealer> allDealers;
    private final DealerSelector selector = new DealerSelector();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("code"));
        nameColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("name"));
        locationColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("location"));

        allDealers = loadAllDealers();
        handleSelectDealers();
    }
}
