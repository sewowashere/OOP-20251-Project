package com.airline.services;

import com.airline.models.User;
import com.airline.persistence.UserDAOImpl;

public class AuthService {
    private static User currentUser; // Giriş yapan kullanıcıyı hafızada tutar
    private static UserDAOImpl userDao = new UserDAOImpl(); // Static yaparak her yerden erişim sağladık

    public AuthService() {
        // Boş constructor
    }

 // AuthService.java içine eklenecek metot
    public static void register(String username, String password, String role) {
        com.airline.persistence.UserDAOImpl userDao = new com.airline.persistence.UserDAOImpl();
        
        // Kullanıcı adı zaten var mı kontrolü (isteğe bağlı ama önerilir)
        boolean exists = userDao.getAll().stream()
                .anyMatch(u -> u.getUsername().equalsIgnoreCase(username));
                
        if (exists) {
            throw new RuntimeException("Username already taken!");
        }

        User newUser = new User(username, password, role);
        userDao.save(newUser); // Bu işlem veriyi users.csv dosyasına ekler
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