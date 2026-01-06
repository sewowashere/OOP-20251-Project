package FlightManagementModule;
import dao.CSVConvertible;


public class Plane implements CSVConvertible{
    private int capacity;
    private String planeID;
    private  String model;
    private  Seat[][] seatMatrix;

    /*public Plane (PlaneModel model, String planeID, float regularPrice){
        this.model = model;
        this.planeID = planeID;
        this.seatMatrix = new Seat[model.getRow()][model.getColumn()];    // Bunun setterı olmalı mı?
        this.capacity = model.getRow() * model.getColumn();

        initializeSeats(regularPrice);
    }*/
    
    public Plane() {
		this.capacity = 0;
		this.planeID = "";
		this.model = null;
		this.seatMatrix = null;}
    
    public Plane(String planeID, String planeModel, int capacity) {
        this.planeID = planeID;
 
        this.capacity = capacity;
		this.model = null;
		this.seatMatrix = null;
    }

    /**
     * Uçak koltuklarını businnes ve economy olarak ayırır ve isimlendirir.
     * Dökümanda 1 uçak 30 satır-6 sütun olarak verilmiş.
     */
   /* public void  initializeSeats(float regularPrice) {
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
    }*/


    public String getPlaneID() {
        return this.planeID;
    }
    public Seat[][] getSeatMatrix() {
        return seatMatrix;
    }
    public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public void setPlaneID(String planeID) {
		this.planeID = planeID;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void setSeatMatrix(Seat[][] seatMatrix) {
		this.seatMatrix = seatMatrix;
	}

	public int getCapacity() {
        return this.capacity;
    }
    public String getModel() {
        return model;
    }
    @Override
    public String toCSV() {
        return planeID + "," + model + "," + capacity;
    }

    @Override
    public void fromCSV(String row) {
        String[] data = row.split(",");
        this.planeID = data[0];
        this.model = data[1];
        this.capacity = Integer.parseInt(data[2]);
    }

    @Override
    public String getId() { return planeID; }
    
}
   
