// main.cpp
#include "curl_handler/curl_handler.hpp"
#include <nlohmann/json.hpp>
#include <openssl/hmac.h>
#include <openssl/evp.h>
#include <iomanip>
#include <sstream>
#include <cassert>
#include <iostream>
#include <chrono>

namespace {
    // ------------------------------------------------------------
    // Helper: HMAC-SHA256 (exact match with Python version)
    // ------------------------------------------------------------
    std::string hmac_sha256(const std::string& data, const std::string& key)
    {
        unsigned char hash[EVP_MAX_MD_SIZE];
        unsigned int hashLen = 0;

        HMAC(EVP_sha256(),
            key.c_str(), static_cast<int>(key.size()),
            reinterpret_cast<const unsigned char*>(data.c_str()), data.size(),
            hash, &hashLen);

        std::ostringstream oss;
        oss << std::hex << std::setfill('0');
        for (unsigned int i = 0; i < hashLen; ++i)
            oss << std::setw(2) << static_cast<unsigned>(hash[i]);
        return oss.str();
    }

    // ------------------------------------------------------------
    // Build payload exactly like the Python server: "GET" + path + "?sorted_query"
    // ------------------------------------------------------------
    std::string make_payload(const std::string& path, const std::map<std::string, std::string>& query = {})
    {
        std::string payload = "GET" + path;

        if (!query.empty()) {
            std::vector<std::pair<std::string, std::string>> sorted(query.begin(), query.end());
            std::sort(sorted.begin(), sorted.end());

            std::ostringstream qs;
            for (size_t i = 0; i < sorted.size(); ++i) {
                if (i > 0) qs << "&";
                qs << sorted[i].first << "=" << sorted[i].second;
            }
            payload += "?" + qs.str();
        }
        return payload;
    }
} // anonymous namespace
void test_get_with_signature_success()
{
    std::cout << "\n=== TEST: GET with Signature + Timestamp – SUCCESS ===\n";

    const std::string URL = "http://127.0.0.1:8080/protected";
    const std::string API_KEY = "demo_key";        // not used in header
    const std::string SECRET_KEY = "super_secret_123";

    // Query params
    std::map<std::string, std::string> query = { {"page", "1"}, {"limit", "10"} };

    // Build full URL
    std::ostringstream url_ss;
    url_ss << URL;

    if (!query.empty()) {
        url_ss << "?";
        bool first = true;
        for (const auto& pair : query) {
            if (!first) url_ss << "&";
            url_ss << pair.first << "=" << pair.second;
            first = false;
        }
    }
    std::string full_url = url_ss.str();

    // Build payload (same as server)
    std::string payload = "GET/protected";
    if (!query.empty()) {
        std::vector<std::pair<std::string, std::string>> sorted(query.begin(), query.end());
        std::sort(sorted.begin(), sorted.end());
        std::ostringstream qs;
        for (size_t i = 0; i < sorted.size(); ++i) {
            if (i > 0) qs << "&";
            qs << sorted[i].first << "=" << sorted[i].second;
        }
        payload += "?" + qs.str();
    }

    // Add timestamp
    auto now = std::chrono::system_clock::now();
    auto seconds = std::chrono::duration_cast<std::chrono::seconds>(now.time_since_epoch()).count();
    std::string timestamp = std::to_string(seconds);

    // Final data to sign
    std::string data_to_sign = payload + timestamp;
    std::string signature = hmac_sha256(data_to_sign, SECRET_KEY);

    std::cout << "Payload  : " << payload << "\n";
    std::cout << "Timestamp: " << timestamp << "\n";
    std::cout << "Signed   : " << data_to_sign << "\n";
    std::cout << "Signature: " << signature << "\n";

    // Send request
    curl::Handler curl(30);
    curl::Result r = curl.GET(full_url, API_KEY, SECRET_KEY, signature);

    std::cout << "HTTP status : " << r.http_code << "\n";
    assert(r.http_code == 200);
    assert(r.body["message"].get<std::string>() == "authenticated");
    assert(r.body["signed_data"].get<std::string>() == data_to_sign);

    std::cout << "OK\n";
}

// void test_no_auth() {
//     std::cout << "\n=== TEST: No Auth (jsonplaceholder) ===\n";
//     curl::Handler curl(30);
//     auto now = std::chrono::high_resolution_clock::now();
//     curl::Result r = curl.GET("http://localhost:5000/random_status");
//     auto after = std::chrono::high_resolution_clock::now();

//     auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(after - now);
//     long time_taken_ms = duration.count();

//     std::cout << "Time taken: " << time_taken_ms << " ms\n";

