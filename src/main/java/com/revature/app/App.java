package com.revature.app;

import com.revature.app.menus.*;
import com.revature.app.models.Employee;
import com.revature.app.service.EmployeeService;
import com.revature.app.service.TicketService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {


    public static EmployeeService es = new EmployeeService();
    public static TicketService ts = new TicketService();

    public static void main(String[] args) {
        System.out.println("Welcome to the Reimbursement Ticket App!");
        System.out.println("Enter 'exit' to exit the app at any time.");
        //scan for user input to decide what actions to take
        Scanner sc = new Scanner(System.in);
        Employee loggedInUser = null;
        //start loop
        boolean running = true;
        String menu = "LoginRegister";
        while(running){

            //menu switch case

            switch (menu) {
                case "LoginRegister":
                    //call on method in menu to show text and do choices. maybe a method called 'spin' which just does the logic once and takes in the menu and scanner.
                    menu=LoginRegister.spin(menu, sc);
                    break;
                case "Login":
                    //call on method in menu to show text and do choices. maybe a method called 'spin' which just does the logic once and takes in the menu and scanner.
                    List loginResult = Login.spin(es,sc);
                    menu= loginResult.get(0).toString();
                    loggedInUser = (Employee) loginResult.get(1);
                    break;
                case "Register":
                    //call on method in menu to show text and do choices. maybe a method called 'spin' which just does the logic once and takes in the menu and scanner.
                    List registerResult = Register.spin(es, sc);
                    menu= registerResult.get(0).toString();
                    loggedInUser = (Employee) registerResult.get(1);
                    break;
                case "LoggedInMenu":
                    menu= LoggedInMenu.spin(ts,sc,loggedInUser);
                    break;
                case "CreateTicket":
                    menu= CreateTicket.spin(ts,sc,loggedInUser);
                    break;
                case "exit":
                    running=false;
                    break;
            }



        }


    }
}
