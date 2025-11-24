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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class CustomerDataAccessObject {
    private static final String DBURL = "jdbc:sqlite:sample.db";
    private static final String REMOTEURL = "http://localhost:5000/customers";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    private static CustomerModel From(ResultSet resultSet) throws SQLException {
        return new CustomerModel(
                resultSet.getString("firstName"),
                resultSet.getString("lastName"),
                resultSet.getString("email"),
                resultSet.getString("phoneNumber"));
    }

    private static void AddFilter(String queryName, String[] values, List<String> filters) {
        if (values.length > 0) {
            filters.add(queryName + "=" + String.join(",", values));
        }
    }

    private static String UpdateRequestFrom(Collection<CustomerModel> customers) {
        var sb = new StringBuilder(REMOTEURL);
        var filters = new ArrayList<String>();

        var firstNames = customers.stream()
                .map(CustomerModel::firstName)
                .filter(Objects::nonNull)
                .toArray(String[]::new);
        AddFilter("firstName", firstNames, filters);

        var lastNames = customers.stream()
                .map(CustomerModel::lastName)
                .filter(Objects::nonNull)
                .toArray(String[]::new);
        AddFilter("lastName", lastNames, filters);

        var emails = customers.stream()
                .map(CustomerModel::email)
                .filter(Objects::nonNull)
                .toArray(String[]::new);
        AddFilter("email", emails, filters);

        var phoneNumbers = customers.stream()
                .map(CustomerModel::phoneNumber)
                .filter(Objects::nonNull)
                .toArray(String[]::new);
        AddFilter("phoneNumber", phoneNumbers, filters);

        if (!filters.isEmpty()) {
            sb.append("?");
            sb.append(String.join("&", filters));
            sb.append("&mode=OR");
        }

        return sb.toString();
    }

    public List<CustomerModel> getAllLocalCustomers() {
        List<CustomerModel> customers = new ArrayList<>();
        try (
                var connection = DriverManager.getConnection(DBURL);
                var statement = connection.createStatement();
                var resultSet = statement.executeQuery("select * from customer")) {
            while (resultSet.next()) {
                customers.add(From(resultSet));
            }
        } catch (SQLException e) {

        }

        return customers;
    }

    public Iterable<CustomerModel> updateLocalCustomers() {
        /*
         * TODO:
         * Update our database with the new data.
         */
        var nameCustomer = new HashMap<String, CustomerModel>();
        var sqlString = """
                select * from customer
                where email is null or phoneNumber is null
                """;
        try (
                var connection = DriverManager.getConnection(DBURL);
                var statement = connection.createStatement();
                var resultSet = statement.executeQuery(sqlString)) {
            while (resultSet.next()) {
                var customer = From(resultSet);
                nameCustomer.put(customer.firstName(), customer);
            }
            var requestStr = UpdateRequestFrom(nameCustomer.values());
            var request = HttpRequest.newBuilder()
                    .uri(new URI(requestStr))
                    .GET()
                    .build();

            var response = httpClient.send(request, BodyHandlers.ofString());
            Type listType = new TypeToken<List<CustomerModel>>() {
            }.getType();
            List<CustomerModel> customersResponse = gson.fromJson(response.body(), listType);
            for (CustomerModel customerModel : customersResponse) {
                System.out.println(customerModel.firstName());
            }
        } catch (InterruptedException | IOException | JsonSyntaxException | SQLException | URISyntaxException e) {

        }

        return nameCustomer.values();
    }

}
