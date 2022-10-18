package com.revature.app.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.app.models.Employee;
import com.revature.app.models.Ticket;
import com.revature.app.service.TicketService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public class TicketServlet extends HttpServlet {

    public final ObjectMapper mapper;
    public final TicketService ts;

    public TicketServlet(ObjectMapper mapper, TicketService ts) {
        this.mapper = mapper;
        this.ts = ts;
    }

    @Override
    public void init() throws ServletException {
        System.out.println("[LOG] - Servlet instantiated");
        System.out.println("[LOG] - Servlet init param: "+ this.getServletConfig().getInitParameter("keygoeshere"));
        System.out.println("[LOG] - Servlet context param: " + this.getServletContext().getInitParameter("testvalueforallservlets"));
    }

    @Override
    public void destroy() {
        System.out.println("[LOG] - Server destroyed");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.setStatus(401);
            resp.setContentType("application/json");


            HashMap<String, Object> errorMessage = new HashMap<>();

            errorMessage.put("Status code", 401);
            errorMessage.put("Message", "Must be logged in to interact with tickets.");
            errorMessage.put("Timestamp", LocalDateTime.now().toString());

            resp.getWriter().write(mapper.writeValueAsString(errorMessage));
            return;
        }
        Employee info = (Employee) session.getAttribute("current-user");
        if(req.getParameter("action").equals("GETPENDING")) {
            //GET the list of PENDING tickets - for managers.

            if (!info.isManager()) {
                resp.setStatus(403);
                resp.setContentType("application/json");


                HashMap<String, Object> errorMessage = new HashMap<>();

                errorMessage.put("Status code", 403);
                errorMessage.put("Message", "Must be a manager to access this feature.");
                errorMessage.put("Timestamp", LocalDateTime.now().toString());

                resp.getWriter().write(mapper.writeValueAsString(errorMessage));
                return;
            }
            List<Ticket> result = ts.getPendingTickets();
            resp.setStatus(200);
            resp.setContentType("application/json");


            resp.getWriter().write(mapper.writeValueAsString(result));// TODO not tested yet. same with Update Ticket (doPut)
        } else if (req.getParameter("action").equals("SEEPREVIOUS")){ // TODO can directly get params with req.getParam("action").equals("SEEPREVIOUS");
            //See your previously submitted tickets along with relevant info - for all users.
            List<Ticket> result = ts.getTickets(info.getEmployeeId());
            if(result != null){
                resp.setStatus(200);
                resp.setContentType("application/json");


                resp.getWriter().write(mapper.writeValueAsString(result));
            } else {
                resp.setStatus(400);
                resp.setContentType("application/json");
                HashMap<String, Object> errorMessage = new HashMap<>();

                errorMessage.put("Status code", 400);
                errorMessage.put("Message", "This user hasn't made any requests!");
                errorMessage.put("Timestamp", LocalDateTime.now());
                resp.getWriter().write(mapper.writeValueAsString(errorMessage));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    //CREATE a ticket.
        HttpSession session = req.getSession(false);

        if (session == null){
            resp.setStatus(401);
            resp.setContentType("application/json");


            HashMap<String, Object> errorMessage = new HashMap<>();

            errorMessage.put("Status code", 401);
            errorMessage.put("Message", "Must be logged in to submit a ticket.");
            errorMessage.put("Timestamp", LocalDateTime.now().toString());

            resp.getWriter().write(mapper.writeValueAsString(errorMessage));
            return;
        }
        Employee info = (Employee) session.getAttribute("current-user");
        HashMap<String, String> ticketInfo = mapper.readValue(req.getInputStream(), HashMap.class);
        if(!ticketInfo.containsKey("amount") || !ticketInfo.containsKey("description")
                || ticketInfo.get("amount") == null || ticketInfo.get("description").trim().equals("")){
            resp.setStatus(400);
            resp.setContentType("application/json");
            HashMap<String, Object> errorMessage = new HashMap<>();
            errorMessage.put("Status code", 400);
            errorMessage.put("Message", "ticket must have amount and description fields.");
            errorMessage.put("Timestamp", LocalDateTime.now().toString());
            resp.getWriter().write(mapper.writeValueAsString(errorMessage));
            return;
        }
        System.out.println(session.getAttribute("current-user"));
        System.out.println(session.getAttribute("current-user").getClass());
        System.out.println(ticketInfo);
        System.out.println(ticketInfo.get("amount"));

        Ticket result = ts.createTicket(info.getEmployeeId(), Float.parseFloat(ticketInfo.get("amount")), ticketInfo.get("description"));

        if(result != null){
            resp.setStatus(200);
            resp.setContentType("application/json");
            HashMap<String, Object> message = new HashMap<>();
            message.put("Status code", 200);
            message.put("Message", "Successfully submitted ticket!");
            message.put("Timestamp", LocalDateTime.now().toString());
            message.put("New ticket", result);

            resp.getWriter().write(mapper.writeValueAsString(message));
            return;
        }
        resp.setStatus(400);
        resp.setContentType("application/json");
        HashMap<String, Object> errorMessage = new HashMap<>();
        errorMessage.put("Status code", 400);
        errorMessage.put("Message", "ticket must have amount and description fields.");
        errorMessage.put("Timestamp", LocalDateTime.now().toString());
        resp.getWriter().write(mapper.writeValueAsString(errorMessage));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //APPROVE or DENY a ticket - for managers.
        HttpSession session = req.getSession(false); // TODO make the return value equal to the pendingtickets list.


        if (session == null){
            resp.setStatus(400);
            resp.setContentType("application/json");


            HashMap<String, Object> errorMessage = new HashMap<>();

            errorMessage.put("Status code", 400);
            errorMessage.put("Message", "Must be logged in to interact with ticket.");
            errorMessage.put("Timestamp", LocalDateTime.now().toString());

            resp.getWriter().write(mapper.writeValueAsString(errorMessage));
            return;
        }
        Employee info = (Employee) session.getAttribute("current-user");
        HashMap<String, String> ticketInfo = mapper.readValue(req.getInputStream(), HashMap.class);
        if (!info.isManager()){
            resp.setStatus(400);
            resp.setContentType("application/json");


            HashMap<String, Object> errorMessage = new HashMap<>();

            errorMessage.put("Status code", 400);
            errorMessage.put("Message", "Must be a manager to accept or deny ticket.");
            errorMessage.put("Timestamp", LocalDateTime.now().toString());

            resp.getWriter().write(mapper.writeValueAsString(errorMessage));
            return;
        }
        else if (
                !ticketInfo.containsKey("ticketId") || !ticketInfo.containsKey("reason") ||
                        ticketInfo.get("ticketId") == null || ticketInfo.get("reason").trim().equals("") ||
                        (!ticketInfo.get("status").equals("APPROVED") && !ticketInfo.get("status").equals("DENIED"))){
            resp.setStatus(400);
            resp.setContentType("application/json");


            HashMap<String, Object> errorMessage = new HashMap<>();

            errorMessage.put("Status code", 400);
            errorMessage.put("Message", "Invalid ticket information.");
            errorMessage.put("Timestamp", LocalDateTime.now().toString());

            resp.getWriter().write(mapper.writeValueAsString(errorMessage));
            return;
        }
        Boolean result = ts.updateTicket(Integer.parseInt(ticketInfo.get("ticketId")),ticketInfo.get("status"),info.getEmployeeId(),ticketInfo.get("reason"));
        if(result){
            resp.setStatus(200);
            resp.setContentType("application/json");
            HashMap<String, Object> message = new HashMap<>();
            message.put("Timestamp", LocalDateTime.now().toString());
            message.put("Message", "Successfully " + ticketInfo.get("status") + " request number " + ticketInfo.get("ticketId"));
            List<Ticket> res = ts.getPendingTickets();
            message.put("Remaining PENDING tickets", res);

            resp.getWriter().write(mapper.writeValueAsString(message));
            return;
        } else{
            resp.setStatus(400);
            resp.setContentType("application/json");


            HashMap<String, Object> errorMessage = new HashMap<>();

            errorMessage.put("Status code", 400);
            errorMessage.put("Message", "Can't change ticket result.");
            errorMessage.put("Timestamp", LocalDateTime.now().toString());

            resp.getWriter().write(mapper.writeValueAsString(errorMessage));
            return;
        }
    }
}
