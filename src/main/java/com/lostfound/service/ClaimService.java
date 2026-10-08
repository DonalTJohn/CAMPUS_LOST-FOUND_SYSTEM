package com.lostfound.service;

import com.lostfound.dao.ClaimDAO;
import com.lostfound.dao.ClaimVerificationDAO;
import com.lostfound.dao.ItemDAO;
import com.lostfound.dao.NotificationDAO;
import com.lostfound.exception.InvalidInputException;
import com.lostfound.exception.ItemNotFoundException;
import com.lostfound.model.Claim;
import com.lostfound.model.ClaimVerification;
import com.lostfound.model.Item;
import com.lostfound.model.Notification;
import com.lostfound.util.Validation;
import java.util.List;

/**
 * Service implementation for ClaimOperations.
 */
public class ClaimService implements ClaimOperations {

    private final ClaimDAO claimDAO;
    private final ClaimVerificationDAO verificationDAO;
    private final ItemDAO itemDAO;
    private final NotificationDAO notificationDAO;

    public ClaimService() {
        this.claimDAO = new ClaimDAO();
        this.verificationDAO = new ClaimVerificationDAO();
        this.itemDAO = new ItemDAO();
        this.notificationDAO = new NotificationDAO();
    }

    public ClaimService(ClaimDAO claimDAO, ClaimVerificationDAO verificationDAO, ItemDAO itemDAO, NotificationDAO notificationDAO) {
        this.claimDAO = claimDAO;
        this.verificationDAO = verificationDAO;
        this.itemDAO = itemDAO;
        this.notificationDAO = notificationDAO;
    }

    @Override
    public boolean submitClaim(Claim claim) throws InvalidInputException, ItemNotFoundException {
        if (claim == null) {
            throw new InvalidInputException("Claim details cannot be empty.");
        }

        Validation.validateClaimProof(claim.getProofDescription());

        Item item = itemDAO.getItemById(claim.getItemId());
        if (item == null) {
            throw new ItemNotFoundException("Item to claim does not exist.");
        }

        if (item.getUserId() == claim.getClaimantId()) {
            throw new InvalidInputException("You cannot submit a claim on an item reported by yourself.");
        }

        if (claimDAO.hasUserClaimedItem(claim.getItemId(), claim.getClaimantId())) {
            throw new InvalidInputException("You already have a pending claim submitted for this item.");
        }

        boolean saved = claimDAO.submitClaim(claim);
        if (saved) {
            // Notify claimant
            notificationDAO.createNotification(new Notification(
                claim.getClaimantId(),
                "Your claim for '" + item.getItemName() + "' was received and is under administrator review."
            ));
            // Notify item reporter
            notificationDAO.createNotification(new Notification(
                item.getUserId(),
                "A user has submitted an ownership claim for your reported item: '" + item.getItemName() + "'."
            ));
        }
        return saved;
    }

    @Override
    public Claim getClaimById(int claimId) throws InvalidInputException {
        Claim claim = claimDAO.getClaimById(claimId);
        if (claim == null) {
            throw new InvalidInputException("Claim with ID " + claimId + " was not found.");
        }
        return claim;
    }

    @Override
    public List<Claim> getClaimsByUser(int userId) {
        return claimDAO.getClaimsByUser(userId);
    }

    @Override
    public List<Claim> getClaimsForItem(int itemId) {
        return claimDAO.getClaimsForItem(itemId);
    }

    @Override
    public List<Claim> getAllClaims() {
        return claimDAO.getAllClaims();
    }

    @Override
    public boolean verifyClaim(int claimId, int adminId, String remarks, String decision) 
            throws InvalidInputException, ItemNotFoundException {
        
        Claim claim = getClaimById(claimId);
        if (remarks == null || remarks.trim().length() < 3) {
            throw new InvalidInputException("Verification remarks are required (at least 3 characters).");
        }
        if (!"APPROVED".equalsIgnoreCase(decision) && !"REJECTED".equalsIgnoreCase(decision)) {
            throw new InvalidInputException("Decision must be either APPROVED or REJECTED.");
        }

        // 1. Update claim status
        boolean updatedClaim = claimDAO.updateClaimStatus(claimId, decision.toUpperCase());
        if (!updatedClaim) {
            return false;
        }

        // 2. Insert verification record
        ClaimVerification verification = new ClaimVerification(claimId, adminId, remarks.trim(), decision.toUpperCase());
        verificationDAO.addVerification(verification);

        // 3. If APPROVED, update Item status to 'CLAIMED' or 'RECOVERED'
        Item item = itemDAO.getItemById(claim.getItemId());
        if ("APPROVED".equalsIgnoreCase(decision)) {
            itemDAO.updateItemStatus(claim.getItemId(), "CLAIMED");
        }

        // 4. Send notification to Claimant
        String claimantMsg = "Your claim for '" + claim.getItemName() + "' has been " + decision.toUpperCase() + 
                             ". Remarks: " + remarks;
        notificationDAO.createNotification(new Notification(claim.getClaimantId(), claimantMsg));

        // 5. Send notification to Item Reporter
        if (item != null) {
            String reporterMsg = "The claim on your reported item '" + item.getItemName() + "' has been " + 
                                 decision.toUpperCase() + " by Admin.";
            notificationDAO.createNotification(new Notification(item.getUserId(), reporterMsg));
        }

        return true;
    }

    @Override
    public ClaimVerification getVerification(int claimId) {
        return verificationDAO.getVerificationByClaimId(claimId);
    }
}
