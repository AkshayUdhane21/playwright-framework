#!/bin/bash
set -e

# Allowed configurations (case-insensitive input mapped to CMake names)
map_type() {
    case "$1" in
        debug|Debug|DEBUG) echo "Debug" ;;
        release|Release|RELEASE) echo "Release" ;;
        test|Test|TEST) echo "Test" ;;
        *)
            echo "Unknown build type: $1"
            echo "Usage: ./build.sh [debug] [release] [test]"
            exit 1
            ;;
    esac
}

if [ $# -eq 0 ]; then
    echo "No build types provided."
    echo "Usage: ./build.sh [debug] [release] [test]"
    exit 1
fi

for arg in "$@"; do
    TYPE=$(map_type "$arg")

    echo "=== Building $TYPE configuration ==="
    cmake -B build -DCMAKE_BUILD_TYPE=$TYPE
    cmake --build build --config $TYPE
done

echo "Done!"
