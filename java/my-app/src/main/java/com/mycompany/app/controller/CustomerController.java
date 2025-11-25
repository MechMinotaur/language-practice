package com.mycompany.app.controller;

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
        } catch (Exception e) {
            this.eventView.LogException(this.customerDAO.getClass().toString(), "getAllLocalCustomers", e);
            return;
        }

        customerView.displayCustomers(customers);
    }

    public void updateLocalCustomers() {
        int customersUpdated;
        try {
            customersUpdated = customerDAO.updateLocalCustomers();
        } catch (Exception e) {
            this.eventView.LogException(this.customerDAO.getClass().toString(), "updateLocalCustomers", e);
            return;
        }
        customerView.displayNumberCustomersUpdated(customersUpdated);
    }
}
