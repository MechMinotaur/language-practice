cmake_minimum_required(VERSION 3.12)
project(my-app LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Required for clangd IntelliSense
set(CMAKE_EXPORT_COMPILE_COMMANDS ON)

add_executable(app_executable main.cpp)

# For distribution:
install(TARGETS app_executable DESTINATION bin)
