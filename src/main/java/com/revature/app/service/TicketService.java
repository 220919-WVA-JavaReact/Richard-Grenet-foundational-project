package com.revature.app.service;

import com.revature.app.dao.TicketDAO;
import com.revature.app.dao.TicketDAOimpl;
import com.revature.app.models.Ticket;

import java.util.List;

public class TicketService {

    TicketDAO td = new TicketDAOimpl();

    public List<Ticket> getTickets(int id){
        return td.getTicketsByEmployeeId(id);
    }

    public Ticket createTicket(int empId, float amount, String description){
        return td.createTicket(empId,amount,description);
    }

    public boolean updateTicket(int ticketId, String status, int resolvedby, String reason){
        return td.updateTicket(ticketId, status, resolvedby, reason);
    }

    public List<Ticket> getPendingTickets() { return td.getPendingTickets();}

}
