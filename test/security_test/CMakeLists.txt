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
# For Visual Studio generators, use generator expression with CMAKE_CFG_INTDIR
# This ensures the correct path is used for each configuration
add_test(NAME SecurityTest COMMAND $<TARGET_FILE:security_test>)
set_tests_properties(SecurityTest PROPERTIES
    WORKING_DIRECTORY "${CMAKE_BINARY_DIR}/$<CONFIG>/bin"
)

