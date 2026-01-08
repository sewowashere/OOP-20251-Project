package com.airline.core.dao;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCSVDAO<T extends CSVConvertible, ID> implements GenericDAO<T, ID> {
    protected final String filePath;

    public AbstractCSVDAO(String filePath) {
        this.filePath = filePath;
        ensureFileExists();
    }

    private void ensureFileExists() {
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Dosya oluşturulamadı: " + e.getMessage());
        }
    }

    // Alt sınıflar boş bir nesne örneği dönmek zorundadır (örn: return new Flight();)
    protected abstract T createInstance();

    @Override
    public void save(T entity) {
        try (PrintWriter out = new PrintWriter(new FileWriter(filePath, true))) {
            out.println(entity.toCSV());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<T> getAll() {
        List<T> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                T entity = createInstance();
                entity.fromCSV(line);
                list.add(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void delete(ID id) {
        List<T> all = getAll();
        // ID'si eşleşmeyenleri filtreleyip dosyayı yeniden yazıyoruz
        boolean removed = all.removeIf(item -> item.getId().equals(id.toString()));
        
        if (removed) {
            rewriteFile(all);
        }
    }

    @Override
    public void update(T entity) {
        List<T> all = getAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId().equals(entity.getId())) {
                all.set(i, entity);
                break;
            }
        }
        rewriteFile(all);
    }

    @Override
    public T findById(ID id) {
        return getAll().stream()
                .filter(item -> item.getId().equals(id.toString()))
                .findFirst()
                .orElse(null);
    }

    private void rewriteFile(List<T> list) {
        try (PrintWriter out = new PrintWriter(new FileWriter(filePath, false))) {
            for (T item : list) {
                out.println(item.toCSV());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveAll(List<T> entities) {
        rewriteFile(entities);
        // Mevcut rewriteFile metodunu kullanarak listeyi dosyaya yazar
    }
}
