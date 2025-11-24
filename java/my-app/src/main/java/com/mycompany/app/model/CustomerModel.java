package com.mycompany.app.model;

public record CustomerModel(
        Integer social,
        String firstName,
        String lastName,
        String email,
        String phoneNumber) {

    public static CustomerModel FromRemote(CustomerModel localCustomer, CustomerModel remoteCustomer) {
        return new CustomerModel(
                localCustomer.social == null ? remoteCustomer.social() : localCustomer.social(),
                localCustomer.firstName == null ? remoteCustomer.firstName() : localCustomer.firstName(),
                localCustomer.lastName == null ? remoteCustomer.lastName() : localCustomer.lastName(),
                localCustomer.email == null ? remoteCustomer.email() : localCustomer.email(),
                localCustomer.phoneNumber == null ? remoteCustomer.phoneNumber() : localCustomer.phoneNumber());
    }
}
