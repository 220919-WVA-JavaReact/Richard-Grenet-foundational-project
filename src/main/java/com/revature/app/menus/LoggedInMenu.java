package com.revature.app.menus;

import com.revature.app.models.Employee;
import com.revature.app.models.Ticket;
import com.revature.app.service.TicketService;

import java.util.Scanner;

public class LoggedInMenu {


    public static String spin(TicketService ts, Scanner sc, Employee loggedInUser){


        System.out.println("What would you like to do?  ( 1 = Create a ticket ) | ( 2 = See all your tickets )");
        String ticketchoice = sc.nextLine();
        if(ticketchoice.equals("exit")){
            System.out.println("Exiting program...");
            return "exit";
        } else{
            if(Integer.parseInt(ticketchoice) == 1){
                //use create ticket logic
                System.out.println("+----------------------------------------+");
                return "CreateTicket";
            } else if(Integer.parseInt(ticketchoice) == 2){
                System.out.println("Fetching all your tickets...");
                System.out.println("+----------------------------------------+");
                //show all the tickets right here
                for (Ticket t :ts.getTickets(loggedInUser.getEmployeeId())) {
                    System.out.println(t.toString());
                    System.out.println("+----------------------------------------+");
                }
                return "LoggedInMenu";
                //use get all your tickets logic
            } else{
                System.out.println("Oops! Make sure to enter either 1 or 2");

            }
        }


        return "LoggedInMenu";
    }


}
