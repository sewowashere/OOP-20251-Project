package com.airline.core.dao;

public interface CSVConvertible {
    /**
     * Nesneyi CSV formatında bir String'e dönüştürür (örn: "101,IST,JFK,2024-12-01")
     */
    String toCSV();

    /**
     * CSV'den okunan bir satırı nesnenin alanlarına doldurur
     */
    void fromCSV(String row);

    /**
     * Nesnenin benzersiz kimliğini (ID) döner (Generic silme/güncelleme için şart)
     */
    String getId();
}
