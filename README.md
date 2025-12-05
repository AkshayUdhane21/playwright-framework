# Glossary
## Libraries Used
### 1. snap7

*Purpose*:
A client/server library designed to communicate with Siemens S7 PLCs (Programmable Logic Controllers).
Key Features:

Supports reading and writing data blocks, inputs, outputs, and memory areas.
Enables integration of industrial automation systems with custom applications.

*Use Case in Project*:
Facilitates direct communication between the application and Siemens PLC for real-time data exchange.

### 2. OpenSSL

Purpose:
A robust toolkit for implementing cryptographic functions and SSL/TLS protocols.
Key Features:

Provides encryption, decryption, and hashing algorithms.
Used for generating HMAC keys to ensure secure API communication.

*Dependencies: zlib (for compression support).*

*Use Case in Project*:
Enforces security standards and ensures integrity and authenticity of data exchanged with APIs.


### 3. spdlog

Purpose:
A fast, header-only C++ logging library.
Key Features:

Supports multi-threaded logging.
Offers customizable log patterns and log levels (info, warning, error, etc.).

*Dependencies: libfmt (for efficient string formatting).*

*Use Case in Project*:
Captures and records application events, errors, and debug information for troubleshooting and monitoring.

### 4. curl

*Purpose*:
A command-line tool and library for transferring data using various protocols (HTTP, HTTPS, FTP, etc.).
Key Features:

Handles GET, POST, and other HTTP requests.
Supports SSL/TLS for secure communication.

*Use Case in Project*:
Performs GET requests to interact with external APIs and retrieve data.


### 5. Service:

Creation:
sc.exe create YokogawaConnectionService binPath= "C:\Users\gauravy\Documents\CppProject\vcpkg_cmake_learn\build\Release\Release\bin\YokogawaConnectionService.exe" start= auto
Then run AddEnvVarService.ps1 as admin

Deletion:
 Get-WmiObject Win32_Service -Filter "Name='YokogawaConnectionService'" | Remove-WmiObject