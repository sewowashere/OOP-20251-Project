package FlightManagementModule;

public class Flight {
    private int flightNum;
    private Route route;
    private String date;
    private String hour;
    private float duration;

    public Flight(int flightNum, String departurePlace, String arrivalPlace, String date, String hour, float duration) {
        this.flightNum = flightNum;
        this.route.setArrivalPlace(arrivalPlace);
        this.route.setDeparturePlace(departurePlace);
        this.date = date;
        this.hour = hour;
        this.duration = duration;
    }

    //
    // Getters
    //
    public int getFlightNum() {
        return flightNum;
    }
    public String getDeparturePlace() {
        return departurePlace;
    }
    public String getArrivalPlace() {
        return arrivalPlace;
    }
    public String getDate() {
        return date;
    }
    public String getHour() {
        return hour;
    }
    public float getDuration() {
        return duration;
    }

    // Verileri dosyaya ayzmayı kolaylaştırmak için CSV formatı
    public String toFileString() {
        return flightNum+","+departurePlace+","+arrivalPlace+","+date+","+hour+","+duration;
    }

}
