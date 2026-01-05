package dao;

import FlightManagementModule.Flight;

public class FlightDAOImpl extends AbstractCSVDAO<Flight, Integer> {
    
    public FlightDAOImpl() {
        // Verilerin saklanacağı dosya adı
        super("flights.csv");
    }

    @Override
    protected Flight createInstance() {
        // DAO dosyadan satır okuduğunda içi boş bir Flight üretip fromCSV'yi çağıracak
        return new Flight();
    }
}
