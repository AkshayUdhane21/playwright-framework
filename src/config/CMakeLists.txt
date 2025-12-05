add_library(config_lib)

set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_STANDARD_REQUIRED true)
set(CMAKE_CXX_EXTENSIONS OFF)

target_sources(config_lib PRIVATE
    config.cpp
)

target_include_directories(config_lib
    PUBLIC
        ${PROJECT_SOURCE_DIR}/include
)

target_link_libraries(config_lib
    PUBLIC
        nlohmann_json::nlohmann_json
        spdlog::spdlog
)

target_compile_features(config_lib PUBLIC cxx_std_20)