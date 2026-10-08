package com.lostfound.dao;

import com.lostfound.model.Claim;
import com.lostfound.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Claim entity.
 */
public class ClaimDAO {

    private static final String BASE_SELECT = 
        "SELECT c.*, i.item_name, i.item_type, i.location AS item_location, " +
        "u.name AS claimant_name, u.email AS claimant_email, u.phone AS claimant_phone " +
        "FROM claims c " +
        "JOIN items i ON c.item_id = i.item_id " +
        "JOIN users u ON c.claimant_id = u.user_id ";

    /**
     * Submits a new claim for an item.
     */
    public boolean submitClaim(Claim claim) {
        String sql = "INSERT INTO claims (item_id, claimant_id, proof_description, claim_status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, claim.getItemId());
            ps.setInt(2, claim.getClaimantId());
            ps.setString(3, claim.getProofDescription());
            ps.setString(4, claim.getClaimStatus() != null ? claim.getClaimStatus() : "PENDING");

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        claim.setClaimId(rs.getInt(1));
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
     * Retrieves a claim by ID.
     */
    public Claim getClaimById(int claimId) {
        String sql = BASE_SELECT + "WHERE c.claim_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, claimId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToClaim(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all claims submitted by a specific user.
     */
    public List<Claim> getClaimsByUser(int claimantId) {
        List<Claim> list = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE c.claimant_id = ? ORDER BY c.claim_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, claimantId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToClaim(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Retrieves all claims for a specific item.
     */
    public List<Claim> getClaimsForItem(int itemId) {
        List<Claim> list = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE c.item_id = ? ORDER BY c.claim_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToClaim(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Retrieves all claims (for Admin dashboard).
     */
    public List<Claim> getAllClaims() {
        List<Claim> list = new ArrayList<>();
        String sql = BASE_SELECT + "ORDER BY c.claim_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                list.add(mapResultSetToClaim(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Updates status of a claim (PENDING, APPROVED, REJECTED).
     */
    public boolean updateClaimStatus(int claimId, String status) {
        String sql = "UPDATE claims SET claim_status = ? WHERE claim_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ps.setInt(2, claimId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Approves a claim.
     */
    public boolean approveClaim(int claimId) {
        return updateClaimStatus(claimId, "APPROVED");
    }

    /**
     * Rejects a claim.
     */
    public boolean rejectClaim(int claimId) {
        return updateClaimStatus(claimId, "REJECTED");
    }

    /**
     * Checks if a user has already submitted a pending claim for an item.
     */
    public boolean hasUserClaimedItem(int itemId, int userId) {
        String sql = "SELECT COUNT(*) FROM claims WHERE item_id = ? AND claimant_id = ? AND claim_status = 'PENDING'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, itemId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Claim mapResultSetToClaim(ResultSet rs) throws SQLException {
        Claim claim = new Claim(
            rs.getInt("claim_id"),
            rs.getInt("item_id"),
            rs.getInt("claimant_id"),
            rs.getString("proof_description"),
            rs.getString("claim_status"),
            rs.getTimestamp("claim_date")
        );
        claim.setItemName(rs.getString("item_name"));
        claim.setItemType(rs.getString("item_type"));
        claim.setItemLocation(rs.getString("item_location"));
        claim.setClaimantName(rs.getString("claimant_name"));
        claim.setClaimantEmail(rs.getString("claimant_email"));
        claim.setClaimantPhone(rs.getString("claimant_phone"));
        return claim;
    }
}
