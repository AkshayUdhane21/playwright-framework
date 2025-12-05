add_library(curl_handler_lib)

set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_STANDARD_REQUIRED true)
set(CMAKE_CXX_EXTENSIONS OFF)

target_sources(curl_handler_lib PRIVATE
    curl_handler.cpp
)

target_include_directories(curl_handler_lib
    PUBLIC
        ${PROJECT_SOURCE_DIR}/include
)

target_link_libraries(curl_handler_lib
    PUBLIC
        CURL::libcurl
        nlohmann_json::nlohmann_json
        spdlog::spdlog
)

target_compile_features(curl_handler_lib PUBLIC cxx_std_20)