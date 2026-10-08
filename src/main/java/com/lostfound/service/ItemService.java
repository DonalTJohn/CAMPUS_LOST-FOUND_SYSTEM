package com.lostfound.service;

import com.lostfound.dao.CategoryDAO;
import com.lostfound.dao.ItemDAO;
import com.lostfound.dao.NotificationDAO;
import com.lostfound.exception.InvalidInputException;
import com.lostfound.exception.ItemNotFoundException;
import com.lostfound.model.Category;
import com.lostfound.model.Item;
import com.lostfound.model.Notification;
import com.lostfound.util.Validation;
import java.util.List;
import java.util.Map;

/**
 * Service implementation for ItemOperations.
 */
public class ItemService implements ItemOperations {

    private final ItemDAO itemDAO;
    private final CategoryDAO categoryDAO;
    private final NotificationDAO notificationDAO;

    public ItemService() {
        this.itemDAO = new ItemDAO();
        this.categoryDAO = new CategoryDAO();
        this.notificationDAO = new NotificationDAO();
    }

    public ItemService(ItemDAO itemDAO, CategoryDAO categoryDAO, NotificationDAO notificationDAO) {
        this.itemDAO = itemDAO;
        this.categoryDAO = categoryDAO;
        this.notificationDAO = notificationDAO;
    }

    @Override
    public boolean reportItem(Item item) throws InvalidInputException {
        if (item == null) {
            throw new InvalidInputException("Item data cannot be null.");
        }

        Validation.validateItemName(item.getItemName());
        Validation.validateCategory(item.getCategoryId());
        Validation.validateDescription(item.getDescription());
        Validation.validateLocation(item.getLocation());
        if (item.getIncidentDate() == null) {
            throw new InvalidInputException("Incident date is required.");
        }
        if (!"LOST".equalsIgnoreCase(item.getItemType()) && !"FOUND".equalsIgnoreCase(item.getItemType())) {
            throw new InvalidInputException("Item type must be either LOST or FOUND.");
        }

        boolean saved = itemDAO.addItem(item);
        if (saved) {
            notificationDAO.createNotification(new Notification(
                item.getUserId(),
                "Your " + item.getItemType() + " item report for '" + item.getItemName() + "' was successfully submitted."
            ));
        }
        return saved;
    }

    @Override
    public boolean updateItem(Item item, int requesterUserId, boolean isAdmin) throws InvalidInputException, ItemNotFoundException {
        Item existing = getItemById(item.getItemId());
        if (!isAdmin && existing.getUserId() != requesterUserId) {
            throw new InvalidInputException("You do not have permission to edit this item.");
        }

        Validation.validateItemName(item.getItemName());
        Validation.validateCategory(item.getCategoryId());
        Validation.validateDescription(item.getDescription());
        Validation.validateLocation(item.getLocation());

        return itemDAO.updateItem(item);
    }

    @Override
    public boolean deleteItem(int itemId, int requesterUserId, boolean isAdmin) throws ItemNotFoundException {
        Item existing = getItemById(itemId);
        if (!isAdmin && existing.getUserId() != requesterUserId) {
            throw new ItemNotFoundException("You do not have permission to delete this item.");
        }
        return itemDAO.deleteItem(itemId);
    }

    @Override
    public Item getItemById(int itemId) throws ItemNotFoundException {
        Item item = itemDAO.getItemById(itemId);
        if (item == null) {
            throw new ItemNotFoundException("Item with ID " + itemId + " was not found.");
        }
        return item;
    }

    @Override
    public List<Item> getAllItems() {
        return itemDAO.getAllItems();
    }

    @Override
    public List<Item> getLostItems() {
        return itemDAO.getLostItems();
    }

    @Override
    public List<Item> getFoundItems() {
        return itemDAO.getFoundItems();
    }

    @Override
    public List<Item> getItemsByUser(int userId) {
        return itemDAO.getItemsByUser(userId);
    }

    @Override
    public List<Item> searchItems(String keyword, Integer categoryId, String location, String itemType, String status, String date) {
        return itemDAO.searchItems(keyword, categoryId, location, itemType, status, date);
    }

    @Override
    public List<Item> getPotentialMatches(int itemId) throws ItemNotFoundException {
        Item item = getItemById(itemId);
        return itemDAO.findPotentialMatches(item);
    }

    @Override
    public boolean updateItemStatus(int itemId, String status) throws ItemNotFoundException {
        Item item = getItemById(itemId);
        boolean updated = itemDAO.updateItemStatus(itemId, status);
        if (updated) {
            notificationDAO.createNotification(new Notification(
                item.getUserId(),
                "The status of your reported item '" + item.getItemName() + "' was updated to " + status + "."
            ));
        }
        return updated;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategories();
    }

    @Override
    public Map<String, Integer> getDashboardStatistics() {
        return itemDAO.getStatistics();
    }
}
