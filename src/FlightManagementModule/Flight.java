package FlightManagementModule;

public class Flight {
    private int flightNum;
    private Route route;
    private String date;
    private float hour;
    private float duration;
    private Plane plane;

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

    // Verileri dosyaya ayzmayı kolaylaştırmak için CSV formatı
    public String toCSV() {
        return flightNum+","+route.getDeparturePlace()+","+route.getArrivalPlace()+","+date+","+hour+","+duration+","+plane.getPlaneID();
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
}


