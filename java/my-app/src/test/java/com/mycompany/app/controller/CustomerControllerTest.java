package com.mycompany.app.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import com.google.gson.JsonSyntaxException;
import com.mycompany.app.model.CustomerDataAccessor;
import com.mycompany.app.model.CustomerModel;
import com.mycompany.app.view.CustomerDisplay;
import com.mycompany.app.view.EventDisplay;

public class CustomerControllerTest {
    private CustomerController customerController;
    private CustomerDataAccessor customerDAO;
    private CustomerDisplay customerDisplay;
    private EventDisplay eventDisplay;

    static Stream<Arguments> customerDaoExceptions() {
        return Stream.of(
                Arguments.of(new JsonSyntaxException("")),
                Arguments.of(new IOException("")),
                Arguments.of(new InterruptedException("")),
                Arguments.of(new URISyntaxException("", "")),
                Arguments.of(new SQLException("")));
    }

    @BeforeEach
    void Setup() {
        this.customerDAO = Mockito.mock(CustomerDataAccessor.class);
        this.customerDisplay = Mockito.mock(CustomerDisplay.class);
        this.eventDisplay = Mockito.mock(EventDisplay.class);

        this.customerController = new CustomerController(this.customerDAO, this.customerDisplay, this.eventDisplay);
    }

    @ParameterizedTest
    @MethodSource("com.mycompany.app.TestData#GetCustomerData")
    void TestDisplayAllLocalCustomersGetsAndDisplays(Iterable<CustomerModel> customers, int numCustomers) {
        var customersList = new ArrayList<CustomerModel>();
        customers.forEach(customersList::add);

        try {
            when(this.customerDAO.getAllLocalCustomers()).thenReturn(customersList);
        } catch (SQLException e) {
            throw new RuntimeException("Stubbing failed unexpectedly", e);
        }

        this.customerController.displayAllLocalCustomers();

        verify(this.customerDisplay).displayCustomers(customers);
    }

    @Test
    void TestDisplayAllLocalCustomersCallsLogExceptionWhenSqlExceptionThrown() {
        try {
            when(this.customerDAO.getAllLocalCustomers()).thenThrow(SQLException.class);
        } catch (SQLException e) {
            throw new RuntimeException("Stubbing failed unexpectedly", e);
        }

        this.customerController.displayAllLocalCustomers();

        verify(this.eventDisplay).LogException(
                eq(this.customerDAO.getClass().toString()),
                eq("getAllLocalCustomers"),
                any(SQLException.class));

    }

    @ParameterizedTest
    @MethodSource("com.mycompany.app.TestData#GetCustomerData")
    void TestUpdateLocalCustomersGetsAndDisplays(Iterable<CustomerModel> customers, int numCustomers) {
        try {
            when(this.customerDAO.updateLocalCustomers()).thenReturn(numCustomers);
        } catch (InterruptedException | IOException | JsonSyntaxException | SQLException | URISyntaxException e) {
            throw new RuntimeException("Stubbing failed unexpectedly", e);
        }

        this.customerController.updateLocalCustomers();

        verify(this.customerDisplay).displayNumberCustomersUpdated(eq(numCustomers));
    }

    @ParameterizedTest
    @MethodSource("customerDaoExceptions")
    void TestUpdateLocalCustomersLogsExceptions(Throwable exception) {
        try {
            when(this.customerDAO.updateLocalCustomers()).thenThrow(exception);
        } catch (InterruptedException | IOException | JsonSyntaxException | SQLException | URISyntaxException e) {
            throw new RuntimeException("Stubbing failed unexpectedly", e);
        }

        this.customerController.updateLocalCustomers();

        verify(this.eventDisplay).LogException(
                eq(this.customerDAO.getClass().toString()),
                eq("updateLocalCustomers"),
                eq(exception));
    }
}
