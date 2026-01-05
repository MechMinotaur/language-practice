#pragma once

#include "ILogger.hpp"
#include <iostream>

class ConsoleLogger : public ILogger {
public:
  void log(const std::string &message) override { log(message, false); }

  void log(const std::string &message, bool noNewLine) override {
    if (noNewLine) {
      std::cout << "LOG: " << message;
    } else {
      std::cout << "LOG: " << message << std::endl;
    }
  }

  void error(const std::string &message) override {
    std::cout << "ERR: " << message << std::endl;
  }
};