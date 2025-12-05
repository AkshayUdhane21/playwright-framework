#include <gtest/gtest.h>
#include "config/config.hpp"
#include <fstream>
#include <spdlog/spdlog.h>
#include <spdlog/sinks/stdout_color_sinks.h>

class ConfigTest : public ::testing::Test {
protected:
	std::string tempFilePath = "test_config.json";
	std::string tempLogfile = "config_test.log";
	void SetUp() override {
		std::ofstream tempLogger(this->tempLogfile);
		std::ofstream out(this->tempFilePath);
		auto console_sink = std::make_shared<spdlog::sinks::stdout_color_sink_mt>();
		auto logger = std::make_shared<spdlog::logger>("daily_logger",
			spdlog::sinks_init_list{ console_sink });

		spdlog::set_default_logger(logger);
		logger->set_level(spdlog::level::info);
		logger->flush_on(spdlog::level::info);
		spdlog::set_pattern("{\"timestamp\":\"%Y-%m-%dT%H:%M:%S.%eZ\",\"level\":\"%l\",\"thread\":%t,\"msg\":\"%v\"}");

		out << R"({
            "port": 8080,
            "name": "hello_json"
        })";
	}

	void TearDown() override {
		std::remove(this->tempFilePath.c_str());
		std::remove(this->tempLogfile.c_str());
		spdlog::shutdown();
	}
};

TEST_F(ConfigTest, LoadsValidConfig) {
	Config cfg;
	{
		SCOPED_TRACE("Testing correct config file path");
		EXPECT_EQ(cfg.load(tempFilePath), true);
	}
}
TEST_F(ConfigTest, FailsLoadingInvalidConfig) {
	Config cfg;
	{
		SCOPED_TRACE("Testing incorrect config file path");
		EXPECT_EQ(cfg.load("hello.txt"), false);
	}
}

TEST_F(ConfigTest, GetsIntConfigElementCorrectly) {
	Config cfg;
	ASSERT_TRUE(cfg.load(tempFilePath));

	{
		SCOPED_TRACE("Checking valid string key retrieval");
		EXPECT_EQ(cfg.get<int>("port"), 8080);
	}
}

TEST_F(ConfigTest, GetsStringConfigElementCorrectly) {
	Config cfg;
	ASSERT_TRUE(cfg.load(tempFilePath));
	{
		SCOPED_TRACE("Checking valid string key retrieval");
		EXPECT_EQ(cfg.get<std::string>("name"), "hello_json");
	}
}

TEST_F(ConfigTest, NonExistingStringKey) {
	Config cfg;
	ASSERT_TRUE(cfg.load(tempFilePath));
	{
		SCOPED_TRACE("Checking non-existing string key");
		std::string incorrectGetString = cfg.get<std::string>("IDontExist");
		EXPECT_EQ(incorrectGetString, std::string{});
	}
}

TEST_F(ConfigTest, NonExistingIntKey) {
	Config cfg;
	ASSERT_TRUE(cfg.load(tempFilePath));
	{
		SCOPED_TRACE("Checking non-existing int key");
		int incorrectGetInt = cfg.get<int>("IDontExist");
		EXPECT_EQ(incorrectGetInt, 0);
	}
}