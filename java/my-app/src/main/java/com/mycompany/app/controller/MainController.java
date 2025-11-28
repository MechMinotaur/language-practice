package com.mycompany.app.controller;

import java.util.Scanner;

import com.mycompany.app.view.EventDisplay;

public class MainController {

    private final CustomerManager customerController;
    private final EventDisplay eventView;
    private final Scanner scanner;

    public MainController(CustomerManager customerController, EventDisplay eventView, Scanner scanner) {
        this.customerController = customerController;
        this.eventView = eventView;
        this.scanner = scanner;
    }

    public void StartMainLoop() {
        var execute = true;

        while (execute) {
            this.eventView.LogInfo("Enter 'X' to exit 'L' to list and 'U' to update database.");
            var command = this.scanner.nextLine().toUpperCase();

            switch (command) {
                case "X" ->
                    execute = false;
                case "L" ->
                    this.customerController.displayAllLocalCustomers();
                case "U" ->
                    this.customerController.updateLocalCustomers();
                default ->
                    this.eventView.LogWarning("Unknown command.");
            }

        }

        this.scanner.close();
    }
}
