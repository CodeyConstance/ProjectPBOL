package com.home.rpgapp.database;

import com.home.rpgapp.model.Character;
import com.home.rpgapp.model.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {

    private Connection connection;

    // Establishing the connection to the MySQL database
    public Connection getConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/rpg_maintenance_system";
            String user = "root";
            String password = "";

            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to MySQL");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    // Method to insert a new character into the database
    public void insertCharacter(String name, String characterClass, int level, int hp, int xp) {
        Connection connection = getConnection();
        String query = "INSERT INTO characters (name, char_class, level, hp, xp) VALUES (?, ?, ?, ?, ?)";
        boolean isInserted = false;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, characterClass);
            stmt.setInt(3, level);
            stmt.setInt(4, hp);
            stmt.setInt(5, xp);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Character added successfully.");
                isInserted = true;
            } else {
                System.out.println("Add new character failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Ensure the connection is closed
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to retrieve the list of characters from the database
    public ObservableList<Character> loadCharacterList() {
        ObservableList<Character> characterList = FXCollections.observableArrayList();
        Connection connection = getConnection();

        String query = "SELECT * FROM characters";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("character_id");
                String name = resultSet.getString("name");
                String charClass = resultSet.getString("char_class");
                int level = resultSet.getInt("level");
                int hp = resultSet.getInt("hp");
                int xp = resultSet.getInt("xp");

                // Create a new Character object
                Character character = new Character(id, name, charClass, level, hp, xp, null);

                // Add the character to the list
                characterList.add(character);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return characterList;
    }

    // Method to update a character in the database
    public boolean updateCharacter(Character character) {
        String query = "UPDATE characters SET name = ?, char_class = ?, level = ?, hp = ?, xp = ? WHERE character_id = ?";
        Connection connection = getConnection();
        boolean isUpdated = false;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, character.getName());
            stmt.setString(2, character.getCharClass());
            stmt.setInt(3, character.getLevel());
            stmt.setInt(4, character.getHp());
            stmt.setInt(5, character.getXp());
            stmt.setInt(6, character.getCharacterId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Character updated successfully.");
                isUpdated = true;
            } else {
                System.out.println("Character update failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return isUpdated;
    }

    // Method to delete a character from the database
    public void deleteCharacter(Character character) {
        String query = "DELETE FROM characters WHERE character_id = ?";
        Connection connection = getConnection();
        boolean isDeleted = false;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, character.getCharacterId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Character deleted successfully.");
                isDeleted = true;
            } else {
                System.out.println("Character deletion failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // ========================== ITEM METHODS ==========================

    // Method to insert a new item into the database
    public void insertItem(String name, String type, int quantity, String effect) {
        Connection connection = getConnection();
        String query = "INSERT INTO items (name, type, quantity, effect) VALUES (?, ?, ?, ?)";
        boolean isInserted = false;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, type);
            stmt.setInt(3, quantity);
            stmt.setString(4, effect);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Item added successfully.");
                isInserted = true;
            } else {
                System.out.println("Add new item failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to load item list from the database (returns the list of items instead of updating the view)
    public List<Item> loadItemList() {
        List<Item> itemList = new ArrayList<>();
        Connection connection = getConnection();

        String query = "SELECT * FROM items";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("item_id");
                String name = resultSet.getString("name");
                String type = resultSet.getString("type");
                int quantity = resultSet.getInt("quantity");
                String effect = resultSet.getString("effect");

                // Create a new Item object
                Item item = new Item(id, name, type, quantity, effect);

                // Add the item to the list
                itemList.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return itemList; // Return the list of items
    }

    // Method to update an item in the database
    public void updateItem(Item item) {
        String query = "UPDATE items SET name = ?, type = ?, quantity = ?, effect = ? WHERE item_id = ?";
        Connection connection = getConnection();
        boolean isUpdated = false;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getType());
            stmt.setInt(3, item.getQuantity());
            stmt.setString(4, item.getEffect());
            stmt.setInt(5, item.getItemId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Item updated successfully.");
                isUpdated = true;
            } else {
                System.out.println("Item update failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to delete an item from the database
    public void deleteItem(Item item) {
        String query = "DELETE FROM items WHERE item_id = ?";
        Connection connection = getConnection();
        boolean isDeleted = false;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, item.getItemId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Item deleted successfully.");
                isDeleted = true;
            } else {
                System.out.println("Item deletion failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    // Method to assign all item assigned to a specific character
    public boolean assignItemToCharacter(int characterId, int itemId) {
        String checkQuery = "SELECT COUNT(*) FROM character_items WHERE character_id = ? AND item_id = ?";
        String insertQuery = "INSERT INTO character_items (character_id, item_id) VALUES (?, ?)";
        Connection connection = getConnection();
        boolean isAssigned = false;

        try {
            // Check if the item is already assigned to the character
            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, characterId);
                checkStmt.setInt(2, itemId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    int count = rs.getInt(1);
                    if (count > 0) {
                        System.out.println("Item is already assigned to the character.");
                        return false; // Item is already assigned
                    }
                }
            }

            // Proceed to assign the item
            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                insertStmt.setInt(1, characterId);
                insertStmt.setInt(2, itemId);
                int rowsAffected = insertStmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Item assigned to character successfully.");
                    isAssigned = true;
                } else {
                    System.out.println("Item assignment failed.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return isAssigned;
    }

    // Method to get unassigned item assigned to a specific character (items that's not on a specific character's inventory, yet.)
    public List<Item> getUnassignedItemsForCharacter(int characterId) {
        List<Item> unassignedItems = new ArrayList<>();
        String query = "SELECT * FROM items WHERE item_id NOT IN (SELECT item_id FROM character_items WHERE character_id = ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, characterId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Item item = new Item();
                item.setItemId(rs.getInt("item_id"));
                item.setName(rs.getString("name"));
                item.setType(rs.getString("type"));
                item.setQuantity(rs.getInt("quantity"));
                item.setEffect(rs.getString("effect"));
                unassignedItems.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return unassignedItems;
    }

    // Method to retrieve all items assigned to a specific character
    public List<Item> getCharacterItems(int characterId) {
        String query = "SELECT i.item_id, i.name, i.type, i.quantity " +
                "FROM items i " +
                "JOIN character_items ci ON i.item_id = ci.item_id " +
                "WHERE ci.character_id = ?";
        List<Item> characterItems = new ArrayList<>();
        Connection connection = getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, characterId);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("item_id");
                String name = resultSet.getString("name");
                String type = resultSet.getString("type");
                int quantity = resultSet.getInt("quantity");

                Item item = new Item(id, name, type, quantity, null);
                characterItems.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return characterItems;
    }
}