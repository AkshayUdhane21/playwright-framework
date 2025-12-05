#include <fstream>
#include "config/config.hpp"
#include <exception>

bool Config::load(const std::string& cfg_file) {
    std::ifstream cfg_input(cfg_file);
    if (!cfg_input.is_open()) {
        std::string errorMsg = "Error while opening " + cfg_file;
        spdlog::error(errorMsg);
        return false;
    }
    try {
        cfg_input >> this->cfg;
    }
    catch (const std::exception& e) {
        spdlog::critical("Error while loading cfg from input stream: {}", e.what());
        return false;
    }
    return true;
}

bool Config::has(const std::string& key) {
    return this->cfg.contains(key);
}