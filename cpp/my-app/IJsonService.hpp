#pragma once

#include <fstream>
#include <nlohmann/json.hpp>

class IJsonService {

public:
  virtual ~IJsonService() = default;
  virtual nlohmann::json parse(std::ifstream &stream) = 0;
};