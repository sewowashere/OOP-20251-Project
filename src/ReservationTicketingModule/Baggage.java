package ReservationTicketingModule;

public class Baggage {
    private double weight;
    // Bilet tipine göre ek kurallar buraya gelebilir
    public Baggage(double weight) { this.weight = weight; }
    public double getWeight() { return weight; }
}
