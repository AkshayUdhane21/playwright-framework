# GitHub Actions and SonarQube Setup Guide

This guide explains how to set up GitHub Actions workflows with SonarQube integration for this project.

## Prerequisites

1. A GitHub repository (this project)
2. A SonarQube or SonarCloud account
3. GitHub repository admin access to configure secrets

## GitHub Actions Workflows

This repository includes two workflow files:

### 1. `ci.yml` - Main CI/CD Pipeline
- Builds the project on both Linux and Windows
- Runs tests
- Performs SonarQube analysis (on Linux)

### 2. `sonarqube.yml` - Standalone SonarQube Analysis
- Dedicated workflow for SonarQube code analysis
- Can be run independently or as part of the main CI pipeline

## Setting Up SonarQube/SonarCloud

### Option 1: Using SonarCloud (Recommended for Public Repos)

1. **Create a SonarCloud Account**
   - Go to [sonarcloud.io](https://sonarcloud.io)
   - Sign in with your GitHub account

2. **Create a New Project**
   - Click "Analyze a new project"
   - Select your GitHub organization/user
   - Choose this repository
   - SonarCloud will generate a project key (e.g., `your-org_ats-yokogawa-conn-service`)

3. **Generate a Token**
   - Go to: Account → Security → Generate Token
   - Copy the generated token

4. **Configure GitHub Secrets**
   - Go to your GitHub repository
   - Navigate to: Settings → Secrets and variables → Actions
   - Add the following secrets:
     - `SONAR_TOKEN`: The token generated in step 3
     - `SONAR_HOST_URL`: `https://sonarcloud.io` (for SonarCloud)

5. **Update `sonar-project.properties`** (if needed)
   - If SonarCloud generated a different project key, update `sonar.projectKey` in `sonar-project.properties`

### Option 2: Using Self-Hosted SonarQube

1. **Set up SonarQube Server**
   - Install and configure your SonarQube server
   - Note the server URL (e.g., `https://sonarqube.yourcompany.com`)

2. **Create a Project**
   - Log in to your SonarQube server
   - Create a new project with key: `ats-yokogawa-conn-service`

3. **Generate a Token**
   - Go to: My Account → Security → Generate Token
   - Copy the generated token

4. **Configure GitHub Secrets**
   - Go to your GitHub repository
   - Navigate to: Settings → Secrets and variables → Actions
   - Add the following secrets:
     - `SONAR_TOKEN`: The token generated in step 3
     - `SONAR_HOST_URL`: Your SonarQube server URL

## Workflow Triggers

The workflows are configured to run on:
- **Push** to `main`, `master`, or `develop` branches
- **Pull requests** to `main`, `master`, or `develop` branches
- **Manual trigger** via GitHub Actions UI (workflow_dispatch)

## Workflow Steps

### Build and Test Job
1. Checks out the code
2. Sets up vcpkg package manager
3. Installs dependencies (CURL, nlohmann-json, spdlog, OpenSSL, open62541pp, gtest)
4. Configures CMake
5. Builds the project
6. Runs tests using CTest

### SonarQube Analysis Job
1. Checks out the code (with full history for better analysis)
2. Sets up vcpkg and installs dependencies
3. Downloads SonarQube Build Wrapper for C/C++
4. Configures CMake
5. Builds the project using the build wrapper (captures compilation database)
6. Runs tests
7. Performs SonarQube scan and uploads results

## Troubleshooting

### Build Failures
- Check that all vcpkg dependencies are correctly specified in `vcpkg.json`
- Verify CMake configuration is correct
- Check build logs for specific error messages

### SonarQube Analysis Failures
- Verify `SONAR_TOKEN` and `SONAR_HOST_URL` secrets are correctly set
- Ensure the project key in `sonar-project.properties` matches your SonarQube/SonarCloud project
- Check that the build wrapper output directory (`bw-output`) is created during build
- Verify network connectivity to SonarQube server

### Test Failures
- Review test output in the workflow logs
- Ensure test executables are built correctly
- Check that test data files are available if needed

## Customization

### Changing Build Types
To build different configurations, modify the `BUILD_TYPE` environment variable in the workflow files:
```yaml
env:
  BUILD_TYPE: Debug  # or Release, Test
```

### Adding More Platforms
To add macOS builds, update the matrix in `ci.yml`:
```yaml
matrix:
  os: [ubuntu-latest, windows-latest, macos-latest]
```

### Excluding Files from Analysis
Update `sonar-project.properties` to exclude additional paths:
```properties
sonar.exclusions=**/build/**,**/custom-path/**
```

## Additional Resources

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [SonarQube Documentation](https://docs.sonarqube.org/)
- [SonarCloud Documentation](https://docs.sonarcloud.io/)
- [vcpkg Documentation](https://vcpkg.io/)

