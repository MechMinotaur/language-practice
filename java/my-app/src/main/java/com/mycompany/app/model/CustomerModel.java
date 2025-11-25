package com.mycompany.app.model;

public record CustomerModel(
        Integer social,
        String firstName,
        String lastName,
        String email,
        String phoneNumber) {

}
