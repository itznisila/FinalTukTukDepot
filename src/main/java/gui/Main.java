package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader inventoryLoader = new FXMLLoader(getClass().getResource("/fxml/Inventory.fxml"));
        Parent inventoryRoot = inventoryLoader.load();
        InventoryController inventoryController = inventoryLoader.getController();

        FXMLLoader dealersLoader = new FXMLLoader(getClass().getResource("/fxml/Dealers.fxml"));
        Parent dealersRoot = dealersLoader.load();

        FXMLLoader cartLoader = new FXMLLoader(getClass().getResource("/fxml/Cart.fxml"));
        Parent cartRoot = cartLoader.load();
        CartController cartController = cartLoader.getController();

        cartController.setInventoryManager(inventoryController.getInventoryManager());
        cartController.setInventoryController(inventoryController);

        Tab inventoryTab = new Tab("Inventory", inventoryRoot);
        Tab dealersTab = new Tab("Dealers", dealersRoot);
        Tab cartTab = new Tab("Cart", cartRoot);
        inventoryTab.setClosable(false);
        dealersTab.setClosable(false);
        cartTab.setClosable(false);

        TabPane tabPane = new TabPane(inventoryTab, dealersTab, cartTab);

        Scene scene = new Scene(tabPane, 900, 650);
        primaryStage.setTitle("Malabe Tuk Tuk Spares Depot");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
