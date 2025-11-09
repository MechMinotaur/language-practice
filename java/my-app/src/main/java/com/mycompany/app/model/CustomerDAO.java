package com.mycompany.app.model;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDAO {
    private String dbURL = "jdbc:sqlite:sample.db";

    public List<CustomerModel> getAllCustomers() {
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
                        Optional.ofNullable(resultSet.getString("phoneNumber"))));
            }
        } catch (SQLException e) {

        }

        return customers;
    }

}
