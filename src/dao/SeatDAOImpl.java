package dao;

import java.util.List;

import FlightManagementModule.Seat;

public class SeatDAOImpl extends AbstractCSVDAO<Seat, String> {
    public SeatDAOImpl() { super("seats.csv"); }
    @Override
    protected Seat createInstance() { return new Seat(); }
    
    // Seat için özel bir metot ekleyebiliriz: Belirli bir uçağın koltuklarını getir
    public List<Seat> getSeatsByPlane(String planeID) {
        return getAll().stream()
                .filter(s -> s.getPlaneID().equals(planeID))
                .toList();
    }
}
