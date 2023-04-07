package by.kozlov.jdbc.starter.utils;

import by.kozlov.jdbc.starter.entities.PassengerCountTickets;
import by.kozlov.jdbc.starter.entities.Ticket;
import by.kozlov.jdbc.starter.filters.TicketFilter;

import java.math.BigDecimal;
import java.sql.SQLException;

public class JdbcRunner {
    public static void main(String[] args) throws SQLException {

        TicketFilter ticketFilter = TicketFilter.getInstance();

        //get most common names
        for (String names: ticketFilter.getMostCommonNames()){
            System.out.println(names + ";");
        }

        System.out.println("-----------------------------");

        //get all passenger and their tickets from all the time
        for (PassengerCountTickets pass: ticketFilter.getPassengerCountTickets()) {
            System.out.println(pass.name() + " - " + pass.countTickets());
        }

        System.out.println("-----------------------------");

        //get all tickets
        System.out.println(ticketFilter.findAllTickets());

        System.out.println("-----------------------------");

        //update ticket data by flight id
        System.out.println(ticketFilter.update(3,new Ticket(new BigDecimal(200),2,"Pasha Kozlov",
                "123548","B1")));

        System.out.println("-----------------------------");

        System.out.println(ticketFilter.findAllTickets());

        System.out.println("-----------------------------");

        //get all flights
        System.out.println(ticketFilter.findAllFlights());

        System.out.println("-----------------------------");

        //update status flight and cost ticket by flight id
        System.out.println(ticketFilter.updateFlightStatusAndTicketCost(1,"SCHEDULED",0.5));

        System.out.println("-----------------------------");

        System.out.println(ticketFilter.findAllFlights());

        System.out.println("-----------------------------");

        System.out.println(ticketFilter.findAllTickets());

    }
}
