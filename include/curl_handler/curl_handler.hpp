#pragma once
#include <curl/curl.h>

#include <nlohmann/json.hpp>
#include <memory>
#include <string>
#include <mutex>
#include <exception>

namespace curl {

    using CurlEasyPtr = std::unique_ptr<CURL, decltype(&curl_easy_cleanup)>;
    using HeaderPtr = std::unique_ptr<curl_slist, decltype(&curl_slist_free_all)>;
    using Seconds = long;

    struct Result {
        long http_code = 0;
        nlohmann::json body;
        bool successfull_req = true;
    };

    static const Result UNSUCCESSFUL_CURL_EXECUTION = { 503, nlohmann::json{}, false};


    class HandlerException : public std::exception {
    private:
        std::string message;
    public:
        explicit HandlerException(const std::string& msg) : message(msg) {}
        const char* what() const noexcept override {
            return message.c_str();
        }

    };

    class Handler {

    private:
        static std::once_flag m_init_curl_flag;

        CurlEasyPtr m_handler{nullptr, &curl_easy_cleanup};
        HeaderPtr m_header{nullptr, &curl_slist_free_all};
        std::string m_buffer;
        std::string m_username_password;

        bool init_curl_global_once(); // Singleton type pattern to make it thread safe. Also, this needs to be called only once in global scope
        template<class T>
        bool safe_setopt(CURLoption opt, T value); // Safe way to set params
        void reset_buffer(); // To reset the buffer string
        bool execute_request(); // Make request

        //contents: A pointer to the received data chunk.
        //size: The size of each data element 
        //nmemb: The number of data elements received.
        //pm_buffer: A user-defined pointer, passed via CURLOPT_WRITEDATA, allowing the callback to access application-specific data
        //We need to use static to remove "this" keyword, which is hidden in cpp functions.
        //We need to remove "this" because curllib requires an exact signature and its a C function, which does not have "this"
        //Will be passed to a curl's inbuilt function, which require a size_t datatype
        static size_t write_to_buffer(void *contents, size_t size, size_t nmemb, void *pm_buffer); 

        Result build_result(); // Build the results

    public:
        Handler();
        Handler(long timeout);
        ~Handler();
        Handler(Handler&&) = delete; // Move Constructor
        Handler(const curl::Handler&) = delete; // Copy Constructor
        Handler& operator=(curl::Handler&&) = delete; // Move Assignment
        Handler& operator=(const curl::Handler& other) = delete; // Copy assignment

        static const Seconds DEFAULT_CURL_TIMEOUT = 30; // seconds

        bool set_api_keys(const std::string& api_key, const std::string& secret_key, const std::string& signature);
        Result GET(const std::string& url); // GET method
        Result GET(const std::string& url, const std::string& api_key);
        Result GET(const std::string& url, const std::string& api_key, const std::string& secret_key, const std::string& signature);
    };
}

