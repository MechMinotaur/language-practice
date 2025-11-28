/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.app.view;

import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mycompany.app.model.CustomerModel;

/**
 *
 * @author tb
 */
public class CustomerViewTest {

    private CustomerView customerView;

    // It's rebuilt each time Setup is called.
    @SuppressWarnings("NonConstantLogger")
    private Logger logger;

    @Captor
    private ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);

    // This is part of JUnit.
    @BeforeEach
    @SuppressWarnings("unused")
    void Setup() {
        this.logger = Mockito.mock(Logger.class);
        this.customerView = new CustomerView(this.logger);
    }

    @ParameterizedTest
    @MethodSource("com.mycompany.app.TestData#GetCustomerData")
    void TestDisplayCustomersLogsForEachCustomer(Iterable<CustomerModel> customers, int numCustomers) {
        this.customerView.displayCustomers(customers);
        verify(this.logger, times(numCustomers)).info(anyString());
    }

    @Test
    void TestDisplayCustomersThrowsWhenCustomersNull() {
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> this.customerView.displayCustomers(null));

        assertEquals("Customers cannot be null.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, -1, 99 })
    void TestDisplayNumberCustomersUpdatedCallsInfoWithNumberUpdated(int updated) {
        this.customerView.displayNumberCustomersUpdated(updated);

        verify(this.logger).info(stringCaptor.capture());
        var capturedString = stringCaptor.getValue();
        assertTrue(capturedString.contains(Integer.toString(updated)));
    }

}
