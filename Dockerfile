FROM ubuntu:22.04 AS builder

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        build-essential \
        cmake \
        ninja-build \
        git \
        curl \
        ca-certificates && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /vcpkg
RUN git clone https://github.com/microsoft/vcpkg . && \
    ./bootstrap-vcpkg.sh -disableMetrics

WORKDIR /app
COPY . .

RUN cmake -B build -S . -G Ninja \
      -DCMAKE_BUILD_TYPE=Release \
      -DCMAKE_TOOLCHAIN_FILE=/vcpkg/scripts/buildsystems/vcpkg.cmake && \
    cmake --build build --config Release --target yokogawa_console

# Runtime image
FROM ubuntu:22.04 AS runtime

RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        ca-certificates && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy built binary from builder stage
COPY --from=builder /app/build/Release/bin/yokogawa_console /app/yokogawa_console

# Default configuration location inside the container
ENV YOKOGAWA_CONNECTION_CONFIG_PATH=/config/config.json

# Directory where you will mount your config.json from the host
RUN mkdir -p /config

ENTRYPOINT ["/app/yokogawa_console"]
















