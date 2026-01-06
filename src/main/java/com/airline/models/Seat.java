package com.airline.models;

import com.airline.core.dao.CSVConvertible;

public class Seat implements CSVConvertible {/*
    private final String seatNum; // example="15A"
    private float price;
    private boolean reserveStatus;
    private SeatClass seatClass;

    public enum SeatClass {
        BUSINESS, //0
        ECONOMY;  //1
    }

    public Seat(String seatNum, float regularPrice, SeatClass seatClass) {
        this.seatNum = seatNum;
        this.reserveStatus = false;

        if (seatClass == SeatClass.ECONOMY) {
            price = regularPrice;
        } else if (seatClass == SeatClass.BUSINESS) {
            price = (float) (regularPrice * 1.5);
        }
    }

    /**
     * Getters
     
    public String getSeatNum() {
        return this.seatNum;
    }
    public float getPrice() {
        return this.price;
    }
    public boolean isReserved() {
        return this.reserveStatus;
    }
    //JUnit testi için seat nesnesi oluşturduğunda getSeatClass()'ın doğru dönüp dönmediğini test edebiliriz.
    public SeatClass getSeatClass() {
        return this.seatClass;
    }

    public void setReserveStatus(boolean reserveStatus) {
        this.reserveStatus = reserveStatus;
    }

    public String toCSV() {
        return seatNum + "," + seatClass + "," + price + "," + reserveStatus;
    }*/
	private String seatNum; // "15A"
    public String getSeatNum() {
		return seatNum;
	}
	public void setSeatNum(String seatNum) {
		this.seatNum = seatNum;
	}
	public String getPlaneID() {
		return planeID;
	}
	public void setPlaneID(String planeID) {
		this.planeID = planeID;
	}
	public SeatClass getSeatClass() {
		return seatClass;
	}
	public void setSeatClass(SeatClass seatClass) {
		this.seatClass = seatClass;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public boolean isReserved() {
		return isReserved;
	}
	public void setReserved(boolean isReserved) {
		this.isReserved = isReserved;
	}

	private String planeID; // Hangi uçağa ait olduğu bilgisi (Foreign Key mantığı)
    private SeatClass seatClass; // Enum: ECONOMY, BUSINESS
    private double price;
    private boolean isReserved;

    public enum SeatClass { ECONOMY, BUSINESS }

    public Seat() {}
    public Seat(String seatNum, String planeID, SeatClass seatClass, double price, boolean isReserved) {
        this.seatNum = seatNum;
        this.planeID = planeID;
        this.seatClass = seatClass;
        this.price = price;
        this.isReserved = isReserved;
    }

    @Override
    public String toCSV() {
        return seatNum + "," + planeID + "," + seatClass + "," + price + "," + isReserved;
    }

    @Override
    public void fromCSV(String row) {
        String[] data = row.split(",");
        this.seatNum = data[0];
        this.planeID = data[1];
        this.seatClass = SeatClass.valueOf(data[2]);
        this.price = Double.parseDouble(data[3]);
        this.isReserved = Boolean.parseBoolean(data[4]);
    }

    @Override
    public String getId() { return planeID + "-" + seatNum; } // Bileşik ID
    // Getters and Setters...
}
	
	

