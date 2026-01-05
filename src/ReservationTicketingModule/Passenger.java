package ReservationTicketingModule;

public class Passenger {
    private String passengerID;
    private String name;
    private String surname;
    private String contactInfo;

    public Passenger(String passengerID, String name, String surname, String contactInfo) {
        this.passengerID = passengerID;
        this.name = name;
        this.surname = surname;
        this.contactInfo = contactInfo;
    }

    public String getPassengerID() {
        return this.passengerID;
    }
    public String getName() {
        return this.name;
    }
    public String getSurname() {
        return this.surname;
    }
    public String getContactInfo() {
        return this.contactInfo;
    }

    public String toCSV() {
        return passengerID + "," +
               name + "," +
               surname + "," +
               contactInfo + ",";
    }
}
