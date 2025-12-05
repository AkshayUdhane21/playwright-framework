add_executable(curl_handler_test
    curl_handler_test.cpp
)

target_link_libraries(curl_handler_test
    PUBLIC 
    curl_handler_lib
    OpenSSL::SSL
)

target_include_directories(curl_handler_test
    PUBLIC
        ${PROJECT_SOURCE_DIR}/include   # if your tests need your public headers
)