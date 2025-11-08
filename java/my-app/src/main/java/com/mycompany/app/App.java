package com.mycompany.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mycompany.app.controller.UserController;
import com.mycompany.app.model.UserModel;
import com.mycompany.app.view.UserView;

/**
 * Hello world!
 */
public class App {
    public static void CreateDatabase(){
        // NOTE: Connection and Statement are AutoCloseable.
        //       Don't forget to close them both in order to avoid leaks.
        try
        (
          // create a database connection
          Connection connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
          Statement statement = connection.createStatement();
        )
        {
          statement.setQueryTimeout(30);  // set timeout to 30 sec.

          statement.executeUpdate("drop table if exists person");
          statement.executeUpdate("create table person (id integer, name string)");
          statement.executeUpdate("insert into person values(1, 'leo')");
          statement.executeUpdate("insert into person values(2, 'yui')");
          ResultSet rs = statement.executeQuery("select * from person");
          while(rs.next())
          {
            // read the result set
            System.out.println("name = " + rs.getString("name"));
            System.out.println("id = " + rs.getInt("id"));
          }
        }
        catch(SQLException e)
        {
          // if the error message is "out of memory",
          // it probably means no database file is found
          e.printStackTrace(System.err);
        }
    }

    public static void main(String[] args) {
        CreateDatabase();
        var userModel = new UserModel();
        var userView = new UserView();
        var UserController = new UserController();
        System.out.println("Hello from App!");
    }
}
