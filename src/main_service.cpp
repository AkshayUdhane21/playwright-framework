#ifndef NOMINMAX
#define NOMINMAX
#endif
#include <windows.h>
#include <tchar.h>
#include <iostream>
#include "application/application.hpp"
#include <exception>
#include <cstdlib>
#define UNICODE
#define _UNICODE

static const TCHAR SERVICE_NAME[] = TEXT("AtsYokogawaConnectionService");

SERVICE_STATUS g_ServiceStatus = { 0 };
SERVICE_STATUS_HANDLE g_StatusHandle = NULL;
HANDLE g_ServiceStopEvent = INVALID_HANDLE_VALUE;

int main_application() {
    std::cout << "Yokogawa API connection service" << std::endl;
    const std::string cfg_path = std::getenv("YOKOGAWA_CONNECTION_CONFIG_PATH") ? std::getenv("YOKOGAWA_CONNECTION_CONFIG_PATH") : "";
    if (cfg_path.empty()) {
        std::cerr << "Cannot find config file! Please check environment variable YOKOGAWA_CONNECTION_CONFIG_PATH";
        return EXIT_FAILURE;
    }
    Application app(cfg_path);
    try {
        app.run();
    }
    catch (const SetupError& e) {
        spdlog::error(e.what());
        return EXIT_FAILURE;
    }
    catch (const std::exception& e) {
        spdlog::critical("Error in running app: {}", e.what());
        return EXIT_FAILURE;
    }
    spdlog::info("Exiting the service");
    std::cout << "Service stopped, press ENTER or any other key to exit" << std::endl;
    return EXIT_SUCCESS;
}

VOID WINAPI ServiceMain(DWORD, LPTSTR*);
VOID WINAPI ServiceCtrlHandler(DWORD);
DWORD WINAPI ServiceWorkerThread(LPVOID);

int _tmain(int argc, TCHAR* argv[]) {


    SERVICE_TABLE_ENTRY ServiceTable[] = {
    { (LPTSTR)SERVICE_NAME, ServiceMain },  // Safe when using TCHAR + UNICODE
    { NULL, NULL }
    };

    if (StartServiceCtrlDispatcher(ServiceTable) == FALSE) { // Used to connect service to SCM
        return GetLastError();
    }
    return 0;
}

VOID WINAPI ServiceMain(DWORD, LPTSTR*) {
    g_StatusHandle = RegisterServiceCtrlHandler(SERVICE_NAME, ServiceCtrlHandler);
    if (g_StatusHandle == NULL) return;


    g_ServiceStatus.dwServiceType = SERVICE_WIN32_OWN_PROCESS;
    g_ServiceStatus.dwCurrentState = SERVICE_START_PENDING;
    g_ServiceStatus.dwControlsAccepted = 0;
    g_ServiceStatus.dwWin32ExitCode = 0;
    g_ServiceStatus.dwServiceSpecificExitCode = 0;
    g_ServiceStatus.dwCheckPoint = 0;
    g_ServiceStatus.dwWaitHint = 3000; // 3 seconds

    SetServiceStatus(g_StatusHandle, &g_ServiceStatus);

    g_ServiceStopEvent = CreateEvent(NULL, TRUE, FALSE, NULL); // (security_params, manual_reset, if false: thread_waits_for SetEvent(), Name of the event)
    if (g_ServiceStopEvent == NULL) {
        g_ServiceStatus.dwCurrentState = SERVICE_STOPPED;
        SetServiceStatus(g_StatusHandle, &g_ServiceStatus);
        return;
    }


    g_ServiceStatus.dwControlsAccepted = SERVICE_ACCEPT_STOP;
    g_ServiceStatus.dwCurrentState = SERVICE_RUNNING;
    g_ServiceStatus.dwCheckPoint = 0;
    g_ServiceStatus.dwWaitHint = 0;

    SetServiceStatus(g_StatusHandle, &g_ServiceStatus);

    HANDLE Thread = CreateThread(NULL, 0, ServiceWorkerThread, NULL, 0, NULL);
    if (Thread == NULL) {
        DWORD err = GetLastError();
        g_ServiceStatus.dwCurrentState = SERVICE_STOPPED;
        g_ServiceStatus.dwWin32ExitCode = err;
        g_ServiceStatus.dwServiceSpecificExitCode = 0;
        SetServiceStatus(g_StatusHandle, &g_ServiceStatus);
        return;
    }

    WaitForSingleObject(Thread, INFINITE);
    CloseHandle(g_ServiceStopEvent);
    CloseHandle(Thread);

    g_ServiceStatus.dwCurrentState = SERVICE_STOPPED;
    g_ServiceStatus.dwControlsAccepted = 0;
    g_ServiceStatus.dwWin32ExitCode = 0;
    SetServiceStatus(g_StatusHandle, &g_ServiceStatus);
}

VOID WINAPI ServiceCtrlHandler(DWORD CtrlCode) {
    switch (CtrlCode) {
    case SERVICE_CONTROL_STOP:
        g_ServiceStatus.dwCurrentState = SERVICE_STOPPED;
        SetServiceStatus(g_StatusHandle, &g_ServiceStatus);
        SetEvent(g_ServiceStopEvent);
        break;
    default:
        break;
    }
}

DWORD WINAPI ServiceWorkerThread(LPVOID lpParam) {

    try {
        spdlog::info("Worker thread started");
        int result = main_application();
        spdlog::info("Worker thread finished with result {}", result);
        SetEvent(g_ServiceStopEvent);
        return result;
    }
    catch (const std::exception& e) {
        spdlog::critical("Exception in worker thread: {}", e.what());
        SetEvent(g_ServiceStopEvent);
        return 1;
    }

}