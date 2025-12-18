add_subdirectory(curl_handler)
add_subdirectory(config)
add_subdirectory(security)

# Windows Service executable (uses WinAPI via windows.h) – build only on Windows
if (WIN32)
    add_executable(AtsYokogawaConnectionService
        application/application.cpp
        main_service.cpp
    )

    target_link_libraries(AtsYokogawaConnectionService
        PUBLIC
            curl_handler_lib
            config_lib
            security_lib
            open62541pp::open62541pp
    )

    target_compile_features(AtsYokogawaConnectionService PRIVATE cxx_std_20)
endif()

# Console executable for running the service without Windows Service Manager.
# This is the target used inside the Docker image (Linux-friendly, no WinAPI).
add_executable(yokogawa_console
    main.cpp
    application/application.cpp
)

target_link_libraries(yokogawa_console
    PUBLIC
        curl_handler_lib
        config_lib
        security_lib
        open62541pp::open62541pp
)

target_compile_features(yokogawa_console PRIVATE cxx_std_20)
###
#if(MSVC)
    # Force /clr
    #target_compile_options(YokogawaConnectionService PRIVATE /clr)

    ## REMOVE /RTC1 in Debug (it breaks /clr)
   # string(REPLACE "/RTC1" "" CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG}")
   # # CRITICAL: Replace /EHs with /EHa (required for /clr)
  #string(REPLACE "/EHs" "/EHa" CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS}")
  #string(REPLACE "/EHs" "/EHa" CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG}")
 # string(REPLACE "/EHs" "/EHa" CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE}")
#endif()

# Enable C++/CLI
#set_target_properties(YokogawaConnectionService PROPERTIES COMMON_LANGUAGE_RUNTIME "")
###