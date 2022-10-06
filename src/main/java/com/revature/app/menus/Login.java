package com.revature.app.menus;

import com.revature.app.models.Employee;
import com.revature.app.service.EmployeeService;

import java.util.*;

public class Login {


    public static List spin(EmployeeService es, Scanner sc){


        System.out.println("Enter your username");
        String username = sc.nextLine();
        System.out.println("Enter your password");
        String password = sc.nextLine();
        //Attempt to login via employee service leveraging the get emp by username in empdao. successful login brings to 'loggedinmenu', failure brings to loginregister.
        Employee res = es.login(username,password);
        List result = new ArrayList();
        if(res != null){
                //successful login
            System.out.println("+----------------------------------------+");
            System.out.println("Welcome " + username + "!");
            result.add("LoggedInMenu");
        } else{
                //login failed
            System.out.println("+----------------------------------------+");
            System.out.println("ERROR: invalid login credentials");
            System.out.println("+----------------------------------------+");
            result.add("LoginRegister");
        }
        result.add(res);
        return result;


    }


}
