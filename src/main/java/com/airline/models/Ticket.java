package com.airline.models;

public class Ticket {
    private String ticketID;
    private double price;
    private Baggage baggage;

    public Ticket(String ticketID, double price, Baggage baggage) {
        this.ticketID = ticketID;
        this.price = price;
        this.baggage = baggage;
    }
    // Getters...

	public String getTicketID() {
		return ticketID;
	}

	public double getPrice() {
		return price;
	}

	public Baggage getBaggage() {
		return baggage;
	}
    
    
}
