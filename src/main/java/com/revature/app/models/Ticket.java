package com.revature.app.models;

import java.util.Objects;

public class Ticket {
    //variables
    private int ticketId;
    private int employeeId;
    private float amount;
    private String description;
    private String status;
    //constructors
    public Ticket(int ticketId, int employeeId, float amount, String description, String status) {
        this.ticketId = ticketId;
        this.employeeId = employeeId;
        this.amount = amount;
        this.description = description;
        this.status = status;
    }

    public Ticket() {
    }
    //getters and setters

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    //overrides
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return getTicketId() == ticket.getTicketId() && getEmployeeId() == ticket.getEmployeeId() && getAmount() == ticket.getAmount() && getStatus() == ticket.getStatus() && Objects.equals(getDescription(), ticket.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTicketId(), getEmployeeId(), getAmount(), getDescription(), getStatus());
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + ticketId +
                ", employeeId=" + employeeId +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

}