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
    public boolean updateTicket(Ticket ticket) {
        return false;
    }
}
