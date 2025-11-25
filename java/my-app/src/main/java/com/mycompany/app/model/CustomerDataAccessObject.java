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
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    private static String CreateUpdateRequestFrom(Iterable<Integer> socials) {
        var sb = new StringBuilder(REMOTEURL);

        sb.append("?&social=");

        var socialsString = StreamSupport.stream(socials.spliterator(), false)
                .map(String::valueOf)
                .collect((Collectors.joining(",")));
        sb.append(socialsString);

        sb.append("&mode=OR");

        return sb.toString();
    }

    private String GetStageUpdatesSql(HttpResponse<String> response) {

        Type listType = new TypeToken<Stack<CustomerModel>>() {
        }.getType();
        Stack<CustomerModel> customersResponse = gson.fromJson(response.body(), listType);

        var stagingSql = """
                create temp table updatesStaging (
                social integer not null,
                newEmail text,
                newPhoneNumber text);
                """;

        var sb = new StringBuilder(stagingSql);

        sb.append("insert into updatesStaging (social, newEmail, newPhoneNumber) values");

        while (!customersResponse.isEmpty()) {
            var remoteCustomer = customersResponse.pop();
            sb.append(String.format("(%d, '%s', '%s')",
                    remoteCustomer.social(),
                    remoteCustomer.email(),
                    remoteCustomer.phoneNumber()));
            if (!customersResponse.isEmpty()) {
                sb.append(",");
            }
        }

        sb.append(";");

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

    public int updateLocalCustomers() {
        var socials = new ArrayList<Integer>();
        var sqlString = """
                select social from customer
                where email is null or phoneNumber is null
                """;
        try (
                var connection = DriverManager.getConnection(DBURL);
                var statement = connection.createStatement();
                var resultSet = statement.executeQuery(sqlString)) {
            while (resultSet.next()) {
                socials.add(resultSet.getInt("social"));
            }

            if (socials.isEmpty()) {
                return 0;
            }

            var requestStr = CreateUpdateRequestFrom(socials);

            var request = HttpRequest.newBuilder()
                    .uri(new URI(requestStr))
                    .GET()
                    .build();

            var response = httpClient.send(request, BodyHandlers.ofString());
            var stageUpdatesSql = GetStageUpdatesSql(response);

            statement.executeUpdate(stageUpdatesSql);

            var updateSql = """
                    update customer
                    set
                        phoneNumber = coalesce(customer.phoneNumber, updatesStaging.newPhoneNumber),
                        email = coalesce(customer.email, updatesStaging.newEmail)
                    from updatesStaging
                    where customer.social = updatesStaging.social
                    """;

            statement.executeUpdate(updateSql);

        } catch (InterruptedException | IOException | JsonSyntaxException | SQLException | URISyntaxException e) {
            System.out.println(e);
            return 0;
        }

        return socials.size();
    }

}
