package FlightManagementModule;

public class Seat {
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
     */
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
    }
}
