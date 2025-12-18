#pragma once
#include "curl_handler/curl_handler.hpp"
#include "config/config.hpp"
#include <exception>
#include <memory>

// On Windows, <windows.h> may define min/max macros that break std::min/std::max
#if defined(min)
#undef min
#endif
#if defined(max)
#undef max
#endif

#include <open62541pp/open62541pp.hpp>

class SetupError : std::exception {
private:
	std::string message;

public:
	explicit SetupError(const std::string& msg) : message(msg) {}
	const char* what() const noexcept override {
		return message.c_str();
	}
};

class Application {
private:
	const int DEFAULT_RETRY_COUNT = 4;
	const int DEFAULT_RETRY_MAX_SECONDS = 8;
	const int DEFAULT_DELAY_MS = 1000;

	opcua::Client m_plc_client;
	Config m_cfg;
	std::string m_plc_ip;

	std::unique_ptr<curl::Handler> m_handler;
	std::string m_api_key, m_secret_key, m_base_url, m_api_request, m_http_verb;
	int m_yokogawa_api_timout_sec;

	int m_delay_ms = -1;
	int m_namespace = -1;
	std::string m_plctag;
	bool m_run_until;

	int m_retry_count = -1;

	inline void wait() {
		std::this_thread::sleep_for(std::chrono::milliseconds(this->m_delay_ms));
	}

	void setup_logger();
	void setup_config(const std::string& cfg_path);
	void validate_config(const std::string& cfg_path);

public:
	Application() = delete;
	Application(const std::string& cfg_path);
	~Application();
	Application(const Application& other) = delete;
	Application(Application&& value) = delete;
	Application& operator=(const Application& other) = delete;
	Application& operator=(Application&& value) = delete;

	bool try_connect_to_plc();
	void run();
	inline void stop();
};