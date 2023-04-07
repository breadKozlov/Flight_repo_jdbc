package by.kozlov.jdbc.starter.entities;

public record PassengerCountTickets(String name, int countTickets) {

    @Override
    public String toString() {
        return "Passenger{" +
                "name='" + name + '\'' +
                ", count of tickets=" + countTickets +
                '}';
    }
}
