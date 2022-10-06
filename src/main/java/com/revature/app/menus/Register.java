package com.revature.app.menus;

import com.revature.app.models.Employee;
import com.revature.app.service.EmployeeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Register {


    public static List spin(EmployeeService es, Scanner sc){

        System.out.println("Enter your first name");
        String first = sc.nextLine();
        System.out.println("Enter your last name");
        String last = sc.nextLine();
        System.out.println("Enter your preferred username");
        String username = sc.nextLine();
        System.out.println("Enter your preferred password");
        String password = sc.nextLine();
//        System.out.println("Are you a manager? ( Y | N )");
//        Boolean manager = sc.nextLine().equalsIgnoreCase("Y");
        Boolean manager = false;
        //Attempt to register via employee service leveraging the get createemp in empdao. successful login brings to 'loggedinmenu', failure brings to loginregister.
        Employee res = es.register(first,last,username,password,manager);
        List result = new ArrayList();
        if(res != null){
            //successful register
            System.out.println("+----------------------------------------+");
            System.out.println("Welcome " + username + "! Thank you for registering.");
            result.add("LoggedInMenu");
        } else{
            //register failed
            System.out.println("+----------------------------------------+");
            System.out.println("ERROR: User with that username already exists.");
            System.out.println("+----------------------------------------+");
            result.add("LoginRegister");
        }
        result.add(res);
        return result;

    }


}
