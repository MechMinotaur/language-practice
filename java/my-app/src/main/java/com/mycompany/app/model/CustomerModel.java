package com.mycompany.app.model;

public record CustomerModel(
        String firstName,
        String lastName,
        String email,
        String phoneNumber) {
}
