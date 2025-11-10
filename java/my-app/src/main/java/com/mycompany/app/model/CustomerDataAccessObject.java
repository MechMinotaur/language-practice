package com.mycompany.app.model;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class CustomerDataAccessObject {
    private static String dbURL = "jdbc:sqlite:sample.db";
    private static String remoteURLString = "http://localhost:5000/customers";
    private HttpClient httpClient = HttpClient.newHttpClient();
    private Gson gson = new Gson();

    private static CustomerModel From(ResultSet resultSet) throws SQLException {
        return new CustomerModel(
                resultSet.getString("firstName"),
                resultSet.getString("lastName"),
                resultSet.getString("email"),
                resultSet.getString("phoneNumber"));
    }

    private static String UpdateRequestFrom(CustomerModel customer) {
        var sb = new StringBuilder(remoteURLString);
        var filters = new ArrayList<String>();

        if (customer.firstName() != null) {
            filters.add("firstName=" + customer.firstName());
        }
        if (customer.lastName() != null) {
            filters.add("lastName=" + customer.lastName());
        }
        if (customer.email() != null) {
            filters.add("email=" + customer.email());
        }
        if (customer.phoneNumber() != null) {
            filters.add("phoneNumber=" + customer.phoneNumber());
        }

        if (!filters.isEmpty()) {
            sb.append("?");
            sb.append(String.join("&", filters));
        }

        return sb.toString();
    }

    public List<CustomerModel> getAllLocalCustomers() {
        List<CustomerModel> customers = new ArrayList<>();
        try (
                var connection = DriverManager.getConnection(dbURL);
                var statement = connection.createStatement();
                var resultSet = statement.executeQuery("select * from customer")) {
            while (resultSet.next()) {
                customers.add(From(resultSet));
            }
        } catch (SQLException e) {

        }

        return customers;
    }

    public List<CustomerModel> updateLocalCustomers() {
        /*
         * TODO:
         * Update our database with the new data.
         */
        List<CustomerModel> customers = new ArrayList<>();
        var sqlString = """
                select * from customer
                where email is null or phoneNumber is null
                """;
        try (
                var connection = DriverManager.getConnection(dbURL);
                var statement = connection.createStatement();
                var resultSet = statement.executeQuery(sqlString)) {
            while (resultSet.next()) {
                var customer = From(resultSet);
                var requestStr = UpdateRequestFrom(customer);
                var request = HttpRequest.newBuilder()
                        .uri(new URI(requestStr))
                        .GET()
                        .build();
                var response = httpClient.send(request, BodyHandlers.ofString());
                Type listType = new TypeToken<List<CustomerModel>>() {
                }.getType();
                ArrayList<CustomerModel> customersResponse = gson.fromJson(response.body(), listType);
                System.out.println(response.body());
            }
        } catch (InterruptedException | IOException | JsonSyntaxException | SQLException | URISyntaxException e) {

        }

        return customers;
    }

}
