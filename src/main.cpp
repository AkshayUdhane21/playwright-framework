#include <iostream>
#include "application/application.hpp"
#include <spdlog/spdlog.h>
#include <exception>
#include <cstdlib>

int main() {
    std::cout << "Yokogawa API connection service"<<std::endl;
    const std::string cfg_path = std::getenv("YOKOGAWA_CONNECTION_CONFIG_PATH") ? std::getenv("YOKOGAWA_CONNECTION_CONFIG_PATH") : "";
    if (cfg_path.empty()) {
        std::cerr << "Cannot find config file! Please check environment variable YOKOGAWA_CONNECTION_CONFIG_PATH";
        return EXIT_FAILURE;
    }
    Application app(cfg_path);
    try {
        app.run();
    }
    catch (const SetupError& e) {
        spdlog::error(e.what());
        return EXIT_FAILURE;
    }
    catch (const std::exception& e) {
        spdlog::critical("Error in running app: {}", e.what());
        return EXIT_FAILURE;
    }
    spdlog::info("Exiting the service");
    std::cout << "Service stopped, press ENTER or any other key to exit" << std::endl;
    return EXIT_SUCCESS;
}