//     std::cout << "HTTP: " << r.http_code << "\n";
//     assert(r.http_code == 200);
//     // This approach
//     if (r.body.contains("asfs")) {
//         std::cout << "message: " << r.body["asfs"].get<std::string>() << std::endl;;
//     } else {
//         std::cerr << "Error body is empty!\n";
//     }
//     // VS
//         auto resp = r.body.value("asfs", "DEFAULT");
//     if (resp == "DEFAULT") {
//         std::cerr << "ERROR!\n";
//     }
//     //
//     std::cout << "OK\n";
// }

void test_auth_success() {
    std::cout << "\n=== TEST: Auth Success (httpbin) ===\n";
    curl::Handler curl(30);
    curl::Result r = curl.GET("https://httpbin.org/basic-auth/user/passwd", "user", "passwd", "");

    std::cout << "HTTP: " << r.http_code << "\n";
    assert(r.http_code == 200);
    std::cout << "Authenticated: " << r.body["authenticated"].get<bool>() << "\n";
    std::cout << "OK\n";
}

void test_auth_fail() {
    std::cout << "\n=== TEST: Auth Fail (wrong password) ===\n";
    curl::Handler curl(30);
    curl::Result r = curl.GET("https://httpbin.org/basic-auth/user/passwd", "user", "Wrong", "");

    std::cout << "HTTP: " << r.http_code << " (expected 401)\n";
    assert(r.http_code == 401);
    std::cout << "OK\n";
}

void test_bearer_success() {
    std::cout << "\n=== TEST: Bearer Auth Success (httpbin) ===\n";
    curl::Handler curl(30);

    // httpbin expects token "mytoken" to return authenticated:true
    curl::Result r = curl.GET("https://httpbin.org/bearer", "mytoken");

    std::cout << "HTTP: " << r.http_code << "\n";
    assert(r.http_code == 200);

    std::cout << "Authenticated: " << r.body["authenticated"].get<bool>() << "\n";
    assert(r.body["authenticated"].get<bool>() == true);

    std::cout << "Token presented: " << r.body["token"].get<std::string>() << "\n";
    assert(r.body["token"].get<std::string>() == "mytoken");

    std::cout << "OK\n";
}

void test_bearer_fail() {
    std::cout << "\n=== TEST: Bearer Auth Fail (wrong token) ===\n";
    curl::Handler curl(30);

    // Wrong token intentionally
    curl::Result r = curl.GET("https://httpbin.org/bearer", "wrong_token");

    std::cout << "HTTP: " << r.http_code << " (expected 401)\n";
    assert(r.http_code == 401);

    std::cout << "OK\n";
}


void test_redirect() {
    std::cout << "\n=== TEST: Redirect (3 hops) ===\n";
    curl::Handler curl(30);
    curl::Result r = curl.GET("https://httpbin.org/redirect/3");
    std::cout << "Final HTTP: " << r.http_code << " (should be 200)\n";
    assert(r.http_code == 200);
    std::cout << "OK\n";
}

void test_404() {
    std::cout << "\n=== TEST: 404 handling ===\n";
    curl::Handler curl(30);
    curl::Result r = curl.GET("https://httpbin.org/status/404");
    std::cout << "HTTP: " << r.http_code << "\n";
    assert(r.http_code == 404);
    assert(r.body.is_null());
    std::cout << "OK\n";
}

// void test_move_constructor() {
//     std::cout << "\n=== TEST: Move Constructor ===\n";
//     curl::Handler a;
//     curl::Handler b = std::move(a);  // ← Uses your = default move ctor
//     curl::Result r = b.GET("http://localhost:5000/random_status");
//     std::cout << "After move, HTTP: " << r.http_code << "\n";
//     assert(r.http_code == 200);
//     std::cout << "OK\n";
// }

// void test_move_assignment() {
//     std::cout << "\n=== TEST: Move Assignment ===\n";
//     curl::Handler a;
//     curl::Handler b;
//     b = std::move(a);  // ← Uses your = default move assign
//     curl::Result r = b.GET("https://httpbin.org/ip");
//     std::cout << "After move-assign, origin: " << r.body["origin"].get<std::string>() << "\n";
//     std::cout << "OK\n";
// }

void test_thread_safety() {
    std::cout << "\n=== TEST: Thread Safety (10 threads) ===\n";
    std::vector<std::thread> threads;
    for (int i = 0; i < 10; ++i) {
        threads.emplace_back([] {
            curl::Handler curl(30);
            curl::Result r = curl.GET("https://jsonplaceholder.typicode.com/posts/1");
            assert(r.http_code == 200);
            // std::cout << "Thread UUID: " << r.body["uuid"].get<std::string>() << "\n";
        });
    }
    for (auto& t : threads) t.join();
    std::cout << "All threads OK\n";
}

