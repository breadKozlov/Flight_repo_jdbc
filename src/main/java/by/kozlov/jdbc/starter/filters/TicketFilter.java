package by.kozlov.jdbc.starter.filters;

import by.kozlov.jdbc.starter.entities.Flight;
import by.kozlov.jdbc.starter.entities.PassengerCountTickets;
import by.kozlov.jdbc.starter.entities.Ticket;
import by.kozlov.jdbc.starter.utils.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketFilter {

    private static final TicketFilter INSTANCE = new TicketFilter();


    private static final String FIND_ALL_TICKETS = """
            select t.passenger_name,t.seat_no,t.cost,t.flight_id,t.passport_no
            from public.ticket as t
            order by t.id;
            """;
    private static final String FIND_ALL_FLIGHTS = """
            select f.arrival_airport_code, f.arrival_date, f.departure_airport_code,
            f.departure_date,f.status from public.flight as f
            join public.airport as air on f.arrival_airport_code = air.code
            order by f.id;
            """;
    private static final String GET_MOST_COMMON_NAMES = """
            select t.passenger_name, count(*) from public.ticket as t
            group by t.passenger_name
            order by count desc
            limit 5;
            """;
    private static final String GET_NAMES_AND_COUNT_TICKETS = """
            select t.passenger_name, count(*) from public.ticket as t
            group by t.passenger_name,t.passport_no
            order by count desc;
            """;
    private static final String UPDATE_BY_ID = """
            update public.ticket
            set passenger_name = ?,
             passport_no = ?,
             cost = ?,
             flight_id = ?,
             seat_no = ?
             where id = ?
            """;
    private static final String UPDATE_COST = """
            update public.ticket
            set cost = cost * ?
            where flight_id = ?;
            """;
    private static final String UPDATE_STATUS_FLIGHT = """
            update public.flight
            set status = ??
            where id = ?;
            """;

    public List<String> getMostCommonNames() {
        List<String> names = new ArrayList<>();
        try(var connection = ConnectionManager.open();
        var statement = connection.createStatement()) {
            var result = statement.executeQuery(GET_MOST_COMMON_NAMES);
            while (result.next()) {
                names.add(result.getString("passenger_name"));
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return names;
    }

    public List<PassengerCountTickets> getPassengerCountTickets() {
        List<PassengerCountTickets> passengers = new ArrayList<>();
        try(var connection = ConnectionManager.open();
            var statement = connection.createStatement()) {
            var result = statement.executeQuery(GET_NAMES_AND_COUNT_TICKETS);
            while (result.next()) {
                passengers.add(new PassengerCountTickets(result.getString("passenger_name"),
                        result.getInt("count")));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return passengers;
    }

    public List<Ticket> findAllTickets() {
        List<Ticket> tickets = new ArrayList<>();
        try(var connection = ConnectionManager.open();
            var statement = connection.createStatement()) {
            var result = statement.executeQuery(FIND_ALL_TICKETS);
            while (result.next()) {
                tickets.add(new Ticket(result.getBigDecimal("cost"),
                        result.getInt("flight_id"),
                        result.getString("passenger_name"),
                        result.getString("passport_no"),
                        result.getString("seat_no")
                        ));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return tickets;
    }

    public boolean update(int id, Ticket ticket) {
        try(var connection = ConnectionManager.open();
            var statement = connection.prepareStatement(UPDATE_BY_ID)) {
            statement.setString(1,ticket.getPassengerName());
            statement.setString(2,ticket.getPassportNo());
            statement.setBigDecimal(3,ticket.getCost());
            statement.setInt(4,ticket.getFlightId());
            statement.setString(5,ticket.getSeatNo());
            statement.setInt(6,id);

            return statement.executeUpdate()>0;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean updateFlightStatusAndTicketCost(int idFlight, String status, Double costFactor) throws SQLException {
        Connection connection = null;
        try {
            connection = ConnectionManager.open();
            connection.setAutoCommit(false);
            updateStatusFlightByFlightId(connection, idFlight, status);
            updateTicketCostByFlightId(connection, idFlight, costFactor);
            connection.commit();
            System.out.println("commit");
        } catch (Exception ex) {
            if (connection != null) {
                connection.rollback();
                System.out.println("connection rollback");
                throw ex;
            }
        } finally {
            if (connection != null) connection.close();
            System.out.println("connection closed");
        }
        return true;
    }

    private boolean updateTicketCostByFlightId(Connection connection, int idFlight,Double costFactor) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_COST)) {
            statement.setDouble(1, costFactor);
            statement.setInt(2, idFlight);
            return statement.executeUpdate() > 0;
        }
    }

    private boolean updateStatusFlightByFlightId(Connection connection, int idFlight, String status) throws SQLException {
        try(var statement = connection.prepareStatement(UPDATE_STATUS_FLIGHT)) {
            statement.setString(1,status);
            statement.setInt(2,idFlight);
            return statement.executeUpdate()>0;
        }
    }

    public List<Flight> findAllFlights() {
        List<Flight> flights = new ArrayList<>();
        try(var connection = ConnectionManager.open();
            var statement = connection.createStatement()) {
            var result = statement.executeQuery(FIND_ALL_FLIGHTS);
            while (result.next()) {
                flights.add(new Flight(result.getString("arrival_airport_code"),
                        result.getString("departure_airport_code"),
                        result.getDate("arrival_date"),
                        result.getDate("departure_date"),
                        result.getString("status")
                ));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return flights;
    }

    private TicketFilter() {}

    public static TicketFilter getInstance() {
        return INSTANCE;
    }
}
