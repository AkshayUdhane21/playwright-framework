#include "application/application.hpp"
#include "curl_handler/curl_handler.hpp"
#include "config/config.hpp"
#include <open62541pp/open62541pp.hpp>
#include <spdlog/spdlog.h>
#include <spdlog/sinks/daily_file_sink.h>
#include "security/security.hpp"
#include <iostream>
#include <chrono>
#include <memory>

void Application::setup_logger() {
    // Setting up logger
    const std::string log_path = std::getenv("YOKOGAWA_LOG_PATH") ? std::getenv("YOKOGAWA_LOG_PATH") : "";
    if (log_path.empty()) {
        std::cerr << "Cannot find config file! Please check environment variable YOKOGAWA_LOG_PATH";
        throw SetupError("Error in configuration file: " + log_path);
    }
    auto daily_sink = std::make_shared<spdlog::sinks::daily_file_sink_mt>(
        log_path, 0, 0, false);
    auto logger = std::make_shared<spdlog::logger>("daily_logger",
        spdlog::sinks_init_list{ daily_sink });
    spdlog::set_default_logger(logger);
    logger->set_level(spdlog::level::info);
    logger->flush_on(spdlog::level::info);
    spdlog::set_pattern("{\"timestamp\":\"%Y-%m-%dT%H:%M:%S.%eZ\",\"level\":\"%l\",\"thread\":%t,\"msg\":\"%v\"}");
}

void Application::setup_config(const std::string& cfg_path) {
    spdlog::info("Loading config: {}", cfg_path);
    this->m_cfg.load(cfg_path);
	// Application parameters
    this->m_delay_ms = this->m_cfg.get<int>("delay_ms");
    this->m_retry_count = (this->m_cfg.has("retry_count")) ? this->m_cfg.get<int>("retry_count") : this->DEFAULT_RETRY_COUNT;
	// Curl handler parameters
    this->m_api_key = this->m_cfg.get<std::string>("api_key");
    this->m_secret_key = this->m_cfg.get<std::string>("secret_key");
    this->m_yokogawa_api_timout_sec = this->m_cfg.get<long>("yokogawa_api_timeout_sec");
	this->m_base_url = this->m_cfg.get<std::string>("base_url");
	this->m_http_verb = this->m_cfg.get<std::string>("http_verb");
	this->m_api_request = this->m_cfg.get<std::string>("api_request");
	// PLC parameters
	this->m_plc_ip = this->m_cfg.get<std::string>("ip");
    this->m_namespace = this->m_cfg.get<int>("namespace");
    this->m_plctag = this->m_cfg.get<std::string>("plc_tag");
}

void Application::validate_config(const std::string& cfg_path) {
	// Validating application parameters
    if (this->m_delay_ms < 0) {
        spdlog::critical("delay_ms is invalid (Value: {}); Defaulting to {}ms", this->m_delay_ms, DEFAULT_DELAY_MS);
        this->m_delay_ms = DEFAULT_DELAY_MS;
    }
    if (this->m_retry_count < 0) {
        this->m_retry_count = this->DEFAULT_RETRY_COUNT;
	}
	// Validating curl handler parameters
    if (this->m_api_key.empty()) {
        spdlog::critical("API Key is invalid (Value: {})", this->m_api_key);
        throw SetupError("Error in configuration file: " + cfg_path);
	}
    if (this->m_secret_key.empty()) {
        spdlog::critical("Secret Key is invalid (Value: {})", this->m_secret_key);
        throw SetupError("Error in configuration file: " + cfg_path);
	}
    if (this->m_yokogawa_api_timout_sec < 1) {
        spdlog::critical("Invalid api timout set (Value: {}); Defaulting to {}s", this->m_yokogawa_api_timout_sec, curl::Handler::DEFAULT_CURL_TIMEOUT);
        this->m_yokogawa_api_timout_sec = curl::Handler::DEFAULT_CURL_TIMEOUT;
    }
    if (this->m_base_url.empty()) {
        spdlog::critical("Base URL is invalid (Value: {})", this->m_base_url);
        throw SetupError("Error in configuration file: " + cfg_path);
    }
    if (this->m_http_verb.empty()) {
        spdlog::critical("HTTP Verb is invalid (Value: {})", this->m_http_verb);
        throw SetupError("Error in configuration file: " + cfg_path);
	}
    if (this->m_api_request.empty()) {
        spdlog::critical("API Request is invalid (Value: {})", this->m_api_request);
        throw SetupError("Error in configuration file: " + cfg_path);
    }
	// Validating PLC parameters
    if (this->m_plc_ip.empty()) {
        spdlog::critical("PLC IP is invalid (Value: {})", this->m_plc_ip);
        throw SetupError("Error in configuration file: " + cfg_path);
	}
    if (this->m_namespace < 1) {
        spdlog::critical("Namespace value is invalid (Value: {})", this->m_namespace);
        throw SetupError("Error in configuration file: " + cfg_path);
    }
    if (this->m_plctag.empty()) {
        spdlog::critical("PLC Tag is invalid (Value: {})", this->m_plctag);
        throw SetupError("Error in configuration file: " + cfg_path);
    }
}

