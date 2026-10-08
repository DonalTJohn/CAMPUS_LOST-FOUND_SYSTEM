package com.lostfound.service;

import com.lostfound.exception.InvalidInputException;
import com.lostfound.exception.UserNotFoundException;
import com.lostfound.model.User;
import java.util.List;

/**
 * Interface defining business operations for User management.
 * Demonstrates abstraction and interface design in Java OOP.
 */
public interface UserOperations {

    /**
     * Registers a new user after full input and uniqueness validation.
     */
    boolean register(User user) throws InvalidInputException;

    /**
     * Authenticates a user by username/email and password.
     */
    User login(String usernameOrEmail, String password) throws InvalidInputException, UserNotFoundException;

    /**
     * Finds a user by ID.
     */
    User getUserById(int userId) throws UserNotFoundException;

    /**
     * Updates user profile information.
     */
    boolean updateProfile(User user) throws InvalidInputException, UserNotFoundException;

    /**
     * Changes a user's password.
     */
    boolean changePassword(int userId, String oldPassword, String newPassword) throws InvalidInputException, UserNotFoundException;

    /**
     * Admin operation: updates user account status (ACTIVE/BLOCKED).
     */
    boolean changeUserStatus(int userId, String status) throws UserNotFoundException;

    /**
     * Admin operation: retrieves all registered users.
     */
    List<User> getAllUsers();

    /**
     * Counts total users.
     */
    int getTotalUsers();
}
