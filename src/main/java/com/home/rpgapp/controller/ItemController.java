package com.home.rpgapp.controller;

import com.home.rpgapp.RPGMaintenanceSystemMain;
import com.home.rpgapp.database.DatabaseHandler;
import com.home.rpgapp.model.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class ItemController {

    // Buttons and forms
    @FXML
    private GridPane itemButtons; // The main menu buttons
    @FXML
    private Button submitItemButton; // Button used for submitting new or edited items
    @FXML
    private Button backButton;  // Single back button used for all views
    @FXML
    public ListView<String> itemListView; // ListView to display items
    private boolean isEditMode = false; // Track whether we're editing or adding a new item
    private Item selectedItem; // The currently selected item to be edited
    private ObservableList<Item> itemList;
    @FXML
    private VBox addEditItemForm; // The form for adding/editing an item

    // Text fields for adding new item
    @FXML
    private TextField itemNameField;
    @FXML
    private TextField itemTypeField;
    @FXML
    private TextField itemQuantityField;
    @FXML
    private TextField itemEffectField;

    // States for toggling visibility
    private boolean isListViewVisible = false;
    private boolean isAddFormVisible = false;
    private RPGMaintenanceSystemMain mainApp;
    public void setMainApp(RPGMaintenanceSystemMain mainApp) {
        this.mainApp = mainApp;
    }
    private DatabaseHandler dbHandler;

    @FXML
    public void initialize() {
        dbHandler = new DatabaseHandler();
        dbHandler.getConnection();
        itemListView.setVisible(false);
        itemListView.setManaged(false); // Ensure it's not shown by default
        itemButtons.setVisible(true);
        itemButtons.setManaged(true); // Ensure buttons are shown by default
        backButton.setVisible(false);
        backButton.setManaged(true);

        // Initialize the hidden itemList
        itemList = FXCollections.observableArrayList();

        /// Users could choose single items to be selected
        itemListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Add a listener to handle item selection
        itemListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int selectedIndex = itemListView.getSelectionModel().getSelectedIndex();

                // Check if the list is populated and the index is valid
                if (!itemList.isEmpty() && selectedIndex >= 0 && selectedIndex < itemList.size()) {
                    // Get the actual Item object from the hidden itemList
                    selectedItem = itemList.get(selectedIndex);
                    System.out.println("Selected item: " + selectedItem.getName());
                } else {
                    System.out.println("Invalid selection or empty list.");
                }
            }
        });
    }


    @FXML
    public void toggleViewItemList(ActionEvent event) {
        isListViewVisible = !isListViewVisible;

        if (isListViewVisible) {
            // Show ListView, hide main buttons, show Back button in a new position
            itemButtons.setVisible(false);
            itemButtons.setManaged(false);
            itemListView.setVisible(true);
            itemListView.setManaged(true);

            backButton.setLayoutX(500);
            backButton.setLayoutY(440);
            backButton.setVisible(true);
            backButton.setManaged(true);

            // Call the method to load items into the ListView
            loadItemsIntoListView();

            // Re-enable item selection after loading the items
            enableItemSelection();
        } else {
            itemListView.setVisible(false);
            itemListView.setManaged(false);
            itemButtons.setVisible(true);
            itemButtons.setManaged(true);
            backButton.setVisible(false);
            backButton.setManaged(false);
        }
    }

    // Method that loads items into the ListView and returns the ObservableList<Item>
    public ObservableList<Item> loadItemsIntoListView() {
        // Clear the current ListView and hidden item list
        itemListView.getItems().clear();
        itemList.clear();

        // Load the items from the database
        List<Item> itemsFromDB = dbHandler.loadItemList();

        System.out.println("Total items: " + itemsFromDB.size());

        // Store the items in the hidden item list
        itemList.addAll(itemsFromDB);

        // Fill the ListView with items name and quantity fetched from the database
        for (Item item : itemList) {
            itemListView.getItems().add(item.getName() + " - Quantity: " + item.getQuantity());
        }

        System.out.println("Updated total items: " + itemListView.getItems().size());

        return itemList;
    }


    @FXML
    public void toggleAddNewItem(ActionEvent event) {
        isEditMode = false;

        isAddFormVisible = !isAddFormVisible;

        if (isAddFormVisible) {
            // Hide main buttons, show form, move Back button to new position
            itemButtons.setVisible(false);
            itemButtons.setManaged(false);
            addEditItemForm.setVisible(true);
            addEditItemForm.setManaged(true);

            // Reset form fields to empty values
            clearFormFields();

            // Update the submit button text to set title to "Add Item"
            submitItemButton.setText("Add Item");

            // Move and show the back button for the form
            backButton.setLayoutX(500);
            backButton.setLayoutY(440);
            backButton.setVisible(true);
            backButton.setManaged(true);
        } else {
            // If returning to the main menu, hide the form and back button
            addEditItemForm.setVisible(false);
            addEditItemForm.setManaged(false);
            itemButtons.setVisible(true);
            itemButtons.setManaged(true);
            backButton.setVisible(false);
            backButton.setManaged(false);
        }
    }


    @FXML
    public void toggleEditItem(ActionEvent event) {
        if (selectedItem != null) {
            // Set the edit mode to true
            isEditMode = true;

            // Show the form for editing with pre-filled fields
            toggleAddEditItem(event);
        } else {
            // Show alert if no item is selected
            showAlert("Please select an item to edit.");
        }
    }


    @FXML
    public void toggleAddEditItem(ActionEvent event) {
        isAddFormVisible = !isAddFormVisible;

        if (isAddFormVisible) {
            // Hide main buttons, show form, move Back button to new position
            itemButtons.setVisible(false);
            itemButtons.setManaged(false);
            addEditItemForm.setVisible(true);
            addEditItemForm.setManaged(true);

            // Position the back button for the edit form
            backButton.setLayoutX(500);
            backButton.setLayoutY(440);
            backButton.setVisible(true);
            backButton.setManaged(true);

            if (isEditMode && selectedItem != null) {
                // Editing an existing item, update form and button text
                submitItemButton.setText("Update Item");
                populateFormFields(selectedItem);
            } else {
                submitItemButton.setText("Add Item");
                clearFormFields();
            }
        } else {
            // If returning to the main menu, hide the form and back button
            addEditItemForm.setVisible(false);
            addEditItemForm.setManaged(false);
            itemButtons.setVisible(true);
            itemButtons.setManaged(true);
            backButton.setVisible(false);
            backButton.setManaged(false);
        }
    }

    // Method to submit a new or edited item
    @FXML
    public void submitNewOrEditedItem(ActionEvent event) {
        String name = itemNameField.getText();
        String type = itemTypeField.getText();
        int quantity = Integer.parseInt(itemQuantityField.getText());
        String effect = itemEffectField.getText();

        if (isEditMode && selectedItem != null) {
            // Update item in the database
            selectedItem.setName(name);
            selectedItem.setType(type);
            selectedItem.setQuantity(quantity);
            selectedItem.setEffect(effect);
            dbHandler.updateItem(selectedItem);
        } else {
            // Insert new item into the database
            dbHandler.insertItem(name, type, quantity, effect);
        }

        clearFormFields();
        toggleAddEditItem(event); // Return to the main menu
    }

    @FXML
    public void deleteItem(ActionEvent event) {
        if (selectedItem != null) {
            // Create confirmation alert
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Delete Confirmation");
            confirmationAlert.setHeaderText("Are you sure you want to delete the item: " + selectedItem.getName() + "?");
            confirmationAlert.setContentText("This action cannot be undone.");

            // Show confirmation and wait for user input
            ButtonType result = confirmationAlert.showAndWait().orElse(ButtonType.CANCEL);

            if (result == ButtonType.OK) {
                // User chose OK, proceed with deletion
                dbHandler.deleteItem(selectedItem);

                // Reload the item list after deletion
                loadItemsIntoListView();

                // Reset selectedItem after deletion
                selectedItem = null;
            }
        } else {
            // Show alert if no item is selected
            showAlert("Please select an item to delete.");
        }
    }

    // Re-enable item selection after items are loaded
    private void enableItemSelection() {
        itemListView.setDisable(false);
    }

    // Helper method to populate form fields with item data
    private void populateFormFields(Item item) {
        itemNameField.setText(item.getName());
        itemTypeField.setText(item.getType());
        itemQuantityField.setText(Integer.toString(item.getQuantity()));
        itemEffectField.setText(item.getEffect());
    }

    // Helper method to clear form fields
    private void clearFormFields() {
        itemNameField.clear();
        itemTypeField.clear();
        itemQuantityField.clear();
        itemEffectField.clear();
    }

    @FXML
    public void handleBackToMenu(ActionEvent event) {
        if (isListViewVisible) {
            // If currently viewing the item list, go back to main menu
            itemListView.setVisible(false);
            itemListView.setManaged(false);
            itemListView.getItems().clear();
            isListViewVisible = false;

        } else if (isAddFormVisible) {
            // If currently in Add/Edit form, go back to main menu
            addEditItemForm.setVisible(false);
            addEditItemForm.setManaged(false);
            isAddFormVisible = false;
        }

        // Show main menu buttons
        itemButtons.setVisible(true);
        itemButtons.setManaged(true);

        // Hide the back button
        backButton.setVisible(false);
        backButton.setManaged(false);
    }

    @FXML
    public void handleBackToMainMenu(ActionEvent event) {
        if (mainApp != null) {
            mainApp.goBackToMainMenu();
        } else {
            System.out.println("MainApp is not set");
        }
    }

    // Method to display alerts
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("No Item Selected");
        alert.setContentText(message);
        alert.showAndWait();
    }
}