package com.mycompany.app.view;

import java.util.logging.Logger;

import com.mycompany.app.model.CustomerModel;

public class CustomerView {

    @SuppressWarnings("NonConstantLogger")
    private final Logger logger;

    public CustomerView(Logger logger) {
        this.logger = logger;
    }

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
            this.logger.info(output);
        }
    }

    public void displayNumberCustomersUpdated(int updated) {
        this.logger.info("%d customers updated.".formatted(updated));
    }
}
