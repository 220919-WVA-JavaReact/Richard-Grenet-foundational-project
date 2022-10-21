package com.revature.app.service;

import com.revature.app.dao.EmployeeDAO;
import com.revature.app.dao.EmployeeDAOimpl;
import com.revature.app.models.Employee;

public class EmployeeService {

    EmployeeDAO ed = new EmployeeDAOimpl();

    public Employee login(String username, String password){
        Employee result = ed.getByUsername(username);
            if ((result != null) && (result.getPassword().equals(password))){
                return result;
            }
        return null;
    }

    public Employee register(String first,String last,String username,String password,Boolean manager){
        Employee result = ed.createEmployee(first, last, username, password, manager);
        return result;
    }


    public Employee changeRole(int employeeId, boolean newRole) {
        Employee result = ed.updateEmployee(employeeId, newRole);
        return result;
    }
}
