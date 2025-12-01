#include <iostream>
#include <string>
#include <vector>

int main() {
  std::vector<std::string> msg{"Hello", "C++",      "World",
                               "from",  "Code-OSS", "and Clangd!"};

  for (const std::string &word : msg) {
    std::cout << word << " ";
  }
  std::cout << std::endl;
  return 0;
}
