package com.mycompany.app.view;

import java.util.List;

import com.mycompany.app.model.CustomerModel;

public class CustomerView {

    public void displayAllCustomers(List<CustomerModel> customers) {
        for (CustomerModel customerModel : customers) {
            System.out.println("First Name: " + customerModel.firstName()
                    + " Last Name: " + customerModel.lastName()
                    + " Email: " + customerModel.email()
                    + " Phone Number: " + customerModel.phoneNumber());
        }
    }
}
