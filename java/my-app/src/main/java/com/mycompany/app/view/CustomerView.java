package com.mycompany.app.view;

import com.mycompany.app.model.CustomerModel;

public class CustomerView {

    public void displayCustomers(Iterable<CustomerModel> customers) {
        for (CustomerModel customerModel : customers) {
            var output = """
                    %s %s
                    Email: %s
                    Phone Number: %s
                      """.formatted(
                    customerModel.firstName(),
                    customerModel.lastName(),
                    customerModel.email(),
                    customerModel.phoneNumber());
            System.out.println(output);
        }
    }
}
