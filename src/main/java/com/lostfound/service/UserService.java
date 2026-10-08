package com.lostfound.service;

import com.lostfound.dao.NotificationDAO;
import com.lostfound.dao.UserDAO;
import com.lostfound.exception.InvalidInputException;
import com.lostfound.exception.UserNotFoundException;
import com.lostfound.model.Notification;
import com.lostfound.model.User;
import com.lostfound.util.Validation;
import java.util.List;

/**
 * Service implementation for UserOperations interface.
 * Implements business validation, data coordination, and notifications.
 */
public class UserService implements UserOperations {

    private final UserDAO userDAO;
    private final NotificationDAO notificationDAO;

    public UserService() {
        this.userDAO = new UserDAO();
        this.notificationDAO = new NotificationDAO();
    }

    public UserService(UserDAO userDAO, NotificationDAO notificationDAO) {
        this.userDAO = userDAO;
        this.notificationDAO = notificationDAO;
    }

    @Override
    public boolean register(User user) throws InvalidInputException {
        if (user == null) {
            throw new InvalidInputException("User data cannot be null.");
        }

        // Validate all fields using Validation utility
        Validation.validateName(user.getName());
        Validation.validateUsername(user.getUsername());
        Validation.validateEmail(user.getEmail());
        Validation.validatePassword(user.getPassword());
        Validation.validatePhone(user.getPhone());

        // Check if email or username already exist in MySQL
        if (userDAO.emailExists(user.getEmail())) {
            throw new InvalidInputException("Email address is already registered: " + user.getEmail());
        }
        if (userDAO.usernameExists(user.getUsername())) {
            throw new InvalidInputException("Username is already taken: " + user.getUsername());
        }

        boolean registered = userDAO.registerUser(user);
        if (registered) {
            // Welcome notification
            notificationDAO.createNotification(new Notification(
                user.getUserId(),
                "Welcome to the Campus Lost & Found Portal! You can now report lost or found items and submit claims."
            ));
        }
        return registered;
    }

    @Override
    public User login(String identifier, String password) throws InvalidInputException, UserNotFoundException {
        if (identifier == null || identifier.trim().isEmpty()) {
            throw new InvalidInputException("Username or email is required.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new InvalidInputException("Password is required.");
        }

        User user = userDAO.loginUser(identifier.trim(), password);
        if (user == null) {
            throw new UserNotFoundException("Invalid credentials. Please check your username/email and password.");
        }

        if (!user.isActive()) {
            throw new InvalidInputException("Your account has been BLOCKED by the administrator. Please contact support.");
        }

        return user;
    }

    @Override
    public User getUserById(int userId) throws UserNotFoundException {
        User user = userDAO.findUserById(userId);
        if (user == null) {
            throw new UserNotFoundException("User with ID " + userId + " does not exist.");
        }
        return user;
    }

    @Override
    public boolean updateProfile(User user) throws InvalidInputException, UserNotFoundException {
        if (user == null || user.getUserId() <= 0) {
            throw new InvalidInputException("Invalid user details.");
        }

        Validation.validateName(user.getName());
        Validation.validateEmail(user.getEmail());
        Validation.validatePhone(user.getPhone());

        User existing = getUserById(user.getUserId());
        // If changing email, ensure new email is not taken by another user
        if (!existing.getEmail().equalsIgnoreCase(user.getEmail()) && userDAO.emailExists(user.getEmail())) {
            throw new InvalidInputException("Email is already taken by another account.");
        }

        return userDAO.updateUser(user);
    }

    @Override
    public boolean changePassword(int userId, String oldPassword, String newPassword) throws InvalidInputException, UserNotFoundException {
        User user = getUserById(userId);
        if (!user.getPassword().equals(oldPassword)) {
            throw new InvalidInputException("Current password does not match.");
        }
        Validation.validatePassword(newPassword);
        return userDAO.updatePassword(userId, newPassword);
    }

    @Override
    public boolean changeUserStatus(int userId, String status) throws UserNotFoundException {
        getUserById(userId); // ensure user exists
        return userDAO.updateUserStatus(userId, status);
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    public int getTotalUsers() {
        return userDAO.getTotalUserCount();
    }
}
