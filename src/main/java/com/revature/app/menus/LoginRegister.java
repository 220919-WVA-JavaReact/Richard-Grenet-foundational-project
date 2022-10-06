package com.revature.app.menus;

import java.util.Scanner;

public class LoginRegister {
    public static String spin(String menu, Scanner sc){


        System.out.println("What would you like to do?  ( 1 = login ) | ( 2 = register account )");
        String loginregister = sc.nextLine();
        if(loginregister.equals("exit")){
            System.out.println("Exiting program...");
            return "exit";
        } else{
            if(Integer.parseInt(loginregister) == 1){
                //use login logic
                System.out.println("+----------------------------------------+");
                return "Login";
            } else if(Integer.parseInt(loginregister) == 2){
                System.out.println("+----------------------------------------+");
                return "Register";
                //use register logic
            } else{
                System.out.println("Oops! Make sure to enter either 1 or 2");

            }
        }


        return "LoginRegister";
    }
}
