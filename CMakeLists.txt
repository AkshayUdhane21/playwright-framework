cmake_minimum_required(VERSION 3.15)
project(ats_yokogawa_conn_service)

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

find_package(CURL CONFIG REQUIRED)
find_package(nlohmann_json CONFIG REQUIRED)
find_package(spdlog CONFIG REQUIRED)
find_package(openssl CONFIG REQUIRED)
find_package(open62541pp CONFIG REQUIRED)
find_package(gtest CONFIG REQUIRED)

add_subdirectory(src)
add_subdirectory(test)