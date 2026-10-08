package com.lostfound.util;

import com.lostfound.exception.InvalidInputException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * Server-side validation utility class.
 * Validates user input according to business rules and security policies.
 */
public class Validation {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private static final Pattern USERNAME_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_]{3,50}$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[0-9]{10,15}$"
    );

    private static final Pattern PASSWORD_DIGIT_PATTERN = Pattern.compile(
        ".*[0-9].*"
    );

    // Private constructor
    private Validation() {}

    /**
     * Validates user full name.
     */
    public static void validateName(String name) throws InvalidInputException {
        if (name == null || name.trim().length() < 2 || name.trim().length() > 100) {
            throw new InvalidInputException("Full name must be between 2 and 100 characters.");
        }
    }

    /**
     * Validates username format.
     */
    public static void validateUsername(String username) throws InvalidInputException {
        if (username == null || !USERNAME_PATTERN.matcher(username.trim()).matches()) {
            throw new InvalidInputException("Username must be 3-50 characters containing only letters, numbers, or underscores.");
        }
    }

    /**
     * Validates email address format.
     */
    public static void validateEmail(String email) throws InvalidInputException {
        if (email == null || !EMAIL_PATTERN.matcher(email.trim()).matches() || email.trim().length() > 100) {
            throw new InvalidInputException("Please provide a valid email address (maximum 100 characters).");
        }
    }

    /**
     * Validates password: at least 8 characters and contains at least one digit.
     */
    public static void validatePassword(String password) throws InvalidInputException {
        if (password == null || password.length() < 8) {
            throw new InvalidInputException("Password must be at least 8 characters long.");
        }
        if (!PASSWORD_DIGIT_PATTERN.matcher(password).matches()) {
            throw new InvalidInputException("Password must contain at least one numeric digit.");
        }
    }

    /**
     * Validates phone number.
     */
    public static void validatePhone(String phone) throws InvalidInputException {
        if (phone == null || !PHONE_PATTERN.matcher(phone.trim()).matches()) {
            throw new InvalidInputException("Phone number must contain between 10 and 15 digits without symbols.");
        }
    }

    /**
     * Validates item name.
     */
    public static void validateItemName(String itemName) throws InvalidInputException {
        if (itemName == null || itemName.trim().length() < 2 || itemName.trim().length() > 100) {
            throw new InvalidInputException("Item name must be between 2 and 100 characters.");
        }
    }

    /**
     * Validates category ID.
     */
    public static void validateCategory(int categoryId) throws InvalidInputException {
        if (categoryId <= 0) {
            throw new InvalidInputException("Please select a valid item category.");
        }
    }

    /**
     * Validates item description.
     */
    public static void validateDescription(String description) throws InvalidInputException {
        if (description == null || description.trim().length() < 5 || description.trim().length() > 500) {
            throw new InvalidInputException("Description must be between 5 and 500 characters.");
        }
    }

    /**
     * Validates location.
     */
    public static void validateLocation(String location) throws InvalidInputException {
        if (location == null || location.trim().length() < 2 || location.trim().length() > 150) {
            throw new InvalidInputException("Location must be between 2 and 150 characters.");
        }
    }

    /**
     * Validates incident date (must be in YYYY-MM-DD format and cannot be in the future).
     */
    public static void validateDate(String dateStr) throws InvalidInputException {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            throw new InvalidInputException("Incident date is required.");
        }
        try {
            LocalDate date = LocalDate.parse(dateStr.trim());
            if (date.isAfter(LocalDate.now())) {
                throw new InvalidInputException("Incident date cannot be in the future.");
            }
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("Incident date must be in valid YYYY-MM-DD format.");
        }
    }

    /**
     * Validates claim proof description.
     */
    public static void validateClaimProof(String proof) throws InvalidInputException {
        if (proof == null || proof.trim().length() < 10 || proof.trim().length() > 500) {
            throw new InvalidInputException("Proof description must be between 10 and 500 characters providing clear ownership details.");
        }
    }
}
