package com.home.rpgapp.controller;

import com.home.rpgapp.RPGMaintenanceSystemMain;
import com.home.rpgapp.database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class RPGMaintenanceSystemController {
    private DatabaseHandler dbHandler;
    private RPGMaintenanceSystemMain mainApp;

    public void setMainApp(RPGMaintenanceSystemMain mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void initialize() {
        dbHandler = new DatabaseHandler();
        dbHandler.getConnection();
    }


    @FXML
    public void onManageItemsClick(ActionEvent event) {
        if (mainApp != null) {
            mainApp.switchToItemView();
        } else {
            System.out.println("MainApp is not set");
        }
    }

    @FXML
    public void onManageCharactersClick(ActionEvent event) {
        if (mainApp != null) {
            mainApp.switchToCharacterView();
        } else {
            System.out.println("MainApp is not set");
        }
    }

    @FXML
    public void goBackToMainMenu(ActionEvent event) {
        if (mainApp != null) {
            mainApp.goBackToMainMenu();
        }
    }

}