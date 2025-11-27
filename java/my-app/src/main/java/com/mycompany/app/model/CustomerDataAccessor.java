package com.mycompany.app.model;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;

import com.google.gson.JsonSyntaxException;

public interface CustomerDataAccessor {

    List<CustomerModel> getAllLocalCustomers() throws SQLException;

    int updateLocalCustomers() throws InterruptedException, IOException, JsonSyntaxException, SQLException, URISyntaxException;
}
