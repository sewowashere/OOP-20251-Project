package com.airline.persistence;

import com.airline.core.dao.AbstractCSVDAO;
import com.airline.models.User;

import java.util.List;

public class UserDAOImpl extends AbstractCSVDAO<User, String> {

    public UserDAOImpl() {
        // Kullanıcı verilerinin saklanacağı dosya adı
        super("users.csv");
    }

    @Override
    protected User createInstance() {
        // DAO dosyadan satır okuduğunda içi boş bir User üretip fromCSV'yi çağıracak
        return new User();
    }

    public void deleteByUsername(String username) {
        // 1. Mevcut tüm kullanıcıları listeden çekiyoruz
        List<User> allUsers = getAll();

        // 2. Silinecek kullanıcı hariç listeyi filtreliyoruz
        List<User> updatedUsers = allUsers.stream()
                .filter(u -> !u.getUsername().equalsIgnoreCase(username))
                .toList();

        // 3. Güncel listeyi dosyaya (users.csv) geri kaydediyoruz
        saveAll(updatedUsers);
    }
}