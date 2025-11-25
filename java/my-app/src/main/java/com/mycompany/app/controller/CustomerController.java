package com.mycompany.app.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

import com.google.gson.JsonSyntaxException;
import com.mycompany.app.model.CustomerDataAccessObject;
import com.mycompany.app.model.CustomerModel;
import com.mycompany.app.view.CustomerView;
import com.mycompany.app.view.EventView;

public class CustomerController {

    private final CustomerDataAccessObject customerDAO;
    private final CustomerView customerView;
    private final EventView eventView;

    public CustomerController(CustomerDataAccessObject customerDAO, CustomerView customerView, EventView eventView) {
        this.customerDAO = customerDAO;
        this.customerView = customerView;
        this.eventView = eventView;
    }

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
