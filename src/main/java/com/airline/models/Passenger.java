package com.airline.models;

import com.airline.core.dao.CSVConvertible;

public class Passenger implements CSVConvertible {
    private String passengerID;
    private String name;
    private String surname;
    private String contactInfo;

    public Passenger() {}
    public Passenger(String id, String name, String surname, String contact) {
        this.passengerID = id;
        this.name = name;
        this.surname = surname;
        this.contactInfo = contact;
    }

    @Override public String toCSV() { return passengerID + "," + name + "," + surname + "," + contactInfo; }
    @Override public void fromCSV(String row) {
        String[] d = row.split(",");
        this.passengerID = d[0]; this.name = d[1]; this.surname = d[2]; this.contactInfo = d[3];
    }
    @Override public String getId() { return passengerID; }
    // Getters...
	public String getPassengerID() {
		return passengerID;
	}
	public String getName() {
		return name;
	}
	public String getSurname() {
		return surname;
	}
	public String getContactInfo() {
		return contactInfo;
	}
    
}
