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

## Setting Up SonarCloud

### Step-by-Step SonarCloud Setup

1. **Create a SonarCloud Account**
   - Go to [sonarcloud.io](https://sonarcloud.io)
   - Sign in with your GitHub account (use the same account as your repository)

2. **Create a New Project**
   - Click "Analyze a new project" or go to "My Account" → "Create Project"
   - Select your GitHub organization/user: **AkshayUdhane21**
   - Choose this repository
   - SonarCloud will generate a project key
   - **Important**: The project key should match what's in `sonar-project.properties`:
     - Current project key: `AkshayUdhane21_ats-yokogawa-conn-service`
   - If SonarCloud generates a different key, either:
     - Update `sonar-project.properties` to match SonarCloud's key, OR
     - Use SonarCloud's key when creating the project

3. **Generate a SonarCloud Token**
   - Go to: [SonarCloud Account → Security](https://sonarcloud.io/account/security)
   - Click "Generate Token"
   - Give it a name (e.g., "GitHub Actions")
   - **Copy the token immediately** (you won't be able to see it again!)

4. **Configure GitHub Secrets** ⚠️ **REQUIRED**
   - Go to your GitHub repository
   - Navigate to: **Settings → Secrets and variables → Actions**
   - Click "New repository secret"
   - Add these two secrets:
     
     **Secret 1: `SONAR_TOKEN`**
     - Name: `SONAR_TOKEN`
     - Value: Paste the token you copied from SonarCloud (step 3)
     
     **Secret 2: `SONAR_HOST_URL`**
     - Name: `SONAR_HOST_URL`
     - Value: `https://sonarcloud.io` (exactly this, no trailing slash)

5. **Verify `sonar-project.properties`**
   - Check that `sonar.projectKey` matches your SonarCloud project key
   - Check that `sonar.organization` matches your SonarCloud organization (should be `AkshayUdhane21`)

### Quick Setup Checklist

- [ ] SonarCloud account created and logged in
- [ ] Project created in SonarCloud with key: `AkshayUdhane21_ats-yokogawa-conn-service`
- [ ] SonarCloud token generated and copied
- [ ] GitHub Secret `SONAR_TOKEN` added with the token
- [ ] GitHub Secret `SONAR_HOST_URL` added with value `https://sonarcloud.io`
- [ ] `sonar-project.properties` verified (project key and organization match)

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

## Quick Start

### To Run the Workflows:

1. **Create a Pull Request** (Recommended for testing)
   - Create a PR from any branch to `main`, `master`, `develop`, or `akshay`
   - The workflow will automatically trigger
   - Go to the "Actions" tab to see the workflow running
   - Go to the "Checks" tab on your PR to see SonarCloud results

2. **Push to repository** - The workflows will automatically run on pushes to `main`, `master`, `develop`, or `akshay` branches

3. **Manual trigger** - Go to Actions tab → Select workflow → Click "Run workflow"

### Workflow Files:

- **`ci.yml`** - Main CI/CD pipeline with build, test, and SonarCloud analysis
- **`sonarqube.yml`** - Standalone SonarCloud analysis workflow

### Testing Your Setup:

1. **Create a test PR**:
   ```bash
   git checkout -b test-sonarcloud
   git commit --allow-empty -m "Test SonarCloud integration"
   git push origin test-sonarcloud
   ```
   Then create a PR from `test-sonarcloud` to `main` (or any configured branch)

2. **Check the workflow**:
   - Go to your repository on GitHub
   - Click the "Actions" tab
   - You should see the workflow running
   - Click on it to see detailed logs

3. **Verify SonarCloud**:
   - If successful, go to [sonarcloud.io](https://sonarcloud.io)
   - Navigate to your project
   - You should see the analysis results

## Troubleshooting

### Build Failures
- Check that all vcpkg dependencies are correctly specified in `vcpkg.json`
- Verify CMake configuration is correct
- Check build logs for specific error messages
- Ensure vcpkg manifest mode is working (dependencies install automatically)

### SonarCloud Analysis Failures

**Most Common Issues:**

1. **"Failed to query server version" Error**
   - **Cause**: Missing or incorrect `SONAR_TOKEN` or `SONAR_HOST_URL` secrets
   - **Fix**: 
     - Go to: Repository → Settings → Secrets and variables → Actions
     - Verify `SONAR_TOKEN` exists and contains your SonarCloud token
     - Verify `SONAR_HOST_URL` exists and is exactly `https://sonarcloud.io` (no trailing slash)
     - Make sure you copied the token correctly (no extra spaces)

2. **"Project key does not exist" Error**
   - **Cause**: Project key mismatch between `sonar-project.properties` and SonarCloud
   - **Fix**: 
     - Check your SonarCloud project key
     - Update `sonar-project.properties` to match, OR
     - Create a new SonarCloud project with the key from `sonar-project.properties`

3. **"Organization does not exist" Error**
   - **Cause**: Organization name mismatch
   - **Fix**: 
     - Verify `sonar.organization` in `sonar-project.properties` matches your SonarCloud organization
     - Should be: `AkshayUdhane21`

4. **Build Wrapper Issues**
   - Check that the build wrapper output directory (`bw-output`) is created during build
   - Verify the build step completed successfully before SonarCloud scan

5. **PR Not Running**
   - Workflows run automatically on PRs to `main`, `master`, `develop`, or `akshay` branches
   - Check the Actions tab to see if the workflow was triggered
   - Make sure you're creating a PR to one of the configured branches

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






