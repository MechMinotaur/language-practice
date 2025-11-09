package com.mycompany.app.model;

import java.util.Optional;

public record CustomerModel(
        String firstName,
        String lastName,
        String email,
        Optional<String> phoneNumber) {

    public String toString() {
        return "First Name: ";
    }

}
