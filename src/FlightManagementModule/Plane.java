package FlightManagementModule;


public class Plane {
    private final int capacity;
    private final String planeID;
    private final PlaneModel model;
    private final Seat[][] seatMatrix;

    public Plane (PlaneModel model, String planeID, float regularPrice){
        this.model = model;
        this.planeID = planeID;
        this.seatMatrix = new Seat[model.getRow()][model.getColumn()];    // Bunun setterı olmalı mı?
        this.capacity = model.getRow() * model.getColumn();

        initializeSeats(regularPrice);
    }

    /**
     * Uçak koltuklarını businnes ve economy olarak ayırır ve isimlendirir.
     * Dökümanda 1 uçak 30 satır-6 sütun olarak verilmiş.
     */
    public void  initializeSeats(float regularPrice) {
        char letter = 'A';
        for (int i = 0; i < model.getColumn(); i++){
            for (int ii = 0; ii < model.getRow(); ii++) {

                Seat s;
                String currSeatNum = ii + "" + letter;

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


    public String getPlaneID() {
        return this.planeID;
    }
    public Seat[][] getSeatMatrix() {
        return seatMatrix;
    }
    public int getCapacity() {
        return this.capacity;
    }
    public PlaneModel getModel() {
        return model;
    }

    public String toCSV() {
        // Not: regularPrice bilgisini Seat nesneleri üzerinden veya
        // sistem genelinden aldığın için burada temel yapı yeterlidir.
        return planeID + "," + model.getModelName() + "," + model.getRow() + "," + model.getColumn();
    }
}
