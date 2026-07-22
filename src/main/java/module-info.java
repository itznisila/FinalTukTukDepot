module org.example.finaltuktukdepot {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.finaltuktukdepot to javafx.fxml;
    exports org.example.finaltuktukdepot;
}