#pragma once

#include "ILogger.hpp"
#include <iostream>

class ConsoleLogger : public ILogger {
public:
  void log(const std::string &message) override {
    std::cout << "LOG: " << message << std::endl;
  }

  void error(const std::string &message) override {
    std::cout << "ERR: " << message << std::endl;
  }
};