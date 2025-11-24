package com.mycompany.app.model;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
                resultSet.getInt("social"),
                resultSet.getString("firstName"),
                resultSet.getString("lastName"),
                resultSet.getString("email"),
                resultSet.getString("phoneNumber"));
    }

    private static String UpdateRequestFrom(Collection<CustomerModel> customers) {
        var sb = new StringBuilder(REMOTEURL);
        var filters = new ArrayList<String>();

        var values = customers.stream()
                .map(CustomerModel::social)
                .map(Object::toString)
                .filter(Objects::nonNull)
                .toArray(String[]::new);

        if (values.length > 0) {
            filters.add("social=" + String.join(",", values));
        }

        if (!filters.isEmpty()) {
            sb.append("?");
            sb.append(String.join("&", filters));
            sb.append("&mode=OR");
        }

        return sb.toString();
    }

    private String GetUpdateCustomersSql(
            HttpResponse<String> response,
            HashMap<Integer, CustomerModel> localCustomers) {

        Type listType = new TypeToken<List<CustomerModel>>() {
        }.getType();
        List<CustomerModel> customersResponse = gson.fromJson(response.body(), listType);

        var sb = new StringBuilder();
        for (CustomerModel remoteCustomer : customersResponse) {
            var localCustomer = localCustomers.get(remoteCustomer.social());

            var updatedCustomer = CustomerModel.FromRemote(localCustomer, remoteCustomer);
            sb.append(updatedCustomer.toString());
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
        var socialLocalCustomer = new HashMap<Integer, CustomerModel>();
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
                socialLocalCustomer.put(customer.social(), customer);
            }
            var requestStr = UpdateRequestFrom(socialLocalCustomer.values());
            var request = HttpRequest.newBuilder()
                    .uri(new URI(requestStr))
                    .GET()
                    .build();

            var response = httpClient.send(request, BodyHandlers.ofString());
            var updateSql = GetUpdateCustomersSql(response, socialLocalCustomer);

        } catch (InterruptedException | IOException | JsonSyntaxException | SQLException | URISyntaxException e) {

        }

        return socialLocalCustomer.values();
    }

}
