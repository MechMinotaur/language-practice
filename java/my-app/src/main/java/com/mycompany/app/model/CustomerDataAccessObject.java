package com.mycompany.app.model;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class CustomerDataAccessObject {
    private String dbURL = "jdbc:sqlite:sample.db";

    public List<CustomerModel> getAllLocalCustomers() {
        List<CustomerModel> customers = new ArrayList<>();
        try (
                var connection = DriverManager.getConnection(dbURL);
                var statement = connection.createStatement();
                var resultSet = statement.executeQuery("select * from customer")) {
            while (resultSet.next()) {
                customers.add(new CustomerModel(
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("email"),
                        resultSet.getString("phoneNumber")));
            }
        } catch (SQLException e) {

        }

        return customers;
    }

    public List<CustomerModel> getAllRemoteCustomers() {
        /*
         * TODO:
         * Use JSON (GSON plugin) and HTTPRequest instead of HTTURLConnection.
         * Query only clients that are missing data.
         * Update our database with the new data.
         */
        List<CustomerModel> customers = new ArrayList<>();

        // Test GSON
        var customer = new CustomerModel(dbURL, dbURL, dbURL, dbURL);
        Gson gson = new Gson();
        var str = gson.toJson(customer);
        System.out.println("GSON String: " + str);

        return customers;
    }

}
