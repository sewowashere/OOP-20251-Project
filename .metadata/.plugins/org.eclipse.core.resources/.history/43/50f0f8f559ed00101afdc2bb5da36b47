package com.airline.services;

import com.airline.models.User;
import com.airline.persistence.UserDAOImpl;

public class AuthService {
    private static User currentUser; // Giriş yapan kullanıcıyı hafızada tutar
    private static UserDAOImpl userDao = new UserDAOImpl(); // Static yaparak her yerden erişim sağladık

    public AuthService() {
        // Boş constructor
    }

    // AdminGUI'nin beklediği o meşhur metod
    public static void register(String username, String password, String role) {
        User newUser = new User(username, password, role);
        userDao.save(newUser);
    }

    public static String getUsername() {
        return (currentUser != null) ? currentUser.getUsername() : "Guest";
    }

    public boolean login(String username, String password) {
        User user = userDao.findById(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void logout() {
        currentUser = null;
    }
}