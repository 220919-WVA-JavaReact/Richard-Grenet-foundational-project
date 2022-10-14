package com.revature.app.util;

import jdk.jfr.StackTrace;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

//Singleton Connection instance
public class ConnectionUtil {

    private static Connection conn = null;

    //makes sure can only create connection in this class(1)
    private ConnectionUtil(){

    }
    //this will automatically decide to return either the currently existing
    //connection or create it.
    public static Connection getConnection(){

        try{
            if (conn != null && !conn.isClosed()){
                //use previously generated connection
                return conn;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        //if no connection exists already create one
        String url = "";
        String username = "";
        String password = "";
        //application.properties file
        Properties prop = new Properties();
        try {
            prop.load(new FileReader("C:\\Users\\RicM3\\REVATURE\\foundational\\Richard-Grenet-foundational-project\\src\\main\\resources\\application.properties"));
            url = prop.getProperty("url");
            username = prop.getProperty("username");
            password = prop.getProperty("password");
        conn = DriverManager.getConnection(url, username, password);
            //above creates new connection^

        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (SQLException e) {
            System.out.println("Couldn't establish connection");
            e.printStackTrace();
        }
        return conn;
    }
    //making sure postgresql driver is ready to go so application doesn't break
    static{
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to load PostgreSQL Driver");
            throw new RuntimeException(e);
        }
    }

}
