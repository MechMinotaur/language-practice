package com.mycompany.app.view;

import java.util.logging.Logger;

import com.mycompany.app.model.CustomerModel;

public class CustomerView implements CustomerDisplay {

    @SuppressWarnings("NonConstantLogger")
    private final Logger logger;

    public CustomerView(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void displayCustomers(Iterable<CustomerModel> customers) {
        if (customers == null) {
            throw new NullPointerException("Customers cannot be null.");
        }

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

    @Override
    public void displayNumberCustomersUpdated(int updated) {
        this.logger.info("%d customers updated.".formatted(updated));
    }
}
