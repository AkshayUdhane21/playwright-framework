#include "curl_handler/curl_handler.hpp"
#include <iostream>
#include <nlohmann/json.hpp>
#include <mutex>
#include <stdexcept>
#include "spdlog/spdlog.h"
#include <string>
#include <chrono>

bool curl::Handler::init_curl_global_once() {
    static std::once_flag once_flag;  // One per program
    std::call_once(once_flag, [] { // This ensures we call it once only, even in multi-threaded envs
        CURLcode res = curl_global_init(CURL_GLOBAL_DEFAULT);
        if (res != CURLE_OK) {  
            //spdlog::error("curl_global_init failed with code {}", static_cast<int>(res));
            //return false;
            throw HandlerException("Cannot initialize curl");
        }
    });

    return true;
}

template<class T>
bool curl::Handler::safe_setopt(CURLoption opt, T value){
        CURLcode everything_ok = curl_easy_setopt(this->m_handler.get(), opt, value);
        if (everything_ok != CURLE_OK){
            spdlog::error("curl options setup failed: {}", curl_easy_strerror(everything_ok));
            return false;
        }

        return true;
}

void curl::Handler::reset_buffer(){
    this->m_buffer.clear();
}

bool curl::Handler::execute_request(){
    CURLcode everything_ok = curl_easy_perform(this->m_handler.get());
    if (everything_ok != CURLE_OK) {
        spdlog::error("request execution failed: {}", curl_easy_strerror(everything_ok));
        return false;
    }

    return true;
}

size_t curl::Handler::write_to_buffer(void *contents, size_t size, size_t nmemb, void *pm_buffer){
    // This function will be called by curl multiple times
    size_t total_size = size * nmemb;
    std::string* m_buffer = static_cast<std::string*>(pm_buffer); // pm_buffer is location of m_buffer
    // Basically get the string stored at pm_buffer.
    // Note m_buffer is local, which will point to global m_buffer
    
    m_buffer->append(static_cast<char*>(contents), total_size); // it will add into global m_buffer.
    return total_size;
}

curl::Result curl::Handler::build_result(){
    curl::Result r;
    long status_code = 0;
    CURLcode everything_ok = curl_easy_getinfo(this->m_handler.get(),CURLINFO_RESPONSE_CODE, &status_code);
    if (everything_ok != CURLE_OK){
            spdlog::error("Unable to HTTP fetch status code: {}", curl_easy_strerror(everything_ok));
            //Just a return statement here...
            throw HandlerException("Unable to fetch HTTP status code");
    }else{
        r.http_code = status_code;
    }
    
    if (this->m_buffer.empty()){
        spdlog::warn("No body found");
        return UNSUCCESSFUL_CURL_EXECUTION;
    }

    try{
        r.body = nlohmann::json::parse(this->m_buffer);
    }catch(const std::exception& e){
        spdlog::error("Json Parsing failed: " + std::string(e.what()));
        r.body = {};
    }

    return r;
}

curl::Handler::Handler(long timeout) {
    init_curl_global_once();  // May throw exception

    this->m_handler.reset(curl_easy_init()); // Because we have a custom deleter, we need to use reset
    if (!this->m_handler) {
        //spdlog::error("curl_easy_init failed");
        throw HandlerException("curl initialization failed");
    }

    //spdlog::info("curl::Handler initialized successfully");

    if (!safe_setopt(CURLOPT_FOLLOWLOCATION, 1L)) {
        throw HandlerException("Unable to redirect");
    } // For redirects
    if (!safe_setopt(CURLOPT_TIMEOUT, timeout)) {
        throw HandlerException("API timeout reached");
    } // 30 second timeout
    if (!safe_setopt(CURLOPT_WRITEFUNCTION, &curl::Handler::write_to_buffer)) {
        throw HandlerException("Unable to tell curl where to write data");
    } // Which function to call while writing the data
    if (!safe_setopt(CURLOPT_WRITEDATA, &(this->m_buffer))) {
        throw HandlerException("Buffer passing to curl failed");
    } // Where to write the data
}

curl::Handler::~Handler(){
    
}

bool curl::Handler::set_api_keys(const std::string& api_key, const std::string& secret_key, const std::string& signature){
    m_header.reset(nullptr);

    if (api_key.empty()) {
        return false;
    }

    if (!signature.empty()) {

        std::string auth_header = "Authorization: " + signature;
        auto now = std::chrono::system_clock::now();
        auto seconds = std::chrono::duration_cast<std::chrono::seconds>(
            now.time_since_epoch()
        ).count();
        std::string time_header = "Timestamp: " + std::to_string(seconds);

        curl_slist* headers = nullptr;
        headers = curl_slist_append(headers, "Accept: application/json, text/plain, */*"); // For the simple reason of making it same as python request
        headers = curl_slist_append(headers, "User-Agent: python-requests/2.31.0"); // The version does not matter that mush
        headers = curl_slist_append(headers, auth_header.c_str());
        headers = curl_slist_append(headers, time_header.c_str());

        m_header.reset(headers);

        if (!safe_setopt(CURLOPT_HTTPHEADER, m_header.get())) {
            return false;
        }
    }
    
    if (secret_key.empty()) {
        std::string auth_header = "Authorization: Bearer " + api_key;
        curl_slist* header_list = curl_slist_append(nullptr, auth_header.c_str()); // No need for smart pointer here becuase we will transfer ownership to m_handler
        header_list = curl_slist_append(header_list, "Accept: application/json, text/plain, */*"); // For the simple reason of making it same as python request
        header_list = curl_slist_append(header_list, "User-Agent: python-requests/2.31.0"); // The version does not matter that mush
        if (!header_list){
            spdlog::error("Failed to set API key header");
            return false;
        }
        m_header.reset(header_list);
        if (!safe_setopt(CURLOPT_HTTPHEADER, m_header.get())) {
            return false;
        }
    } else {
        std::string userpwd = api_key + ":" + secret_key;
        if (!safe_setopt(CURLOPT_USERPWD, userpwd.c_str())) {
            return false;
        }
    }

    return true;
}


curl::Result curl::Handler::GET(const std::string& url){
    reset_buffer();
    if (!safe_setopt(CURLOPT_URL, url.c_str())) {
        return UNSUCCESSFUL_CURL_EXECUTION;
    }
    if (!safe_setopt(CURLOPT_HTTPGET, 1L)) {
        return UNSUCCESSFUL_CURL_EXECUTION;
    } // Refer these docs: https://curl.se/libcurl/c/CURLOPT_HTTPGET.html
    if (!execute_request()) {
        return UNSUCCESSFUL_CURL_EXECUTION;
    }// Execute order 66
    curl::Result r = build_result();
    return r;
}

curl::Result curl::Handler::GET(const std::string& url, const std::string& api_key) {
    if (!set_api_keys(api_key, "", "")) {
        return UNSUCCESSFUL_CURL_EXECUTION;
    }
    return GET(url);
}

curl::Result curl::Handler::GET(const std::string& url, const std::string& api_key, const std::string& secret_key, const std::string& signature) {
    if (!set_api_keys(api_key, secret_key, signature)) {
        return UNSUCCESSFUL_CURL_EXECUTION;
    }
    return GET(url);
}
