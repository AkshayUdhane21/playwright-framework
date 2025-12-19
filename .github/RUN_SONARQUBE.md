# How to Run SonarQube Analysis with GitHub Actions

## Quick Start

### Option 1: Run Standalone SonarQube Workflow

1. **Go to GitHub Actions Tab**
   - Navigate to: `https://github.com/YOUR_USERNAME/YOUR_REPO/actions`
   - Click on **"SonarQube Analysis"** workflow

2. **Click "Run workflow"**
   - Select your branch (main/master/develop/akshay)
   - Click **"Run workflow"** button

### Option 2: Run as Part of CI Pipeline

1. **Go to GitHub Actions Tab**
   - Navigate to: `https://github.com/YOUR_USERNAME/YOUR_REPO/actions`
   - Click on **"CI Build, Test, and SonarQube Analysis"** workflow

2. **Click "Run workflow"**
   - The SonarQube analysis will run automatically after build-and-test completes

## Prerequisites (First Time Setup)

### For SonarCloud (Recommended for Public Repos)

1. **Create SonarCloud Account**
   - Go to https://sonarcloud.io
   - Sign in with GitHub

2. **Create Project**
   - Click "Analyze a new project"
   - Select your organization/user
   - Choose this repository
   - Project key will be generated (or use: `ats-yokogawa-conn-service`)

3. **Generate Token**
   - Go to: Account → Security → Generate Token
   - Copy the token

4. **Add GitHub Secrets**
   - Go to: Repository → Settings → Secrets and variables → Actions
   - Add secret: `SONAR_TOKEN` = your generated token
   - Add secret: `SONAR_HOST_URL` = `https://sonarcloud.io`

### For Self-Hosted SonarQube

1. **Get Server URL and Token**
   - Log in to your SonarQube server
   - Create a project with key: `ats-yokogawa-conn-service`
   - Generate a token: My Account → Security → Generate Token

2. **Add GitHub Secrets**
   - Go to: Repository → Settings → Secrets and variables → Actions
   - Add secret: `SONAR_TOKEN` = your token
   - Add secret: `SONAR_HOST_URL` = your SonarQube server URL (e.g., `https://sonarqube.yourcompany.com`)

## What the Workflow Does

1. ✅ Checks out code with full history
2. ✅ Sets up vcpkg and installs dependencies
3. ✅ Downloads SonarQube Build Wrapper for C/C++
4. ✅ Configures CMake
5. ✅ Builds project with build wrapper (captures compilation info)
6. ✅ Runs tests
7. ✅ Performs SonarQube scan and uploads results

## Troubleshooting

### "Could not find a package configuration file provided by 'gtest'"
✅ **FIXED**: Updated CMakeLists.txt to use `GTest` instead of `gtest`

### "SONAR_TOKEN not found"
- Make sure you've added the `SONAR_TOKEN` secret in GitHub repository settings

### "SONAR_HOST_URL not found"
- Make sure you've added the `SONAR_HOST_URL` secret in GitHub repository settings

### Build Wrapper Errors
- The workflow automatically downloads the build wrapper
- If it fails, check the workflow logs for download errors

## Viewing Results

After the workflow completes:
- **SonarCloud**: Go to https://sonarcloud.io → Your Project
- **Self-Hosted**: Go to your SonarQube server → Your Project

## Manual Trigger

Both workflows support `workflow_dispatch`, so you can trigger them manually:
- Go to Actions tab
- Select the workflow
- Click "Run workflow"
- Select branch and click "Run workflow"







