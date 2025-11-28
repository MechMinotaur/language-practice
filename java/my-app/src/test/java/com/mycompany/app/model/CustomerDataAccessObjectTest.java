package com.mycompany.app.model;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.net.http.HttpClient;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import com.google.gson.Gson;

public class CustomerDataAccessObjectTest {
    private CustomerDataAccessObject customerDataAccessObject;
    private DataSource dataSource;
    private final String remoteRestUrl = "http://localhost:5000/customers";
    private HttpClient httpClient;
    private Gson gson;

    @BeforeEach
    void Setup() {
        this.dataSource = Mockito.mock(DataSource.class);
        this.httpClient = Mockito.mock(HttpClient.class);
        this.gson = Mockito.mock(Gson.class);
        this.customerDataAccessObject = new CustomerDataAccessObject(this.dataSource, remoteRestUrl, httpClient, gson);
    }

    @ParameterizedTest
    @MethodSource("com.mycompany.app.TestData#GetCustomerData")
    void TestGetAllLocalCustomersQueriesLocalDatabase(Iterable<CustomerModel> customers, int numCustomers) {
        var mockConnection = Mockito.mock(Connection.class);
        var mockStatement = Mockito.mock(Statement.class);
        var mockResultSet = Mockito.mock(ResultSet.class);

        var mockedCustomers = new ArrayList<CustomerModel>();
        customers.forEach(mockedCustomers::add);

        try {
            when(this.dataSource.getConnection()).thenReturn(mockConnection);
            when(mockConnection.createStatement()).thenReturn(mockStatement);
            when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);

            // AtomicInteger since below requires final.
            final int[] iterations = { numCustomers };
            when(mockResultSet.next()).thenAnswer(invocation -> {
                return iterations[0]-- > 0;
            });
            when(mockResultSet.getInt("social")).thenAnswer(invocation -> {
                return mockedCustomers.get(iterations[0]).social();
            });
            when(mockResultSet.getString("firstName")).thenAnswer(invocation -> {
                return mockedCustomers.get(iterations[0]).firstName();
            });
            when(mockResultSet.getString("lastName")).thenAnswer(invocation -> {
                return mockedCustomers.get(iterations[0]).lastName();
            });
            when(mockResultSet.getString("email")).thenAnswer(invocation -> {
                return mockedCustomers.get(iterations[0]).email();
            });
            when(mockResultSet.getString("phoneNumber")).thenAnswer(invocation -> {
                return mockedCustomers.get(iterations[0]).phoneNumber();
            });

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
}
