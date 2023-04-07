package by.kozlov.jdbc.starter.entities;

import java.math.BigDecimal;

public class Ticket {

    private BigDecimal cost;
    private int flightId;
    private String passengerName;
    private String passportNo;
    private String seatNo;

    public Ticket(BigDecimal cost, int flightId, String passengerName, String passportNo, String seatNo) {
        this.cost = cost;
        this.flightId = flightId;
        this.passengerName = passengerName;
        this.passportNo = passportNo;
        this.seatNo = seatNo;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "cost=" + cost +
                ", flightId=" + flightId +
                ", passengerName='" + passengerName + '\'' +
                ", passportNo='" + passportNo + '\'' +
                ", seatNo='" + seatNo + '\'' +
                '}';
    }
}
