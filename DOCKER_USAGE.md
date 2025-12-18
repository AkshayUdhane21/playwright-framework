## Docker usage for Yokogawa Connection Service

This document shows **step-by-step** how to:
- build the Docker image
- run the container
- use your own `config.json`
- push the image to **Docker Hub**

All commands assume you are in the project root directory: `youkugawaheartbeat-master`.

---

### 1. Prerequisites

- **Docker** installed (Docker Desktop on Windows is fine)
- Internet access (the build pulls dependencies via `vcpkg`)
- Docker is using **Linux containers** (default on Docker Desktop)

---

### 2. Build the Docker image

From the project root:

```bash
docker build -t yokogawa-connection-service:latest .
```

Explanation:
- **`-t yokogawa-connection-service:latest`**: names the image `yokogawa-connection-service` with tag `latest`
- The Dockerfile:
  - builds all C++ code using CMake + `vcpkg`
  - creates a small runtime image that runs the console executable `yokogawa_console`

This step can take several minutes the first time because `vcpkg` has to download and build all C++ libraries.

---

### 3. Providing `config.json` to the container

The application reads the config file using the environment variable:

- **`YOKOGAWA_CONNECTION_CONFIG_PATH`**, which is set inside the container to `/config/config.json` by default.

You should have a `config.json` on your host machine (for example in `C:\path\to\config.json` on Windows).

We will **mount** that file into the container and let the service read it.

---

### 4. Run the container (basic)

Example (Linux/macOS path):

```bash
docker run --rm \
  -v /absolute/path/to/your/config.json:/config/config.json:ro \
  yokogawa-connection-service:latest
```

Example (Windows PowerShell path):

```powershell
docker run --rm `
  -v C:\absolute\path\to\your\config.json:/config/config.json:ro `
  yokogawa-connection-service:latest
```

Notes:
- **`-v host_path:/config/config.json:ro`** mounts your local `config.json` into the container as read-only.
- The service inside the container uses the default environment:
  - `YOKOGAWA_CONNECTION_CONFIG_PATH=/config/config.json`

If you want to override the path, you can set the env variable explicitly:

```bash
docker run --rm \
  -e YOKOGAWA_CONNECTION_CONFIG_PATH=/config/custom.json \
  -v /absolute/path/to/your/config.json:/config/custom.json:ro \
  yokogawa-connection-service:latest
```

---

### 5. Run container in background (detached)

To run as a background service:

```bash
docker run -d \
  --name yokogawa-conn-service \
  -v /absolute/path/to/your/config.json:/config/config.json:ro \
  yokogawa-connection-service:latest
```

Check logs:

```bash
docker logs -f yokogawa-conn-service
```

Stop and remove the container:

```bash
docker stop yokogawa-conn-service
docker rm yokogawa-conn-service
```

---

### 6. Tagging the image for Docker Hub

#### 6.1. Choose your Docker Hub name

Assume:
- Your Docker Hub username is **`YOUR_DOCKERHUB_USERNAME`**
- You want the repository to be **`yokogawa-connection-service`**

Create a tag that includes your Docker Hub namespace:

```bash
docker tag yokogawa-connection-service:latest \
  YOUR_DOCKERHUB_USERNAME/yokogawa-connection-service:latest
```

You can create additional version tags if you like:

```bash
docker tag yokogawa-connection-service:latest \
  YOUR_DOCKERHUB_USERNAME/yokogawa-connection-service:v1.0.0
```

---

### 7. Login and push to Docker Hub

#### 7.1. Login

```bash
docker login
```

Enter your Docker Hub **username** and **password / access token**.

#### 7.2. Push the image

Push the `latest` tag:

```bash
docker push YOUR_DOCKERHUB_USERNAME/yokogawa-connection-service:latest
```

Or push a specific version:

```bash
docker push YOUR_DOCKERHUB_USERNAME/yokogawa-connection-service:v1.0.0
```

After this, you will see the repository and tags in your Docker Hub account.

---

### 8. Pull and run from any machine

On any machine with Docker:

```bash
docker pull YOUR_DOCKERHUB_USERNAME/yokogawa-connection-service:latest
```

Run with your `config.json`:

```bash
docker run --rm \
  -v /absolute/path/to/your/config.json:/config/config.json:ro \
  YOUR_DOCKERHUB_USERNAME/yokogawa-connection-service:latest
```

---

### 9. Quick checklist

- **Build image**: `docker build -t yokogawa-connection-service:latest .`
- **Run with config**:
  - Mount config: `-v /path/to/config.json:/config/config.json:ro`
  - (Optional) override env: `-e YOKOGAWA_CONNECTION_CONFIG_PATH=/config/config.json`
- **Tag for Docker Hub**: `docker tag yokogawa-connection-service:latest YOUR_DOCKERHUB_USERNAME/yokogawa-connection-service:latest`
- **Push to Docker Hub**: `docker push YOUR_DOCKERHUB_USERNAME/yokogawa-connection-service:latest`









