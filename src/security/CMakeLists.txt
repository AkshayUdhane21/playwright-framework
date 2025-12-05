add_library(security_lib)

set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_STANDARD_REQUIRED true)
set(CMAKE_CXX_EXTENSIONS OFF)

target_sources(security_lib PRIVATE
    security.cpp
)

# Tell this library where to find its headers
target_include_directories(security_lib
    PUBLIC
        ${PROJECT_SOURCE_DIR}/external/include
        ${PROJECT_SOURCE_DIR}/include
)

target_link_libraries(security_lib
    PUBLIC
        OpenSSL::SSL
)

target_compile_features(security_lib PUBLIC cxx_std_20)