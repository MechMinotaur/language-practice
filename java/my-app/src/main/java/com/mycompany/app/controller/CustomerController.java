package com.mycompany.app.controller;

import java.util.List;

import com.mycompany.app.model.CustomerDataAccessObject;
import com.mycompany.app.model.CustomerModel;
import com.mycompany.app.view.CustomerView;

public class CustomerController {
    private CustomerDataAccessObject customerDAO;
    private CustomerView customerView;

    public CustomerController(CustomerDataAccessObject customerDAO, CustomerView customerView) {
        this.customerDAO = customerDAO;
        this.customerView = customerView;
    }

    public void displayAllCustomers() {
        List<CustomerModel> customers = customerDAO.getAllCustomers();
        customerView.displayAllCustomers(customers);
    }
}
