#pragma once
#include <nlohmann/json.hpp>
#include <spdlog/spdlog.h>

class Config {
private:
    using json = nlohmann::json;
    json cfg;
public:
    bool load(const std::string& path);
    template<typename T>
    T get(const std::string& key) const {
        if (this->cfg.contains(key)) {
            try {
                T result = cfg[key].get<T>();
                return result;
            }
            catch (const spdlog::spdlog_ex& ex) {
                spdlog::critical("Config Error: {}", ex.what());
                return T{};
            }
        }
        else {
            spdlog::warn("Key not found (Key: " + key + ")");
            return T{};
        }
    }

    bool has(const std::string& key);
};