// void test_container() {
//     std::cout << "\n=== TEST: Store in vector ===\n";
//     std::vector<curl::Handler> handlers;
//     handlers.emplace_back();                    // default ctor
//     handlers.emplace_back("user", "passwd");     // auth ctor
//     handlers.push_back(std::move(handlers[0]));  // move
//     std::cout << "Vector size: " << handlers.size() << "\n";
//     std::cout << "OK\n";
// }

void test_large_json() {
    std::cout << "\n=== TEST: Large JSON (1000 items) ===\n";
    curl::Handler curl(30);
    curl::Result r = curl.GET("https://jsonplaceholder.typicode.com/comments");
    std::cout << "HTTP: " << r.http_code << "\n";
    assert(r.http_code == 200);

    // Must be an array and non-empty
    assert(r.body.is_array());
    assert(r.body.size() >= 100);

    std::cout << "OK\n";
}


void test_chunked_stream_json() {
    std::cout << "\n=== TEST: Chunked JSON Stream ===\n";
    curl::Handler curl(30);
    curl::Result r = curl.GET("https://httpbin.org/json");
    std::cout << "HTTP: " << r.http_code << "\n";
    assert(r.http_code == 200);

    assert(r.body.is_object());
    assert(r.body.contains("slideshow"));

    std::cout << "OK\n";
}


void test_json_schema_like() {
    std::cout << "\n=== TEST: JSON Structure Validation ===\n";
    curl::Handler curl(30);
    curl::Result r = curl.GET("https://jsonplaceholder.typicode.com/posts/1");
    assert(r.http_code == 200);

    assert(r.body.is_object());
    assert(r.body.contains("userId"));
    assert(r.body.contains("id"));
    assert(r.body.contains("title"));
    assert(r.body.contains("body"));

    std::cout << "OK\n";
}


void test_bad_tls() {
    std::cout << "\n=== TEST: TLS Failure ===\n";
    curl::Handler curl(30);
    curl::Result r = curl.GET("https://self-signed.badssl.com/");
    std::cout << "HTTP: " << r.http_code << "\n";
    assert(r.http_code == 0); // connection should fail
    std::cout << "OK\n";
}

// void test_use_after_move() {
//     std::cout << "\n=== TEST: Use After Move ===\n";
//     curl::Handler a;
//     curl::Handler b = std::move(a);
//     curl::Result r = a.GET("https://httpbin.org/ip"); // this must NOT crash
//     std::cout << "HTTP: " << r.http_code << "\n";
//     std::cout << "OK\n";
// }

void test_nested_json_array() {
    std::cout << "\n=== TEST: Nested JSON Array ===\n";
    curl::Handler curl(30);
    curl::Result r = curl.GET("https://jsonplaceholder.typicode.com/users");
    assert(r.http_code == 200);
    assert(r.body.is_array());
    assert(r.body[0].contains("address"));
    assert(r.body[0]["address"].contains("geo"));
    std::cout << "OK\n";
}


void test_thread_stress() {
    std::cout << "\n=== TEST: Thread Stress (200 threads) ===\n";
    std::vector<std::thread> threads;
    threads.reserve(200);
    for (int i = 0; i < 200; ++i) {
        threads.emplace_back([] {
            curl::Handler curl(30);
            auto r = curl.GET("https://jsonplaceholder.typicode.com/posts/1");
            assert(r.http_code == 200);
        });
    }
    for (auto& t : threads) t.join();
    std::cout << "OK\n";
}


void test_get_with_signature_fail() {
    std::cout << "\n=== TEST: GET with Signature Fail ===\n";
    curl::Handler curl(30);

    std::string api_key = "my_api_key";
    std::string secret_key = "my_secret_key";
    std::string signature = ""; // missing signature should cause failure

    curl::Result r = curl.GET("https://httpbin.org/get", api_key, secret_key, signature);

    std::cout << "HTTP: " << r.http_code << " (expected 0 for unsuccessful curl execution)\n";
    assert(r.http_code == 0);

    std::cout << "OK\n";
}


int main() {
    try {
        /*test_bearer_success();
        test_bearer_fail();*/
        test_get_with_signature_success();
        test_get_with_signature_fail();
        // test_no_auth();
         test_auth_success();
         test_auth_fail();
        // test_redirect();
        // test_404();
        // test_move_constructor();
        // test_move_assignment();
        // test_thread_safety();
        // test_container();
        // test_large_json();
        // test_chunked_stream_json();
        // test_json_schema_like();
        // test_nested_json_array();
        // test_bad_tls();
        // test_use_after_move();
        // test_thread_stress();

        std::cout << "\nALL TESTS PASSED!\n";
    }
    catch (const std::exception& e) {
        std::cerr << "TEST FAILED: " << e.what() << "\n";
        return 1;
    }
    return 0;
}