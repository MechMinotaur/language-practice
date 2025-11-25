package com.mycompany.app.controller;

import java.util.Scanner;

import com.mycompany.app.view.EventView;

public class MainController {

    private final CustomerController customerController;
    private final EventView eventView;

    public MainController(CustomerController customerController, EventView eventView) {
        this.customerController = customerController;
        this.eventView = eventView;
    }

    public void StartMainLoop() {
        var scanner = new Scanner(System.in);
        var execute = true;

        while (execute) {
            this.eventView.LogInfo("Enter 'X' to exit 'L' to list and 'U' to update database.");
            var command = scanner.nextLine().toUpperCase();

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
    }
}
