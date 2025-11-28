package com.mycompany.app;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

import com.mycompany.app.model.CustomerModel;

public class TestData {

    public static Stream<Arguments> GetCustomerData() {
        var leo = new CustomerModel(111111111, "Leo", "Yui", "leo.yui@email.com", "123-456-7890");
        var bill = new CustomerModel(222222222, "Bill", "George", "bill.geroge@email.com", null);
        var abcde = new CustomerModel(333333333, "Abcde", "Hendrix", "abcde.hendrix@email.com", "256-123-4567");
        var lorum = new CustomerModel(444444444, "Lorum", "Ipsum", "lorum.ipsum@email.com", null);

        var allCustomers = Arrays.asList(leo, bill, abcde, lorum);
        var emptyCustomers = Collections.emptyList();

        return Stream.of(
                Arguments.of(allCustomers, allCustomers.size()),
                Arguments.of(emptyCustomers, emptyCustomers.size()));
    }
}
