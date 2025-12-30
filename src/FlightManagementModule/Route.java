package FlightManagementModule;

public class Route {
    //
    // Departure - Arrival Information
    // If plane isn't directly goes to the destination area.
    //
    private String departurePlace;
    private String arrivalPlace;
    private String transferPlace;


    public Route(String departure, String arrival, String transfer) {
        this.departurePlace = departure;
        this.arrivalPlace = arrival;
        this.transferPlace = transfer;
    }

    //
    // Getter - Setters
    //
    public String getDeparturePlace(){
        return this.departurePlace;
    }
    public String getArrivalPlace() {
        return this.arrivalPlace;
    }
    public String getTransferPlace() {
        return this.transferPlace;
    }

    public void setDeparturePlace(String departurePlace) {
        this.departurePlace = departurePlace;
    }
    public void setArrivalPlace(String arrivalPlace) {
        this.arrivalPlace = arrivalPlace;
    }
    public void setTransferPlace(String transferPlace) {
        this.transferPlace = transferPlace;
    }

}
