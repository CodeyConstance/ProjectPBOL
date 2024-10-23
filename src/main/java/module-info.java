module com.home.rpgapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;

    opens com.home.rpgapp to javafx.fxml;
    exports com.home.rpgapp;

    exports com.home.rpgapp.controller;
    opens com.home.rpgapp.controller to javafx.fxml;

    exports com.home.rpgapp.model;
    opens com.home.rpgapp.model to javafx.fxml;

    exports com.home.rpgapp.database;
    opens com.home.rpgapp.database to javafx.fxml;
}