package com.revature.app.dao;

import com.revature.app.models.Employee;
import com.revature.app.models.Ticket;
import com.revature.app.util.ConnectionUtil;

import javax.xml.transform.Result;
import java.sql.*;

public class EmployeeDAOimpl implements EmployeeDAO {
    @Override
    public Employee getByUsername(String username) {


        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM employees WHERE username = ?";
            //? goes where info goes
            PreparedStatement stat = conn.prepareStatement(sql);
            //set individual values for ? makes
            stat.setString(1, username);
            ResultSet rs = stat.executeQuery();
            if (rs.next()) {
                int empId = rs.getInt("employee_id");
                String frst = rs.getString("first");
                String lst = rs.getString("last");
                String usrname = rs.getString("username");
                String passwrd = rs.getString("password");
                Boolean managr = rs.getBoolean("manager");
                Employee employee = new Employee(empId, frst, lst, usrname, passwrd, managr);

//                System.out.println(employee.toString());

                return employee;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public Employee createEmployee(String first, String last, String username, String password, Boolean manager) {


        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM employees WHERE username = ?";
            //? goes where info goes
            PreparedStatement stat = conn.prepareStatement(sql);
            //set individual values for ? makes
            stat.setString(1, username);
            System.out.println("[LOG] - Executing query to see if username is not already taken...");
            ResultSet rs = stat.executeQuery();

            System.out.println("[LOG] - Result of query is: " + rs.toString());
            if (!rs.next()) {
                stat.close();
                String sql2 = "INSERT INTO employees (\"first\", \"last\", username, \"password\", manager)" +
                        "VALUES (?,?,?,?,?)";
                PreparedStatement stat2 = conn.prepareStatement(sql2);
                stat2.setString(1, first);
                stat2.setString(2, last);
                stat2.setString(3, username);
                stat2.setString(4, password);
                stat2.setBoolean(5, manager);
                System.out.println("[LOG] - Executing update to place new user in database...");
                int rs2 = stat2.executeUpdate();
                System.out.println("[LOG] - Result of update is: " + rs2);
                if (rs2 == 1) {
                    stat2.close();
                    String sql3 = "SELECT * FROM employees WHERE username = ?";
                    PreparedStatement stat3 = conn.prepareStatement(sql3);
                    stat3.setString(1, username);
                    ResultSet rs3 = stat3.executeQuery();
                    System.out.println(rs3);
                    rs3.next();
                    System.out.println(rs3);
                    int empId = rs3.getInt("employee_id");
                    String frst = rs3.getString("first");
                    String lst = rs3.getString("last");
                    String usrname = rs3.getString("username");
                    String passwrd = rs3.getString("password");
                    Boolean managr = rs3.getBoolean("manager");
                    Employee employee = new Employee(empId, frst, lst, usrname, passwrd, managr);

                    return employee;

                }


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public Employee updateEmployee(int id, Boolean newRole) {


        try (Connection conn = ConnectionUtil.getConnection()) {

            String sqlC = "SELECT * FROM employees WHERE employee_id = ?";
            PreparedStatement statC = conn.prepareStatement(sqlC);
            statC.setInt(1, id);
            ResultSet rs = statC.executeQuery();
            if (rs.next()) {
                int empId = rs.getInt("employee_id");
                String frst = rs.getString("first");
                String lst = rs.getString("last");
                String usrname = rs.getString("username");
                String passwrd = rs.getString("password");
                Boolean managr = rs.getBoolean("manager");
                Employee emp = new Employee(empId, frst, lst, usrname, passwrd, managr);
                statC.close();

                String sql = "UPDATE employees SET manager=? WHERE employee_id=?";
                PreparedStatement stat = conn.prepareStatement(sql);
                stat.setBoolean(1, newRole);
                stat.setInt(2, emp.getEmployeeId());
                int rs2 = stat.executeUpdate();
                if (rs2 == 1) {
                    return emp;
                }

                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return null;


    }
}