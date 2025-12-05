set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_STANDARD_REQUIRED true)
set(CMAKE_CXX_EXTENSIONS OFF)

# Create the test executable
add_executable(security_test
    security_test.cpp
)

# Include directories
target_include_directories(security_test
    PRIVATE
        ${PROJECT_SOURCE_DIR}/include
)

# Link with gtest (using modern vcpkg style) and required libraries
target_link_libraries(security_test
    PRIVATE
        GTest::gtest_main
        GTest::gtest
        security_lib
        OpenSSL::SSL
)

# Register test with CTest
# For multi-config generators, CTest will find the executable in the correct config directory
add_test(NAME SecurityTest COMMAND security_test)

