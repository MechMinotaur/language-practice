package com.mycompany.app.controller;

import com.mycompany.app.model.CustomerDataAccessObject;
import com.mycompany.app.view.CustomerView;

public class CustomerController {
    private final CustomerDataAccessObject customerDAO;
    private final CustomerView customerView;

    public CustomerController(CustomerDataAccessObject customerDAO, CustomerView customerView) {
        this.customerDAO = customerDAO;
        this.customerView = customerView;
    }

    public void displayAllLocalCustomers() {
        var customers = customerDAO.getAllLocalCustomers();
        customerView.displayCustomers(customers);
    }

    public void updateLocalCustomers() {
        var customers = customerDAO.updateLocalCustomers();
        customerView.displayCustomers(customers);
    }
}
