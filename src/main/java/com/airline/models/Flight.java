package com.airline.models;
import com.airline.core.dao.CSVConvertible;

public class Flight implements CSVConvertible {
    private int flightNum;
    private Route route; // Lokasyon bilgileri artık sadece burada duruyor [cite: 9, 12]
    private String date;
    private float hour;
    private float duration;
    private Plane plane;

    public Flight() {}

    public Flight(int flightNum, String departurePlace, String arrivalPlace, String date) {
        this.flightNum = flightNum;
        this.route = new Route(departurePlace, arrivalPlace); // Route nesnesi oluşturuluyor [cite: 9]
        this.date = date;
    }

    public Flight(int flightNum, String departurePlace, String arrivalPlace, String date, float hour, float duration, Plane plane, String transfer) {
        this.flightNum = flightNum;
        this.route = new Route(departurePlace, arrivalPlace);
        if (transfer != null && !transfer.isEmpty()){
            this.route.setTransferPlace(transfer);
        }
        this.date = date;
        this.hour = hour;
        this.duration = duration;
        this.plane = plane;
    }

    // Getters
    public int getFlightNum() { return flightNum; }
    public String getDate() { return date; }
    public float getHour() { return hour; }
    public float getDuration() { return duration; }

    // REDUNDANCY ÇÖZÜMÜ: Bilgiyi doğrudan Route nesnesinden alıyoruz
    public String getDeparturePlace() {
        return route != null ? route.getDeparturePlace() : "";
    }
    public String getArrivalPlace() {
        return route != null ? route.getArrivalPlace() : "";
    }

    // Setters
    public void setRoute(Route route) { this.route = route; }
    public void setHour(float hour) { this.hour = hour; }
    public void setDuration(float duration) { this.duration = duration; }

    @Override
    public String toCSV() {
        // departurePlace ve arrivalPlace artık route üzerinden çekiliyor
        return flightNum + "," + getDeparturePlace() + "," + getArrivalPlace() + "," + date + "," + hour + "," + duration;
    }

    @Override
    public void fromCSV(String row) {
        String[] data = row.split(",");
        this.flightNum = Integer.parseInt(data[0]);
        // Nesne yapısını kurmak için Route nesnesini dolduruyoruz
        this.route = new Route(data[1], data[2]);
        this.date = data[3];
        this.hour = (data.length > 4) ? Float.parseFloat(data[4]) : 0.0f;
        this.duration = (data.length > 5) ? Float.parseFloat(data[5]) : 0.0f;
    }

    @Override
    public String getId() { return String.valueOf(this.flightNum); }

    @Override
    public String toString() {
        return flightNum + "," + getDeparturePlace() + "," + getArrivalPlace() + "," + date + "," + hour + "," + duration + "," + (plane != null ? plane.getPlaneID() : "NULL");
    }

    // Flight.java içine bu metodun olduğundan emin ol
    public String getFormattedHour() {
        int hours = (int) hour;
        int minutes = Math.round((hour - hours) * 100);
        return String.format("%02d:%02d", hours, minutes);
    }

    public Plane getPlane() {
        return getPlane();
    }
}