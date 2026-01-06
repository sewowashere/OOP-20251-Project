package com.airline.persistence;

import com.airline.core.dao.AbstractCSVDAO;
import com.airline.models.Plane;

public class PlaneDAOImpl extends AbstractCSVDAO<Plane, String> {
    public PlaneDAOImpl() { super("planes.csv"); }
    @Override
    protected Plane createInstance() { return new Plane(); }
}
