package FlightManagementModule;

public class Seat {
    private String seatNum; // example="15A"
    private int price;
    private boolean reserveStatus;


    public enum SeatClass {
        BUSINESS,
        ECONOMY;
    }

}
