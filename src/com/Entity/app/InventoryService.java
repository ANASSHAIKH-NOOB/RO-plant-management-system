package src.com.Entity.app;

import src.com.Persistance.app.InventoryDAO;
import src.com.Entity.app.InventoryItem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryService {

    private static InventoryService instance;
    private final InventoryDAO inventoryDAO;
    
    private final Map<String, InventoryItem> stockMap;
    private final TrieNode rootNode;

    private InventoryService() {
        this.inventoryDAO = new InventoryDAO();
        this.stockMap = new HashMap<>();
        this.rootNode = new TrieNode();
        loadInitialData();
    }

    public static synchronized InventoryService getInstance() {
        if (instance == null) {
            instance = new InventoryService();
        }
        return instance;
    }

    private void loadInitialData() {
        List<InventoryItem> items = inventoryDAO.getAllItems();
        for (InventoryItem item : items) {
            stockMap.put(item.getName().toLowerCase(), item);
            insertIntoTrie(item.getName());
        }
    }

    public void addNewItem(InventoryItem item) {
        if (stockMap.containsKey(item.getName().toLowerCase())) {
            throw new IllegalArgumentException("Item already exists: " + item.getName());
        }

        try {
            inventoryDAO.addItem(item);
            
            stockMap.put(item.getName().toLowerCase(), item);
            insertIntoTrie(item.getName());
            
        } catch (SQLException e) {
            throw new RuntimeException("Database Error: " + e.getMessage());
        }
    }

    public void updateStock(String itemName, int change) {
        InventoryItem item = stockMap.get(itemName.toLowerCase());
        if (item == null) {
            throw new IllegalArgumentException("Item not found: " + itemName);
        }

        item.adjustStock(change);
        
        try {
            inventoryDAO.updateStockCount(item.getId(), item.getCurrentStock());
        } catch (SQLException e) {
            item.adjustStock(-change); 
            throw new RuntimeException("Failed to update DB stock.");
        }
    }

    public InventoryItem getItemByName(String name) {
        return stockMap.get(name.toLowerCase());
    }

    public List<InventoryItem> getAllItems() {
        return new ArrayList<>(stockMap.values());
    }

    private void insertIntoTrie(String word) {
        TrieNode current = rootNode;
        for (char ch : word.toLowerCase().toCharArray()) {
            current.children.putIfAbsent(ch, new TrieNode());
            current = current.children.get(ch);
        }
        current.isEndOfWord = true;
        current.actualName = word; 
    }

    public List<InventoryItem> searchItems(String prefix) {
        List<InventoryItem> results = new ArrayList<>();
        TrieNode current = rootNode;

        for (char ch : prefix.toLowerCase().toCharArray()) {
            current = current.children.get(ch);
            if (current == null) {
                return results; 
            }
        }

        collectAllWords(current, results);
        return results;
    }

    private void collectAllWords(TrieNode node, List<InventoryItem> results) {
        if (node.isEndOfWord) {
            results.add(stockMap.get(node.actualName.toLowerCase()));
        }
        for (TrieNode child : node.children.values()) {
            collectAllWords(child, results);
        }
    }

    private static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isEndOfWord = false;
        String actualName;
    }
}