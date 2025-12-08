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
# For multi-config generators (like Visual Studio), use generator expression to specify the full path
add_test(NAME SecurityTest COMMAND $<TARGET_FILE:security_test>)
# Set working directory to where the executable is located
set_tests_properties(SecurityTest PROPERTIES
    WORKING_DIRECTORY $<TARGET_FILE_DIR:security_test>
)

