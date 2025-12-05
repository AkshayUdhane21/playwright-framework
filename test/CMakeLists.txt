# add_subdirectory(curl_handler_test)

set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_STANDARD_REQUIRED true)
set(CMAKE_CXX_EXTENSIONS OFF)

enable_testing() # to enable testing

# gtest is already found in the main CMakeLists.txt using CONFIG mode (modern vcpkg style)
# No need to find it again, just use GTest::gtest and GTest::gtest_main targets

# Config tests
add_executable(config_test config/config_test.cpp)
target_link_libraries(config_test 
    PRIVATE 
        GTest::gtest_main 
        GTest::gtest 
        config_lib
)
target_include_directories(config_test
    PRIVATE
        ${PROJECT_SOURCE_DIR}/include
)
# Register ConfigTest with CTest
# For multi-config generators, CTest will find the executable in the correct config directory
add_test(NAME ConfigTest COMMAND config_test)

# Security tests
add_subdirectory(security_test)