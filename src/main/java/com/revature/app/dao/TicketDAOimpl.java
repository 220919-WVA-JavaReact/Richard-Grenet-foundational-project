package com.revature.app.dao;

import com.revature.app.models.Ticket;
import com.revature.app.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAOimpl implements TicketDAO{
    @Override
    public Ticket createTicket(Integer employeeId, Float amount, String description) {
        //Should return ticket to use in a statement after to confirm.
        try (Connection conn = ConnectionUtil.getConnection()){
            String sql = "INSERT INTO tickets (employee_id, amount, description) VALUES (?,?,?) RETURNING *";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1,employeeId);
            stmt.setFloat(2,amount);
            stmt.setString(3,description);

            ResultSet rs;

            if((rs = stmt.executeQuery())!=null){
                //need to call rs.next() somehow.
                rs.next();
                int id = rs.getInt("ticket_id");
                int empId = rs.getInt("employee_id");
                float amnt = rs.getFloat("amount");
                String descrip = rs.getString("description");
                String status = rs.getString("status");
                Ticket ticket = new Ticket(id,empId,amount,descrip,status);
                return ticket;
            }
        }   catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Ticket> getAllTickets() {
        //SELECT * FROM tickets
        //First get a connection to database
        Connection conn = ConnectionUtil.getConnection();
        //create statement
        try {
            Statement stat = conn.createStatement();
            String sql = "SELECT * FROM tickets";
            ResultSet rs = stat.executeQuery(sql);

            // to store all tickets, create an empty list and store info inside.
            List<Ticket> tickets = new ArrayList<>();
            while(rs.next()){
                int id = rs.getInt("ticket_id");
                int empId = rs.getInt("employee_id");
                float amount = rs.getFloat("amount");
                String descrip = rs.getString("description");
                String status = rs.getString("status");
                Ticket ticket = new Ticket(id,empId,amount,descrip,status);
                tickets.add(ticket);
            }
            return tickets;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Ticket> getTicketsByEmployeeId(int id) {
        //using prepared statement
        List<Ticket> tickets = new ArrayList<>();
        try( Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT * FROM tickets WHERE employee_id = ?";
            //? goes where info goes
            PreparedStatement stat = conn.prepareStatement(sql);
            //set individual values for ? makes
            stat.setInt(1, id);
            ResultSet rs;
            if((rs = stat.executeQuery()) != null){

                while(rs.next()){
                    int tId = rs.getInt("ticket_id");
                    int empId = rs.getInt("employee_id");
                    float amount = rs.getFloat("amount");
                    String descrip = rs.getString("description");
                    String status = rs.getString("status");
                    Ticket ticket = new Ticket(tId,empId,amount,descrip,status);
                    tickets.add(ticket);
                }
                return tickets;
            }

        } catch(SQLException e){
            e.printStackTrace();
        }

        return tickets;
    }

    @Override
    public boolean updateTicket(int ticketId, String status, int resolvedby, String reason) {
        try (Connection conn = ConnectionUtil.getConnection()){
            String checkPending = "SELECT * FROM tickets WHERE ticket_id = ?";
            PreparedStatement statCheck = conn.prepareStatement(checkPending);
            statCheck.setInt(1,ticketId);
            ResultSet rsC;
            if((rsC = statCheck.executeQuery()) != null){
                String statusPre = rsC.getString("status");
                if(statusPre.equals("DENIED") || statusPre.equals("APPROVED")) return false;
            }

            String sql = "UPDATE tickets SET status=?, resolvedby=?, reason=? WHERE ticket_id = ?";

            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setString(1,status);
            stat.setInt(2,1); // TODO need to either code in resolvedby field to java, remove from sql, or otherwise decide what to do with it.
            stat.setString(3,"reasongoeshere"); // TODO same for reason...
            stat.setInt(4, ticketId);
            int rs;
            if((rs = stat.executeUpdate()) == 1){
                return true;
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Ticket> getPendingTickets() {
        List<Ticket> tickets = new ArrayList<>();
        try( Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT * FROM tickets WHERE status = PENDING";
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(sql);

                while(rs.next()){
                    int tId = rs.getInt("ticket_id");
                    int empId = rs.getInt("employee_id");
                    float amount = rs.getFloat("amount");
                    String descrip = rs.getString("description");
                    String status = rs.getString("status");
                    Ticket ticket = new Ticket(tId,empId,amount,descrip,status);
                    tickets.add(ticket);
                }
                tickets.sort((t,t2) -> t.getTicketId() - t2.getTicketId());
                return tickets;


        } catch(SQLException e){
            e.printStackTrace();
        }

        return tickets;
    }
}
