module com.example.reteasocialagui {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires java.sql;
    exports com.example.reteasocialagui;
    exports com.example.reteasocialagui.Domain;

    opens com.example.reteasocialagui to javafx.fxml;
}