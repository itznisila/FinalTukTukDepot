package gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import logic.Dealerselector;
import model.Dealer;
import parser.DealerParser;

import java.util.List;

public class DealersController {

    @FXML
    private TableView<Dealer> dealersTable;
    @FXML private TableColumn<Dealer, String> idColumn;
    @FXML private TableColumn<Dealer, String> nameColumn;
    @FXML private TableColumn<Dealer, String> locationColumn;

    private List<Dealer> allDealers;
    private final Dealerselector selector = new Dealerselector();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("code"));
        nameColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("name"));
        locationColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("location"));

        allDealers = loadAllDealers();
        handleSelectDealers();
    }

    @FXML
    private void handleSelectDealers() {
        List<Dealer> chosen = selector.selectRandomDealers(allDealers, 4);
        selector.sortByLocation(chosen);
        dealersTable.setItems(FXCollections.observableArrayList(chosen));
    }

    private List<Dealer> loadAllDealers() {
        DealerParser parser = new DealerParser();
        return parser.parseFile("data/dealers_legacy.txt");
    }
}

