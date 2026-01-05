#pragma once

#include "IJsonService.hpp"
#include "ILogger.hpp"
#include <functional>
#include <iostream>
#include <limits>
#include <memory>
#include <string>
#include <vector>

class MainService {
private:
  bool running = true;
  std::shared_ptr<ILogger> logger;
  std::shared_ptr<IJsonService> jsonService;

  struct MenuOption {
    std::string label;
    std::function<void()> action;
  };

  std::vector<MenuOption> options;

  void callJsonService() {
    if (logger)
      logger->log("Calling JsonService!");
  }

  void greet() {
    logger->log("The service can perform the following:");
    for (auto i = 0; i < options.size(); i++) {
      logger->log(std::to_string(i) + ". " + options[i].label);
    }

    logger->log("Enter an option: ", true);
  }

  int getUserInput() {
    int option;
    while (true) {
      greet();

      if (!(std::cin >> option)) {
        logger->error("Invalid input, please use a valid integer.");
        std::cin.clear();
        std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
      } else if (option > options.size() || option < 0) {
        logger->error("Invalid input, please select a valid option.");
      } else {
        return option;
      }
    }
  }

public:
  MainService(std::shared_ptr<ILogger> logger,
              std::shared_ptr<IJsonService> jsonService)
      : logger(std::move(logger)), jsonService(std::move(jsonService)) {
    options = {
        {"Print Hello World!", [this]() { this->logger->log("Hello World!"); }},
        {"Print from JsonService.", [this]() { this->callJsonService(); }},
        {"Exit", [this]() { this->running = false; }}};
  }

  void run() {
    logger->log("Main Service Start.");
    while (running) {
      int option = getUserInput();

      options[option].action();
    }
  }
};
