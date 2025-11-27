package com.mycompany.app.view;

import com.mycompany.app.model.CustomerModel;

public interface CustomerDisplay {

    void displayCustomers(Iterable<CustomerModel> customers);

    void displayNumberCustomersUpdated(int updated);
}
