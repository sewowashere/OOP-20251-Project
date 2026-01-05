package dao;

import FlightManagementModule.Plane;

public class PlaneDAOImpl extends AbstractCSVDAO<Plane, String> {
    public PlaneDAOImpl() { super("planes.csv"); }
    @Override
    protected Plane createInstance() { return new Plane(); }
}
