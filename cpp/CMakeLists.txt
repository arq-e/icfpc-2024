cmake_minimum_required(VERSION 3.14)
project(cpp_solver VERSION 0.1)

set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_STANDARD_REQUIRED True)

include(FetchContent)
FetchContent_Declare(json URL https://github.com/nlohmann/json/releases/download/v3.11.3/json.tar.xz)
FetchContent_MakeAvailable(json)

FetchContent_Declare(argparse GIT_REPOSITORY https://github.com/p-ranav/argparse.git)
FetchContent_MakeAvailable(argparse)

add_executable(spaceship src/spaceship/main.cpp)
add_executable(lambdaman src/lambdaman/main.cpp)
target_link_libraries(spaceship PRIVATE nlohmann_json::nlohmann_json argparse)