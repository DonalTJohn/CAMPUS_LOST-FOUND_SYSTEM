package com.lostfound.service;

import com.lostfound.exception.InvalidInputException;
import com.lostfound.exception.ItemNotFoundException;
import com.lostfound.model.Category;
import com.lostfound.model.Item;
import java.util.List;
import java.util.Map;

/**
 * Interface defining business operations for Item management.
 */
public interface ItemOperations {

    /**
     * Reports a new lost or found item.
     */
    boolean reportItem(Item item) throws InvalidInputException;

    /**
     * Updates an existing item.
     */
    boolean updateItem(Item item, int requesterUserId, boolean isAdmin) throws InvalidInputException, ItemNotFoundException;

    /**
     * Deletes an item by ID.
     */
    boolean deleteItem(int itemId, int requesterUserId, boolean isAdmin) throws ItemNotFoundException;

    /**
     * Retrieves an item by ID.
     */
    Item getItemById(int itemId) throws ItemNotFoundException;

    /**
     * Retrieves all items.
     */
    List<Item> getAllItems();

    /**
     * Retrieves all lost items.
     */
    List<Item> getLostItems();

    /**
     * Retrieves all found items.
     */
    List<Item> getFoundItems();

    /**
     * Retrieves all items reported by a specific user.
     */
    List<Item> getItemsByUser(int userId);

    /**
     * Performs multi-criteria search for items.
     */
    List<Item> searchItems(String keyword, Integer categoryId, String location, String itemType, String status, String date);

    /**
     * Returns potential matches for a given item using SQL heuristics.
     */
    List<Item> getPotentialMatches(int itemId) throws ItemNotFoundException;

    /**
     * Updates the status of an item (Admin / Claim resolution).
     */
    boolean updateItemStatus(int itemId, String status) throws ItemNotFoundException;

    /**
     * Retrieves all categories.
     */
    List<Category> getAllCategories();

    /**
     * Retrieves portal statistics for dashboard.
     */
    Map<String, Integer> getDashboardStatistics();
}
