package com.lostfound.service;

import com.lostfound.exception.InvalidInputException;
import com.lostfound.exception.ItemNotFoundException;
import com.lostfound.model.Claim;
import com.lostfound.model.ClaimVerification;
import java.util.List;

/**
 * Interface defining business operations for Claim submission and Admin verification.
 */
public interface ClaimOperations {

    /**
     * Submits a claim proof for an item.
     */
    boolean submitClaim(Claim claim) throws InvalidInputException, ItemNotFoundException;

    /**
     * Retrieves a claim by ID.
     */
    Claim getClaimById(int claimId) throws InvalidInputException;

    /**
     * Retrieves all claims made by a specific user.
     */
    List<Claim> getClaimsByUser(int userId);

    /**
     * Retrieves all claims for an item.
     */
    List<Claim> getClaimsForItem(int itemId);

    /**
     * Retrieves all claims in the portal for Admin review.
     */
    List<Claim> getAllClaims();

    /**
     * Admin operation: Verifies a claim with remarks and decision (APPROVED/REJECTED).
     */
    boolean verifyClaim(int claimId, int adminId, String remarks, String decision) throws InvalidInputException, ItemNotFoundException;

    /**
     * Retrieves verification details for a claim.
     */
    ClaimVerification getVerification(int claimId);
}
