#include <fstream>
#include <iostream>
#include <nlohmann/json.hpp>
#include <string>
#include <vector>

int main() {
  std::string exampleJsonFP =
      std::filesystem::path(__FILE__).parent_path() / "example.json";
  std::ifstream f(exampleJsonFP);
  nlohmann::json data = nlohmann::json::parse(f);

  std::vector<std::string> msg{"Hello", "C++",      data["Hello"],
                               "from",  "Code-OSS", "and Clangd!"};

  for (const std::string &word : msg) {
    std::cout << word << " ";
  }
  std::cout << std::endl;

  return 0;
}
