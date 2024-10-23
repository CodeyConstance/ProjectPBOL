package com.home.rpgapp;

import com.home.rpgapp.controller.CharacterController;
import com.home.rpgapp.controller.ItemController;
import com.home.rpgapp.controller.RPGMaintenanceSystemController;
import com.home.rpgapp.controller.RootLayoutController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class RPGMaintenanceSystemMain extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = stage;

        // Load the root layout from FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/home/rpgapp/root-layout.fxml"));
        rootLayout = loader.load();

        // Get the controller and set the main app reference
        RootLayoutController rootController = loader.getController();
        rootController.setMainApp(this);

        // Show the scene containing the root layout
        Scene scene = new Scene(rootLayout);
        primaryStage.setTitle("RPG Maintenance System");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Show the main menu
        showMainMenu();
    }

    public void showMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/home/rpgapp/rpg-maintenance-system-view.fxml"));
            Parent mainMenu = loader.load();

            // Get the controller and set the main app reference
            RPGMaintenanceSystemController mainMenuController = loader.getController();
            mainMenuController.setMainApp(this);

            // Set the main menu into the center of root layout
            rootLayout.setCenter(mainMenu);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to switch to the Item view
    public void switchToItemView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/home/rpgapp/item-view.fxml"));
            Parent itemView = loader.load();

            // Get the controller and set the main app reference
            ItemController itemController = loader.getController();
            itemController.setMainApp(this);

            // Set the item view into the center of root layout
            rootLayout.setCenter(itemView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to switch to the Character view
    public void switchToCharacterView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/home/rpgapp/character-view.fxml"));
            Parent characterView = loader.load();

            // Get the controller and set the main app reference
            CharacterController characterController = loader.getController();
            characterController.setMainApp(this);

            // Set the character view into the center of root layout
            rootLayout.setCenter(characterView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to go back to the main menu
    public void goBackToMainMenu() {
        showMainMenu();
    }

    public static void main(String[] args) {
        launch();
    }
}