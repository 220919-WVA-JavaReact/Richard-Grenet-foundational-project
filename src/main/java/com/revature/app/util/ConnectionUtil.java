package com.revature.app.util;

import jdk.jfr.StackTrace;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
        String url = System.getenv("url");
        String username = System.getenv("username");
        String password = System.getenv("password");
        try {
            conn = DriverManager.getConnection(url, username, password);
            //above creates new connection^
        } catch (SQLException e) {
            System.out.println("Couldn't establish connection");
            e.printStackTrace();
        }
        return conn;
    }
}
