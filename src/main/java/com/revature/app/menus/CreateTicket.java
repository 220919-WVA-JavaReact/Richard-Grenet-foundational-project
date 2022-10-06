package com.revature.app.menus;

import com.revature.app.models.Employee;
import com.revature.app.models.Ticket;
import com.revature.app.service.TicketService;

import java.util.Scanner;

public class CreateTicket {


    public static String spin(TicketService ts, Scanner sc, Employee loggedInUser){


        System.out.println("Please provide us with some information so we can send in your reimbursement request.");

        System.out.println("Enter an amount ( Amount you wish to be reimbursed )");
        String amnt = sc.nextLine();
        if(amnt.equals("exit")){
            System.out.println("Exiting program...");
            return "exit";
        } else if (amnt.trim().equals("")) {
            System.out.println("+----------------------------------------+");
            System.out.println("ERROR: Amount field is required.");
            System.out.println("+----------------------------------------+");
            return "CreateTicket";
        }
        System.out.println("Now give a small description of the circumstances surrounding the requested reimbursement.");
        String descrip = sc.nextLine();
        if (descrip.trim().equals("")) {
            System.out.println("+----------------------------------------+");
            System.out.println("ERROR: description field is required.");
            System.out.println("+----------------------------------------+");
            return "CreateTicket";
        }
        Ticket res = ts.createTicket(loggedInUser.getEmployeeId(),Float.parseFloat(amnt),descrip);
            if(res != null){
                //ticket creation succeeded
                System.out.println("Your ticket has been created!");
                System.out.println("+-----------------------------+");
                System.out.println(res.toString());
                System.out.println("+-----------------------------+");
                return "LoggedInMenu";
            } else{
                System.out.println("Ticket creation failed.");

            }
        return "LoggedInMenu";
        }



    }
