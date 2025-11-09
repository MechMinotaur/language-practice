package com.mycompany.app.controller;

import java.util.List;

import com.mycompany.app.model.CustomerDAO;
import com.mycompany.app.model.CustomerModel;
import com.mycompany.app.view.CustomerView;

public class CustomerController {
    private CustomerDAO customerDAO;
    private CustomerView customerView;

    public CustomerController(CustomerDAO customerDAO, CustomerView customerView) {
        this.customerDAO = customerDAO;
        this.customerView = customerView;
    }

    public void displayAllCustomers() {
        List<CustomerModel> customers = customerDAO.getAllCustomers();
        customerView.displayAllCustomers(customers);
    }
}
