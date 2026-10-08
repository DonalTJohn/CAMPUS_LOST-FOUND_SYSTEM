package com.lostfound.dao;

import com.lostfound.model.Item;
import com.lostfound.util.DBConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object for Item entity.
 * Handles item creation, queries, multi-filter search, status updates, and potential match heuristics.
 */
public class ItemDAO {

    private static final String BASE_SELECT = 
        "SELECT i.*, c.category_name, u.name AS reporter_name, u.email AS reporter_email, u.phone AS reporter_phone " +
        "FROM items i " +
        "JOIN categories c ON i.category_id = c.category_id " +
        "JOIN users u ON i.user_id = u.user_id ";

    /**
     * Adds a new item to the database.
     */
    public boolean addItem(Item item) {
        String sql = "INSERT INTO items (user_id, category_id, item_name, item_type, description, location, incident_date, image_path, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, item.getUserId());
            ps.setInt(2, item.getCategoryId());
            ps.setString(3, item.getItemName());
            ps.setString(4, item.getItemType());
            ps.setString(5, item.getDescription());
            ps.setString(6, item.getLocation());
            ps.setDate(7, item.getIncidentDate());
            ps.setString(8, item.getImagePath());
            ps.setString(9, item.getStatus() != null ? item.getStatus() : "ACTIVE");

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        item.setItemId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Updates an existing item.
     */
    public boolean updateItem(Item item) {
        String sql = "UPDATE items SET category_id = ?, item_name = ?, description = ?, location = ?, incident_date = ?, image_path = ?, status = ? " +
                     "WHERE item_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, item.getCategoryId());
            ps.setString(2, item.getItemName());
            ps.setString(3, item.getDescription());
            ps.setString(4, item.getLocation());
            ps.setDate(5, item.getIncidentDate());
            ps.setString(6, item.getImagePath());
            ps.setString(7, item.getStatus());
            ps.setInt(8, item.getItemId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Updates item status (ACTIVE, CLAIMED, RECOVERED, REJECTED, PENDING).
     */
    public boolean updateItemStatus(int itemId, String status) {
        String sql = "UPDATE items SET status = ? WHERE item_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ps.setInt(2, itemId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes an item by ID.
     */
    public boolean deleteItem(int itemId) {
        String sql = "DELETE FROM items WHERE item_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, itemId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves an item by its ID.
     */
    public Item getItemById(int itemId) {
        String sql = BASE_SELECT + "WHERE i.item_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToItem(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all items ordered by creation date.
     */
    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        String sql = BASE_SELECT + "ORDER BY i.created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                items.add(mapResultSetToItem(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    /**
     * Retrieves all active LOST items.
     */
    public List<Item> getLostItems() {
        return getItemsByType("LOST");
    }

    /**
     * Retrieves all active FOUND items.
     */
    public List<Item> getFoundItems() {
        return getItemsByType("FOUND");
    }

    private List<Item> getItemsByType(String type) {
        List<Item> items = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE i.item_type = ? AND i.status != 'REJECTED' ORDER BY i.created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, type);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(mapResultSetToItem(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    /**
     * Retrieves items reported by a specific user.
     */
    public List<Item> getItemsByUser(int userId) {
        List<Item> items = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE i.user_id = ? ORDER BY i.created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(mapResultSetToItem(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    /**
     * Searches items with dynamic multi-criteria filters using PreparedStatement.
     */
    public List<Item> searchItems(String keyword, Integer categoryId, String location, String itemType, String status, String dateStr) {
        List<Item> items = new ArrayList<>();
        StringBuilder sql = new StringBuilder(BASE_SELECT).append("WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (i.item_name LIKE ? OR i.description LIKE ?) ");
            String term = "%" + keyword.trim() + "%";
            params.add(term);
            params.add(term);
        }

        if (categoryId != null && categoryId > 0) {
            sql.append("AND i.category_id = ? ");
            params.add(categoryId);
        }

        if (location != null && !location.trim().isEmpty()) {
            sql.append("AND i.location LIKE ? ");
            params.add("%" + location.trim() + "%");
        }

        if (itemType != null && !itemType.trim().isEmpty() && !"ALL".equalsIgnoreCase(itemType)) {
            sql.append("AND i.item_type = ? ");
            params.add(itemType.trim());
        }

        if (status != null && !status.trim().isEmpty() && !"ALL".equalsIgnoreCase(status)) {
            sql.append("AND i.status = ? ");
            params.add(status.trim());
        }

        if (dateStr != null && !dateStr.trim().isEmpty()) {
            try {
                Date date = Date.valueOf(dateStr.trim());
                sql.append("AND i.incident_date = ? ");
                params.add(date);
            } catch (IllegalArgumentException ignored) {}
        }

        sql.append("ORDER BY i.created_at DESC");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(mapResultSetToItem(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    /**
     * Heuristic Potential Match finder:
     * When a LOST item is reported (or viewed), searches active FOUND items that share:
     * 1) Same category
     * 2) Or similar keywords in name/description
     * 3) Or similar location
     * 4) Or incident date within +/- 7 days
     */
    public List<Item> findPotentialMatches(Item item) {
        List<Item> matches = new ArrayList<>();
        if (item == null) return matches;

        String targetType = item.isLost() ? "FOUND" : "LOST";
        String sql = BASE_SELECT + 
            "WHERE i.item_type = ? AND i.status = 'ACTIVE' AND i.item_id != ? AND (" +
            "  i.category_id = ? OR " +
            "  i.item_name LIKE ? OR " +
            "  i.location LIKE ? OR " +
            "  (i.incident_date BETWEEN DATE_SUB(?, INTERVAL 7 DAY) AND DATE_ADD(?, INTERVAL 7 DAY))" +
            ") ORDER BY (CASE WHEN i.category_id = ? THEN 2 ELSE 0 END + CASE WHEN i.location LIKE ? THEN 2 ELSE 0 END) DESC LIMIT 6";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String firstKeyword = "%" + item.getItemName().split("\\s+")[0] + "%";
            String locationMatch = "%" + (item.getLocation() != null ? item.getLocation() : "") + "%";

            ps.setString(1, targetType);
            ps.setInt(2, item.getItemId());
            ps.setInt(3, item.getCategoryId());
            ps.setString(4, firstKeyword);
            ps.setString(5, locationMatch);
            ps.setDate(6, item.getIncidentDate());
            ps.setDate(7, item.getIncidentDate());
            ps.setInt(8, item.getCategoryId());
            ps.setString(9, locationMatch);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    matches.add(mapResultSetToItem(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return matches;
    }

    /**
     * Dashboard analytics statistics.
     */
    public Map<String, Integer> getStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        String sql = "SELECT " +
                     "COUNT(*) AS total_items, " +
                     "SUM(CASE WHEN item_type = 'LOST' THEN 1 ELSE 0 END) AS total_lost, " +
                     "SUM(CASE WHEN item_type = 'FOUND' THEN 1 ELSE 0 END) AS total_found, " +
                     "SUM(CASE WHEN status = 'ACTIVE' THEN 1 ELSE 0 END) AS active_items, " +
                     "SUM(CASE WHEN status = 'RECOVERED' OR status = 'CLAIMED' THEN 1 ELSE 0 END) AS resolved_items " +
                     "FROM items";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                stats.put("totalItems", rs.getInt("total_items"));
                stats.put("totalLost", rs.getInt("total_lost"));
                stats.put("totalFound", rs.getInt("total_found"));
                stats.put("activeItems", rs.getInt("active_items"));
                stats.put("resolvedItems", rs.getInt("resolved_items"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }

    private Item mapResultSetToItem(ResultSet rs) throws SQLException {
        Item item = new Item(
            rs.getInt("item_id"),
            rs.getInt("user_id"),
            rs.getInt("category_id"),
            rs.getString("item_name"),
            rs.getString("item_type"),
            rs.getString("description"),
            rs.getString("location"),
            rs.getDate("incident_date"),
            rs.getString("image_path"),
            rs.getString("status"),
            rs.getTimestamp("created_at")
        );
        item.setCategoryName(rs.getString("category_name"));
        item.setReporterName(rs.getString("reporter_name"));
        item.setReporterEmail(rs.getString("reporter_email"));
        item.setReporterPhone(rs.getString("reporter_phone"));
        return item;
    }
}
