package com.mycompany.app.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import com.google.gson.Gson;

public class CustomerDataAccessObjectTest {
    private CustomerDataAccessObject customerDataAccessObject;

    private final String remoteRestUrl = "http://localhost:5000/customers";
    private HttpClient httpClient;
    private Gson gson;

    private DataSource dataSource;
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    @BeforeEach
    void Setup() {
        this.dataSource = Mockito.mock(DataSource.class);
        this.httpClient = Mockito.mock(HttpClient.class);
        this.gson = Mockito.mock(Gson.class);
        this.customerDataAccessObject = new CustomerDataAccessObject(this.dataSource, remoteRestUrl, httpClient, gson);

        this.connection = Mockito.mock(Connection.class);
        this.statement = Mockito.mock(Statement.class);
        this.resultSet = Mockito.mock(ResultSet.class);
    }

    List<CustomerModel> SetupMockDataBase(Iterable<CustomerModel> customers, int numCustomers) throws SQLException {
        var mockedCustomers = new ArrayList<CustomerModel>();
        customers.forEach(mockedCustomers::add);

        when(this.dataSource.getConnection()).thenReturn(this.connection);
        when(this.connection.createStatement()).thenReturn(this.statement);
        when(this.statement.executeQuery(anyString())).thenReturn(this.resultSet);

        // AtomicInteger since below requires final.
        final int[] iterations = { numCustomers };
        when(this.resultSet.next()).thenAnswer(invocation -> {
            return iterations[0]-- > 0;
        });
        when(this.resultSet.getInt("social")).thenAnswer(invocation -> {
            return mockedCustomers.get(iterations[0]).social();
        });
        when(this.resultSet.getString("firstName")).thenAnswer(invocation -> {
            return mockedCustomers.get(iterations[0]).firstName();
        });
        when(this.resultSet.getString("lastName")).thenAnswer(invocation -> {
            return mockedCustomers.get(iterations[0]).lastName();
        });
        when(this.resultSet.getString("email")).thenAnswer(invocation -> {
            return mockedCustomers.get(iterations[0]).email();
        });
        when(this.resultSet.getString("phoneNumber")).thenAnswer(invocation -> {
            return mockedCustomers.get(iterations[0]).phoneNumber();
        });

        return mockedCustomers;
    }

    @ParameterizedTest
    @MethodSource("com.mycompany.app.TestData#GetCustomerData")
    void TestGetAllLocalCustomersQueriesLocalDatabase(Iterable<CustomerModel> customers, int numCustomers) {
        try {
            var mockedCustomers = this.SetupMockDataBase(customers, numCustomers);

            // Act
            var returnedCustomers = this.customerDataAccessObject.getAllLocalCustomers();

            // Verify
            for (CustomerModel returned : returnedCustomers) {
                var found = false;
                for (CustomerModel mocked : mockedCustomers) {
                    found = returned.equals(mocked);
                    if (found) {
                        break;
                    }
                }
                assertTrue(found);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Stubbing failed unexpectedly", e);
        }
    }

    @SuppressWarnings("unchecked")
    @ParameterizedTest
    @MethodSource("com.mycompany.app.TestData#GetCustomerData")
    void TestUpdateLocalCustomersReturnsHowManyCustomersUpdated(Iterable<CustomerModel> customers, int numCustomers) {
        try {
            var mockedCustomers = this.SetupMockDataBase(customers, numCustomers);
            var stackedCustomers = new ArrayDeque<CustomerModel>(mockedCustomers);

            var mockSuccessBody = "{\"message\": \"Success\"}";

            HttpResponse<String> mockResponse = mock(HttpResponse.class);
            when(mockResponse.body())
                    .thenReturn(mockSuccessBody);

            when(this.gson.fromJson(eq(mockResponse.body()), any(Type.class)))
                    .thenReturn(stackedCustomers);

            when(this.httpClient.send(
                    any(HttpRequest.class),
                    any(BodyHandler.class))).thenReturn(mockResponse);

            // AtomicInteger since below requires final.
            final int[] found = { 0 };
            when(this.statement.executeUpdate(anyString())).thenAnswer(invocation -> {
                if (found[0] != 0) {
                    return found[0];
                }
                String sqlStr = invocation.getArgument(0, String.class);
                for (CustomerModel customer : customers) {
                    if (sqlStr.contains(String.valueOf(customer.social()))) {
                        found[0]++;
                    }
                }
                return found[0];
            });

            var numUpdated = this.customerDataAccessObject.updateLocalCustomers();

            assertEquals(numCustomers, numUpdated);

        } catch (IOException | InterruptedException | SQLException | URISyntaxException e) {
            throw new RuntimeException("Stubbing failed unexpectedly", e);
        }
    }
}
