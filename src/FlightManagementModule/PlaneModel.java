package FlightManagementModule;

public class PlaneModel {
    private String modelName;
    private int row;
    private int column;

    public PlaneModel(String modelName, int row, int column) {
        this.modelName = modelName;
        this.row = row;
        this.column = column;
    }

    public int capacityCalculator() {
        return row * column;
    }

    public int getRow() {
        return this.row;
    }
    public int getColumn() {
        return this.column;
    }
    public int calculateCapacity() {
        return this.row * this.column;
    }
}
