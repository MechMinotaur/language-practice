#pragma once

#include "IJsonService.hpp"
#include "ILogger.hpp"
#include <fstream>
#include <memory>

class JsonService : public IJsonService {
private:
  std::shared_ptr<ILogger> logger;

public:
  JsonService(std::shared_ptr<ILogger> logger) : logger(std::move(logger)) {}

  nlohmann::json parse(std::ifstream &stream) {
    return nlohmann::json::parse(stream);
  }
};