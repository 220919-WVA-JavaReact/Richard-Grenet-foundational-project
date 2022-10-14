package com.revature.app.dao;

import com.revature.app.models.Employee;
import com.revature.app.models.Ticket;
import com.revature.app.util.ConnectionUtil;

import javax.xml.transform.Result;
import java.sql.*;

public class EmployeeDAOimpl implements EmployeeDAO{
    @Override
    public Employee getByUsername(String username) {


        try( Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT * FROM employees WHERE username = ?";
            //? goes where info goes
            PreparedStatement stat = conn.prepareStatement(sql);
            //set individual values for ? makes
            stat.setString(1, username);
            ResultSet rs = stat.executeQuery();
            if(rs.next()){
                int empId = rs.getInt("employee_id");
                String frst = rs.getString("first");
                String lst = rs.getString("last");
                String usrname = rs.getString("username");
                String passwrd = rs.getString("password");
                Boolean managr = rs.getBoolean("manager");
                Employee employee = new Employee(empId,frst,lst,usrname,passwrd,managr);

//                System.out.println(employee.toString());

                return employee;
            }

        } catch(SQLException e){
            e.printStackTrace();
        }



        return null;
    }

    @Override
    public Employee createEmployee(String first, String last, String username, String password, Boolean manager) {


        try( Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT * FROM employees WHERE username = ?";
            //? goes where info goes
            PreparedStatement stat = conn.prepareStatement(sql);
            //set individual values for ? makes
            stat.setString(1, username);
            ResultSet rs = stat.executeQuery();
            if(!rs.next()){
                String sql2 = "INSERT INTO employees (\"first\", \"last\", username, \"password\", manager)" +
                        "VALUES (?,?,?,?,?)";
                PreparedStatement stat2 = conn.prepareStatement(sql2);
                stat2.setString(1,first);
                stat2.setString(2,last);
                stat2.setString(3,username);
                stat2.setString(4,password);
                stat2.setBoolean(5,manager);
                int rs2 = stat2.executeUpdate();
                System.out.println(rs2);
                if(rs2 == 1){
                    String sql3 = "SELECT * FROM employees WHERE username = ?";
                    PreparedStatement stat3 = conn.prepareStatement(sql3);
                    stat.setString(1, username);
                    ResultSet rs3 = stat3.executeQuery();
                    rs3.next();

                    int empId = rs3.getInt("employee_id");
                    String frst = rs3.getString("first");
                    String lst = rs3.getString("last");
                    String usrname = rs3.getString("username");
                    String passwrd = rs3.getString("password");
                    Boolean managr = rs3.getBoolean("manager");
                    Employee employee = new Employee(empId,frst,lst,usrname,passwrd,managr);

                    return employee;

                }




            }

        } catch(SQLException e){
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public Employee updateEmployee(Employee emp) {

        try (Connection conn = ConnectionUtil.getConnection()){
            String sql = "UPDATE employees SET manager=? WHERE employee_id=?";
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setBoolean(1,emp.isManager());
            stat.setInt(2,emp.getEmployeeId());
            int rs = stat.executeUpdate();
            if(rs == 1){
                return emp;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return emp;
    }
}
