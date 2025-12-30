#pragma once

#include "ILogger.hpp"
#include <iostream>
#include <limits>
#include <memory>

class MainService {
private:
  std::shared_ptr<ILogger> logger;

  int getUserInput() {
    int option;
    bool badOption = true;
    do {
      logger->log("Enter an option:");
      logger->log("1. Print Hello World!");
      logger->log("2. Exit");

      if (!(std::cin >> option)) {
        logger->error("Invalid input, please use a valid integer.");
        std::cin.clear();
        std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
      } else {
        badOption = false;
      }

    } while (badOption);

    return option;
  }

public:
  MainService(std::shared_ptr<ILogger> logger) : logger(std::move(logger)) {}

  void run() {
    logger->log("Main Service Start.");
    bool run = true;
    while (run) {

      int option = getUserInput();

      switch (option) {
      case 1:
        logger->log("Hello world!");
        break;
      case 2:
        run = false;
        break;
      default:
        logger->error("Invalid input, try again.");
      }
    }
  }
};