// Perform the entire setup
Application::Application(const std::string& cfg_path) : m_run_until(true) {
    setup_logger();
    // Setting up config
    setup_config(cfg_path);
    // Setting up necessary parameters received from config
    validate_config(cfg_path);
    // setup curl handler
    this->m_handler = std::make_unique<curl::Handler>(this->m_yokogawa_api_timout_sec);
}

bool Application::try_connect_to_plc() {
    this->m_plc_client.config();
    // Prepare parameters if rack/slot exist

    for (int retry = 0; retry < m_retry_count; ++retry) {

        // Attempt connection (two possible signatures)
        try {
            this->m_plc_client.connect(m_plc_ip);
            return true;
        } catch (opcua::BadDisconnect& ex) {
            spdlog::critical("Failed to establish connection to OPC-UA server at {}", m_plc_ip);
        } catch (opcua::BadStatus& ex) {
            spdlog::critical("OPC-UA connection error (Status: {})", ex.what());
        }

        // ---- Exponential backoff ----
        int delay = 1 << retry;                    // 1, 2, 4, 8, ...
        delay = (delay < this->DEFAULT_RETRY_MAX_SECONDS) ? delay : this->DEFAULT_RETRY_MAX_SECONDS; // Clamp to your configured max
        spdlog::warn("OPC-UA connection retry scheduled. Retrying in {}s (attempt {}/{})", delay, retry + 1, m_retry_count);
        std::this_thread::sleep_for(std::chrono::seconds(delay));
    }

    return false;
}

// Destroy the setup
Application::~Application() {
    this->m_plc_client.disconnect();
    this->m_run_until = false;
    spdlog::shutdown();
}

inline void Application::stop() {
    this->m_run_until = false;
}

// Run the business logic
void Application::run() {
    spdlog::info("Yokogawa heartbeat service started successfully");
    
    const std::string url = this->m_base_url + this->m_api_request;
    const std::string message = this->m_http_verb + this->m_api_request;
    const std::string signature = compute_hmac_sha256(this->m_secret_key, message);
    
    opcua::NodeId yokogawa_nodeid_(this->m_namespace, this->m_plctag);
    opcua::Node yokogawa_node(this->m_plc_client, yokogawa_nodeid_);

    spdlog::info("OPC-UA Node selected: {}", std::string(yokogawa_nodeid_.toString()));
	spdlog::info("Attempting connection to PLC at {}", this->m_plc_ip);
    if (!try_connect_to_plc()) {
        spdlog::error("Unable to establish initial connection to PLC. Service shutting down");
        this->stop();
        return;
    }
    // Start a conditionally infinite loop
    spdlog::info("Heartbeat interval configured: {}ms", this->m_delay_ms);
    while (this->m_run_until) {
        //auto before = std::chrono::high_resolution_clock::now();
        try {
            curl::Result r = m_handler->GET(url, this->m_api_key, this->m_secret_key, signature);
            spdlog::debug("HTTP response received (Status Code: {})", r.http_code);
            auto dv = yokogawa_node.readValue();
            if (dv.empty()) {
                spdlog::warn("Unable to read current value from PLC node");
                wait();
                continue;
            }
            auto yokogawa_bit = dv.to<bool>();
            if (r.http_code == 200) {
                if (!yokogawa_bit) {
                    spdlog::info("Health check passed. Updating PLC node status to HEALTHY");
                    yokogawa_node.writeValue(opcua::Variant{ true });
                }
            }
            else {
                if (yokogawa_bit) {
                    spdlog::info("Health check failed (HTTP {}). Updating PLC node status to UNHEALTHY", r.http_code);
                    yokogawa_node.writeValue(opcua::Variant{ false });
                }
            }
        }
        catch (const curl::HandlerException& ex) {
            spdlog::error("HTTP handler error: {}", ex.what());
        }
        catch (opcua::BadStatus& ex) {
            spdlog::error("OPC-UA operation failed: {}", ex.what());
        }
        catch (opcua::BadVariantAccess& ex) {
            spdlog::error("Type mismatch error. PLC tag '{}' is not of type Boolean. Service shutting down", this->m_plctag);
            this->stop();
        }
        catch (std::exception& ex) {
            spdlog::critical("Unexpected error encountered: {}", ex.what());
            this->stop();
        }
        wait();
    }
}