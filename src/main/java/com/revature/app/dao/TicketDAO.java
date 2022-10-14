package com.revature.app.dao;

import com.revature.app.models.Ticket;

import java.util.List;

public interface TicketDAO {

    Ticket createTicket(Integer employeeId, Float amount, String description);

    List<Ticket> getAllTickets();

    List<Ticket> getTicketsByEmployeeId(int id);

    boolean updateTicket(int ticketId, String status, int resolvedby, String reason);

    List<Ticket> getPendingTickets();
}
