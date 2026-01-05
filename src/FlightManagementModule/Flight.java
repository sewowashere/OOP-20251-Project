package FlightManagementModule;
import dao.CSVConvertible;

public class Flight implements CSVConvertible {
    private int flightNum;
    private Route route;
    private String date;
    private float hour;
    private float duration;
    private Plane plane;
    
    private String departurePlace;
    private String arrivalPlace;
    
    public Flight() {} //instance olusturma icin bos constructor
    
    public Flight(int flightNum, String departurePlace, String arrivalPlace, String date) { //deneme amaclı yazdıgım constructor duzelecek
        this.flightNum = flightNum;
        this.departurePlace = departurePlace;
        this.arrivalPlace = arrivalPlace;
        this.date = date;
    }

    public Flight(int flightNum, String departurePlace, String arrivalPlace, String date, float hour, float duration, Plane plane, String transfer) {
        this.flightNum = flightNum;

        // Öncesinde nesneyi oluşturmalıyız:
        this.route = new Route(departurePlace, arrivalPlace);
        // Eğer aktarmalı uçuşsa;
        if (transfer != null && !transfer.isEmpty()){
            this.route.setTransferPlace(transfer);
        }

        this.date = date;
        this.hour = hour;
        this.duration = duration;
        this.plane = plane;
    }

    

	/**
     * Getters
     */
    public int getFlightNum() {
        return flightNum;
    }
    public String getDeparturePlace() {
        return route.getDeparturePlace();
    }
    public String getArrivalPlace() {
        return route.getArrivalPlace();
    }
    public String getDate() {
        return date;
    }
    public float getHour() {
        return hour;
    }
    public float getDuration() {
        return duration;
    }
    public Plane getPlane(){
        return plane;
    }





    /**
     * Boş koltuk sayısını veren yardımcı method.
     * GUI de işimize yarayabilir.
     */
    public int getAvailableSeatsCount() {
        int count = 0;
        Seat[][] matrix = plane.getSeatMatrix();
        for(Seat[] row : matrix){
            for(Seat seat : row){   //column
                if(!seat.isReserved()) {
                    count++;
                }
            }
        }
        return count;
    }

    
    // Uçağın iniş yapacağı zamanı hesaplar.
    public float durationCalculator() {
        // Saati doğru bir şekilde dakika formatına çevirme (int)
        int hourToMin = (int)hour*60 + Math.round((hour - (int)hour)*100);

        // Uçuş süresini doğr bir şekilde dakikaya çevirme (int)
        int durationToMin = (int)duration*60 + Math.round((duration - (int)duration)*100);

        int res = hourToMin + durationToMin;
        return (float)(res/60) + ((float)(res %60)/100);
    }
    
    @Override
    public String toCSV() {
        // Verileri virgülle ayırarak String yapıyoruz
        return flightNum + "," + departurePlace + "," + arrivalPlace + "," + date;
    }

    @Override
    public void fromCSV(String row) {
        String[] data = row.split(",");
        this.flightNum = Integer.parseInt(data[0]);
        this.departurePlace = data[1];
        this.arrivalPlace = data[2];
        this.date = data[3];
    }

    @Override
    public String getId() {
        return String.valueOf(this.flightNum);
    }

    // Getter ve Setter'lar...
    @Override
    public String toString() {
        return "Flight #" + flightNum + " [" + departurePlace + " -> " + arrivalPlace + "]";
    }
}
    




