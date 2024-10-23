package com.home.rpgapp.controller;

import com.home.rpgapp.RPGMaintenanceSystemMain;
import com.home.rpgapp.database.DatabaseHandler;
import com.home.rpgapp.model.Character;
import com.home.rpgapp.model.Item;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class CharacterController {

    // Buttons and forms
    @FXML
    private GridPane characterButtons; // The main menu buttons
    @FXML
    private Button submitButton; // Button used for submitting new or edited characters
    @FXML
    private Button assignItemsButton; // Button used for assigning items to characters
    @FXML
    private Button backButton;  // Single back button used for all views

    @FXML
    private ListView<String> characterListView; // ListView to display characters
    private ObservableList<Character> characterList;
    private boolean isEditMode = false; // Track whether we're editing or adding a new character
    private Character selectedCharacter; // The currently selected character to be edited
    private ObservableList<Item> selectedItems; // List of selected items to be assigned
    @FXML
    private VBox addEditCharacterForm; // The form for adding/editing a character
    @FXML
    private TableView<Item> itemSelectionTableView; // TableView for displaying items
    @FXML
    private TableColumn<Item, String> itemNameColumn;
    @FXML
    private TableColumn<Item, Integer> itemQuantityColumn;
    @FXML
    private TableView<Item> characterInventoryTableView; // TableView for character inventory
    @FXML
    private TableColumn<Character, String> characterNameColumn;
    @FXML
    private TableColumn<Item, String> characterItemNameColumn;
    @FXML
    private TableColumn<Item, Integer> characterItemQuantityColumn;
    // Text fields for adding new character
    @FXML
    private TextField characterNameField;
    @FXML
    private TextField characterClassField;
    @FXML
    private TextField characterLevelField;
    @FXML
    private TextField characterHpField;
    @FXML
    private TextField characterXpField;

    // States for toggling visibility
    private boolean isListViewVisible = false;
    private boolean isAddFormVisible = false;
    private ObservableList<Item> itemList;
    private ObservableList<Item> characterInventoryList;
    private DatabaseHandler dbHandler;
    private RPGMaintenanceSystemMain mainApp;
    public void setMainApp(RPGMaintenanceSystemMain mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void initialize() {
        dbHandler = new DatabaseHandler();
        dbHandler.getConnection();
        characterListView.setVisible(false);
        characterListView.setManaged(false); // Ensure it's not shown by default
        characterButtons.setVisible(true);
        characterButtons.setManaged(true); // Ensure buttons are shown by default
        backButton.setVisible(false);
        backButton.setManaged(true);

        // Configure the item table view for items to be assigned
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        itemQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        itemSelectionTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // Users could choose multiple items to be assigned

        // Configure the table for viewing character inventory
        characterNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        characterItemNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        characterItemQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        characterList = FXCollections.observableArrayList();
        characterInventoryList = FXCollections.observableArrayList();
        selectedItems = FXCollections.observableArrayList(); // Initialize selected items list
        characterListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); // Users could assign single item only during selection

        // Add listener for character selection
        characterListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int selectedIndex = characterListView.getSelectionModel().getSelectedIndex();
                if (!characterList.isEmpty() && selectedIndex >= 0 && selectedIndex < characterList.size()) {
                    selectedCharacter = characterList.get(selectedIndex);
                    System.out.println("Selected character: " + selectedCharacter.getName());
                } else {
                    System.out.println("Invalid selection or empty list.");
                }
            }
        });

        // Add listener for item selection changes
        itemSelectionTableView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Item>) change -> {
            selectedItems.clear(); // Clear previous selections
            selectedItems.addAll(itemSelectionTableView.getSelectionModel().getSelectedItems()); // Add currently selected items
            System.out.println("Selected items: " + selectedItems);
        });
    }

    @FXML
    public void toggleViewCharacterList(ActionEvent event) {
        isListViewVisible = !isListViewVisible;

        if (isListViewVisible) {
            // Show ListView, hide main buttons, show Back button in a new position
            characterButtons.setVisible(false);
            characterButtons.setManaged(false);
            characterListView.setVisible(true);
            characterListView.setManaged(true);
            assignItemsButton.setVisible(false);

            backButton.setLayoutX(500);
            backButton.setLayoutY(440);
            backButton.setVisible(true);
            backButton.setManaged(true);

            loadCharactersIntoListView();

            enableCharacterSelection();
        } else {
            characterListView.setVisible(false);
            characterListView.setManaged(false);
            characterButtons.setVisible(true);
            characterButtons.setManaged(true);
            backButton.setVisible(false);
            backButton.setManaged(false);
        }
    }

    // Method to load character list
    public ObservableList<Character> loadCharactersIntoListView() {
        // Clear the current ListView and hidden item list
        characterListView.getItems().clear();
        characterList.clear();

        // Fetch the items from the database
        List<Character> charactersFromDB = dbHandler.loadCharacterList();

        System.out.println("Total characters: " + charactersFromDB.size());

        // Store the items in the hidden item list
        characterList.addAll(charactersFromDB);

        // Fill the ListView with items fetched from the database
        for (Character character : characterList) {
            characterListView.getItems().add(character.getName() + " - Class: " + character.getCharClass());
        }

        System.out.println("Updated total characters: " + characterListView.getItems().size());

        return characterList;
    }

    @FXML
    public void toggleAddNewCharacter(ActionEvent event) {
        isEditMode = false;

        isAddFormVisible = !isAddFormVisible;

        if (isAddFormVisible) {
            // Hide main buttons, show form, move Back button to new position
            characterButtons.setVisible(false);
            characterButtons.setManaged(false);
            addEditCharacterForm.setVisible(true);
            addEditCharacterForm.setManaged(true);

            // Reset form fields to empty values
            clearFormFields();

            // Update the submit button text to set title to "Add Character"
            submitButton.setText("Add Character");

            // Move and show the back button for the form
            backButton.setLayoutX(500);
            backButton.setLayoutY(440);
            backButton.setVisible(true);
            backButton.setManaged(true);
        } else {
            // If returning to the main menu, hide the form and back button
            addEditCharacterForm.setVisible(false);
            addEditCharacterForm.setManaged(false);
            characterButtons.setVisible(true);
            characterButtons.setManaged(true);
            backButton.setVisible(false);
            backButton.setManaged(false);
        }
    }


    @FXML
    public void toggleAddEditCharacter(ActionEvent event) {
        isAddFormVisible = !isAddFormVisible;

        if (isAddFormVisible) {
            // Hide main buttons, show form, move Back button to new position
            characterButtons.setVisible(false);
            characterButtons.setManaged(false);
            addEditCharacterForm.setVisible(true);
            addEditCharacterForm.setManaged(true);

            // Position the back button for the edit form
            backButton.setLayoutX(500);
            backButton.setLayoutY(440);
            backButton.setVisible(true);
            backButton.setManaged(true);

            if (isEditMode && selectedCharacter != null) {
                // Editing an existing character, update form and button text
                submitButton.setText("Update Character");
                populateFormFields(selectedCharacter);
            } else {
                // Adding a new character
                submitButton.setText("Add Character");
                clearFormFields();
            }
        } else {
            // If returning to the main menu, hide the form and back button
            addEditCharacterForm.setVisible(false);
            addEditCharacterForm.setManaged(false);
            characterButtons.setVisible(true);
            characterButtons.setManaged(true);
            backButton.setVisible(false);
            backButton.setManaged(false);
        }
    }

    // Method to switch to edit mode and pre-fill character data
    @FXML
    public void toggleEditCharacter(ActionEvent event) {
        if (selectedCharacter != null) {
            isEditMode = true;
            toggleAddEditCharacter(event);
        } else {
            showAlert("Please select a character to edit.");
        }
    }

    // Method to toggle the assign items view
    @FXML
    public void toggleAssignItemsToCharacter(ActionEvent event) {
        if (selectedCharacter == null) {
            showAlert("Please select a character before assigning items.");
            return;
        }
        // Show item assignment table and assign button, hide everything else
        characterListView.setVisible(false);
        itemSelectionTableView.setVisible(true);
        assignItemsButton.setVisible(true); // Show the assign items button
        backButton.setVisible(true);
        characterInventoryTableView.setVisible(false);
        characterButtons.setVisible(false);

        // Load items into the table view
        loadItemsIntoTableView();
    }


    // Method to submit a new or edited character
    @FXML
    public void submitNewOrEditedCharacter(ActionEvent event) {
        String name = characterNameField.getText();
        String charClass = characterClassField.getText();
        int level = Integer.parseInt(characterLevelField.getText());
        int hp = Integer.parseInt(characterHpField.getText());
        int xp = Integer.parseInt(characterXpField.getText());

        if (isEditMode && selectedCharacter != null) {
            // Update character in the database
            selectedCharacter.setName(name);
            selectedCharacter.setCharClass(charClass);
            selectedCharacter.setLevel(level);
            selectedCharacter.setHp(hp);
            selectedCharacter.setXp(xp);
            dbHandler.updateCharacter(selectedCharacter);
        } else {
            // Insert new character into the database
            dbHandler.insertCharacter(name, charClass, level, hp, xp);
        }

        clearFormFields();
        toggleAddEditCharacter(event); // Return to the main menu
    }

    @FXML
    public void deleteCharacter(ActionEvent event) {
        if (selectedCharacter != null) {
            // Create confirmation alert
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Delete Confirmation");
            confirmationAlert.setHeaderText("Are you sure you want to delete the character: " + selectedCharacter.getName() + "?");
            confirmationAlert.setContentText("This action cannot be undone.");

            // Show confirmation and wait for user input
            ButtonType result = confirmationAlert.showAndWait().orElse(ButtonType.CANCEL);

            if (result == ButtonType.OK) {
                // User chose OK, proceed with deletion
                dbHandler.deleteCharacter(selectedCharacter);
                loadCharactersIntoListView(); // Refresh the character list
            }
        } else {
            // Show alert if no character is selected
            showAlert("Please select a character to delete.");
        }
    }

    // Re-enable item selection after items are loaded
    private void enableCharacterSelection() {
        characterListView.setDisable(false);
    }

    // Method to load items from the database into the TableView
    public void loadItemsIntoTableView() {
        if (selectedCharacter == null) {
            showAlert("Please select a character first.");
            return;
        }
        List<Item> unassignedItems = dbHandler.getUnassignedItemsForCharacter(selectedCharacter.getCharacterId());
        itemList = FXCollections.observableArrayList(unassignedItems);
        itemSelectionTableView.setItems(itemList);
    }

    // Method to handle assigning selected items to a character
    @FXML
    public void assignSelectedItemsToCharacter(ActionEvent event) {
        if (selectedCharacter == null) {
            showAlert("Please select a character before assigning items.");
            return;
        }

        if (selectedItems.isEmpty()) {
            showAlert("Please select at least one item to assign.");
            return;
        }

        // Confirm item assignment
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Item Assignment");
        confirmationAlert.setHeaderText("Are you sure you want to assign these items to " + selectedCharacter.getName() + "?");
        confirmationAlert.setContentText("This action cannot be undone.");

        if (confirmationAlert.showAndWait().get() == ButtonType.OK) {
            for (Item item : selectedItems) {
                boolean assigned = dbHandler.assignItemToCharacter(selectedCharacter.getCharacterId(), item.getItemId());
                if (!assigned) {
                    // Inform the user that the item was already assigned
                    showAlert("Item '" + item.getName() + "' is already assigned to " + selectedCharacter.getName() + ".");
                }
            }
            showAlert("Items assignment completed for " + selectedCharacter.getName());
        }

        // Clear selected items after assignment
        selectedItems.clear();

        // Go back to the character view
        handleBackToMenu(event);
    }

    // Toggle visibility for viewing a character's inventory
    @FXML
    public void toggleViewCharacterInventory(ActionEvent event) {
        isListViewVisible = !isListViewVisible;

        if (isListViewVisible) {
            // Hide main buttons and display the inventory list
            characterButtons.setVisible(false);
            characterButtons.setManaged(false);
            characterInventoryTableView.setVisible(true);
            characterInventoryTableView.setManaged(true);
            backButton.setVisible(true);
            backButton.setManaged(true);

            loadCharacterInventory(); // Load the character's inventory
        } else {
            characterInventoryTableView.setVisible(false);
            characterInventoryTableView.setManaged(false);
            characterButtons.setVisible(true);
            characterButtons.setManaged(true);
            backButton.setVisible(false);
            backButton.setManaged(false);
        }
    }

    // Load the inventory for the selected character
    public void loadCharacterInventory() {
        if (selectedCharacter != null) {
            characterInventoryTableView.getItems().clear();
            List<Item> characterItems = dbHandler.getCharacterItems(selectedCharacter.getCharacterId());

            characterInventoryList.setAll(characterItems);
            characterInventoryTableView.setItems(characterInventoryList);
        } else {
            showAlert("Please select a character.");
        }
    }


    // Helper method to populate form fields with character data
    private void populateFormFields(Character character) {
        characterNameField.setText(character.getName());
        characterClassField.setText(character.getCharClass());
        characterLevelField.setText(Integer.toString(character.getLevel()));
        characterHpField.setText(Integer.toString(character.getHp()));
        characterXpField.setText(Integer.toString(character.getXp()));
    }

    // Helper method to clear form fields
    private void clearFormFields() {
        characterNameField.clear();
        characterClassField.clear();
        characterLevelField.clear();
        characterHpField.clear();
        characterXpField.clear();
    }

    @FXML
    public void handleBackToMenu(ActionEvent event) {
        // Hide character list if visible
        if (isListViewVisible) {
            characterListView.setVisible(false);
            characterListView.setManaged(false);
            characterListView.getItems().clear();
            isListViewVisible = false;
        }

        // Hide add/edit character form if visible
        if (isAddFormVisible) {
            addEditCharacterForm.setVisible(false);
            addEditCharacterForm.setManaged(false);
            isAddFormVisible = false;
        }

        // Hide item selection table if visible
        if (itemSelectionTableView.isVisible()) {
            itemSelectionTableView.setVisible(false);
            itemSelectionTableView.setManaged(false);
            itemSelectionTableView.getItems().clear();
        }

        // Hide character inventory table if visible
        if (characterInventoryTableView.isVisible()) {
            characterInventoryTableView.setVisible(false);
            characterInventoryTableView.setManaged(false);
            characterInventoryTableView.getItems().clear();
        }

        assignItemsButton.setVisible(false);

        // Show the main menu buttons
        characterButtons.setVisible(true);
        characterButtons.setManaged(true);

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
        alert.setTitle("No Character Selected");
        alert.setContentText(message);
        alert.showAndWait();
    }
}