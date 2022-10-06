package com.revature.app.models;

import java.util.Objects;

public class Employee {
    //variables
    private int employeeId;
    private String first;
    private String last;
    private String username;
    private String password;
    private boolean manager;
    //constructors
    public Employee(int employeeId, String first, String last, String username, String password, Boolean manager) {
        this.employeeId = employeeId;
        this.first = first;
        this.last = last;
        this.username = username;
        this.password = password;
        this.manager = manager;
    }

    public Employee(int employeeId) {
        this.employeeId = employeeId;
    }
    //getters and setters


    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isManager() {
        return manager;
    }

    public void setManager(boolean manager) {
        this.manager = manager;
    }
    //overrides
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return getEmployeeId() == employee.getEmployeeId() && isManager() == employee.isManager() && Objects.equals(getFirst(), employee.getFirst()) && Objects.equals(getLast(), employee.getLast()) && Objects.equals(getUsername(), employee.getUsername()) && Objects.equals(getPassword(), employee.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmployeeId(), getFirst(), getLast(), getUsername(), getPassword(), isManager());
    }

    @Override
    public String toString() {
        return "Employee{" +
                "studentId=" + employeeId +
                ", first='" + first + '\'' +
                ", last='" + last + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", manager=" + manager +
                '}';
    }
}
