package com.lostfound.dao;

import com.lostfound.model.ClaimVerification;
import com.lostfound.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Data Access Object for ClaimVerification entity.
 */
public class ClaimVerificationDAO {

    /**
     * Records a verification review by an administrator.
     */
    public boolean addVerification(ClaimVerification verification) {
        String sql = "INSERT INTO claim_verifications (claim_id, admin_id, remarks, decision) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, verification.getClaimId());
            ps.setInt(2, verification.getAdminId());
            ps.setString(3, verification.getRemarks());
            ps.setString(4, verification.getDecision());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        verification.setVerificationId(rs.getInt(1));
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
     * Retrieves verification info for a claim.
     */
    public ClaimVerification getVerificationByClaimId(int claimId) {
        String sql = "SELECT cv.*, u.name AS admin_name " +
                     "FROM claim_verifications cv " +
                     "JOIN users u ON cv.admin_id = u.user_id " +
                     "WHERE cv.claim_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, claimId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ClaimVerification cv = new ClaimVerification(
                        rs.getInt("verification_id"),
                        rs.getInt("claim_id"),
                        rs.getInt("admin_id"),
                        rs.getString("remarks"),
                        rs.getString("decision"),
                        rs.getTimestamp("verified_at")
                    );
                    cv.setAdminName(rs.getString("admin_name"));
                    return cv;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
