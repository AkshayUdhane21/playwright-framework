cmake_minimum_required(VERSION 3.15)
project(ats_yokogawa_conn_service)

# Enable compile commands export for SonarQube/SonarCloud analysis
set(CMAKE_EXPORT_COMPILE_COMMANDS ON)

set(VCPKG_MANIFEST_FEATURES "" CACHE STRING "")
# Organize build outputs by config type (Debug/Release/Test)
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/${CMAKE_BUILD_TYPE}/bin)
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/${CMAKE_BUILD_TYPE}/lib)
set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/${CMAKE_BUILD_TYPE}/lib)

# Ensure multi-config generators (like Ninja Multi or Visual Studio) also work:
foreach(OUTPUTCONFIG Debug Release RelWithDebInfo MinSizeRel Test)
    string(TOUPPER ${OUTPUTCONFIG} OUTPUTCONFIG_UP)
    set(CMAKE_RUNTIME_OUTPUT_DIRECTORY_${OUTPUTCONFIG_UP} ${CMAKE_BINARY_DIR}/${OUTPUTCONFIG}/bin)
    set(CMAKE_LIBRARY_OUTPUT_DIRECTORY_${OUTPUTCONFIG_UP} ${CMAKE_BINARY_DIR}/${OUTPUTCONFIG}/lib)
    set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY_${OUTPUTCONFIG_UP} ${CMAKE_BINARY_DIR}/${OUTPUTCONFIG}/lib)
endforeach()

# On Windows, prevent <windows.h> from defining min/max macros that break std::min/std::max
if (MSVC)
    add_compile_definitions(NOMINMAX)
endif()

include(CTest)
enable_testing()

find_package(CURL CONFIG REQUIRED)
find_package(nlohmann_json CONFIG REQUIRED)
find_package(spdlog CONFIG REQUIRED)
find_package(OpenSSL REQUIRED)
find_package(open62541pp CONFIG REQUIRED)
find_package(GTest CONFIG REQUIRED)

add_subdirectory(src)
add_subdirectory(test)