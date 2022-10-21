package com.revature.app.dao;

import com.revature.app.models.Employee;

public interface EmployeeDAO {

    Employee getByUsername(String username);

    Employee createEmployee(String first, String last, String username,
                             String password, Boolean manager);
    Employee updateEmployee(int id, Boolean newRole);
}
