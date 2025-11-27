package com.mycompany.app.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

import com.google.gson.JsonSyntaxException;
import com.mycompany.app.model.CustomerDataAccessor;
import com.mycompany.app.model.CustomerModel;
import com.mycompany.app.view.CustomerDisplay;
import com.mycompany.app.view.EventDisplay;

public class CustomerController implements CustomerManager {

    private final CustomerDataAccessor customerDAO;
    private final CustomerDisplay customerView;
    private final EventDisplay eventView;

    public CustomerController(CustomerDataAccessor customerDAO, CustomerDisplay customerView, EventDisplay eventView) {
        this.customerDAO = customerDAO;
        this.customerView = customerView;
        this.eventView = eventView;
    }

    @Override
    public void displayAllLocalCustomers() {
        Iterable<CustomerModel> customers;
        try {
            customers = customerDAO.getAllLocalCustomers();
        } catch (SQLException e) {
            this.eventView.LogException(this.customerDAO.getClass().toString(), "getAllLocalCustomers", e);
            return;
        }

        customerView.displayCustomers(customers);
    }

    @Override
    public void updateLocalCustomers() {
        int customersUpdated;
        try {
            customersUpdated = customerDAO.updateLocalCustomers();
        } catch (JsonSyntaxException | IOException | InterruptedException | URISyntaxException | SQLException e) {
            this.eventView.LogException(this.customerDAO.getClass().toString(), "updateLocalCustomers", e);
            return;
        }
        customerView.displayNumberCustomersUpdated(customersUpdated);
    }
}
