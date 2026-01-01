package FlightManagementModule;

import java.util.HashMap;
import java.util.Map;

public class Plane {
    //private final int capacity;
    private final String planeID;
    private final PlaneModel model;
    private final Seat[][] seatMatrix;

    public Plane (PlaneModel model, String planeID, Seat[][] seatMatrix, float regularPrice){
        this.model = model;
        this.planeID = planeID;
        this.seatMatrix = new Seat[model.getRow()][model.getColumn()];    // Bunun setterı olmalı mı?


        initializeSeats(regularPrice);
    }

    private String getPlaneID() {
        return planeID;
    }
    private Seat[][] getSeatMatrix() {
        return seatMatrix;
    }



    // Bunun initialize fonksiyonu olmalı:
    public void  initializeSeats(float regularPrice) {
        char letter = 'A';
        for (int i = 0; i < model.getColumn(); i++){
            for (int ii = 1; ii < model.getRow() + 1; ii++) {

                Seat s;
                String currSeatNum = letter + "" + ii;

                if (ii <= 8) {
                    s = new Seat(currSeatNum, (float) (regularPrice * 1.5), Seat.SeatClass.BUSINESS);
                } else {
                    s = new Seat(currSeatNum, regularPrice, Seat.SeatClass.ECONOMY);
                }

                seatMatrix[ii][i] = s;
            }
            letter++;
        }
    }

}
