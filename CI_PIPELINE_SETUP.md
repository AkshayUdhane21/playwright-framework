# CI Pipeline Setup Guide for Yokogawa Heartbeat Project

This guide provides detailed instructions for setting up Continuous Integration (CI) pipelines for your project.

## Table of Contents
1. [Prerequisites](#prerequisites)
2. [Option 1: GitHub Actions CI (Recommended)](#option-1-github-actions-ci-recommended)
3. [Option 2: Jenkins CI Pipeline](#option-2-jenkins-ci-pipeline)
4. [Troubleshooting](#troubleshooting)

---

## Prerequisites

### Required Software
- **CMake** (version 3.15 or higher)
- **vcpkg** (C++ package manager)
- **Git** (for version control)
- **C++ Compiler** (MSVC on Windows, GCC/Clang on Linux)

### vcpkg Setup
1. Install vcpkg:
   ```powershell
   git clone https://github.com/Microsoft/vcpkg.git C:\vcpkg
   cd C:\vcpkg
   .\bootstrap-vcpkg.bat
   ```

2. Set environment variable:
   ```powershell
   [System.Environment]::SetEnvironmentVariable("VCPKG_ROOT", "C:\vcpkg", "Machine")
   ```

3. Verify installation:
   ```powershell
   C:\vcpkg\vcpkg.exe version
   ```

---

## Option 1: GitHub Actions CI (Recommended)

GitHub Actions is the easiest option since your project is already on GitHub. It runs automatically on every push and pull request.

### Step 1: Create GitHub Actions Workflow

Create the following file in your repository:
- Path: `.github/workflows/ci.yml`

The workflow file is already created (see `.github/workflows/ci.yml` in your repo).

### Step 2: Configure the Workflow

The workflow will:
- ✅ Trigger on push and pull requests
- ✅ Build on Windows (and optionally Linux)
- ✅ Run all tests
- ✅ Report test results

### Step 3: Enable GitHub Actions

1. Go to your repository on GitHub
2. Click on **Actions** tab
3. If prompted, click **"I understand my workflows, go ahead and enable them"**
4. The pipeline will run automatically on the next push

### Step 4: View Results

- Go to **Actions** tab to see pipeline runs
- Click on a run to see detailed logs
- Green checkmark = success, Red X = failure

---

## Option 2: Jenkins CI Pipeline

### Part A: Jenkins Server Setup

#### 1. Install Jenkins
- Download Jenkins from https://www.jenkins.io/download/
- Install on your Windows server
- Complete initial setup wizard
- Install recommended plugins

#### 2. Install Required Jenkins Plugins
Go to **Manage Jenkins** → **Manage Plugins** → **Available** and install:
- ✅ **Pipeline** (usually pre-installed)
- ✅ **Git** plugin
- ✅ **GitHub** plugin (for webhooks)
- ✅ **GitHub Branch Source** plugin
- ✅ **AnsiColor** plugin (for colored output)
- ✅ **Timestamper** plugin

#### 3. Configure System Tools
Go to **Manage Jenkins** → **Global Tool Configuration**:

- **Git**: Set path to Git executable (usually `C:\Program Files\Git\bin\git.exe`)
- **CMake**: If installed globally, add to PATH or specify path

#### 4. Set Environment Variables
Go to **Manage Jenkins** → **Configure System** → **Global properties**:
- Add environment variable: `VCPKG_ROOT` = `C:\vcpkg` (or your vcpkg path)

---

### Part B: Create Jenkins Pipeline Job

#### Step 1: Create New Pipeline Job
1. Click **New Item** on Jenkins dashboard
2. Enter job name (e.g., "Yokogawa-Heartbeat-CI")
3. Select **Pipeline**
4. Click **OK**

#### Step 2: Configure General Settings

In the **General** section:
- ✅ **GitHub project**: Check this box
  - **Project url**: `https://github.com/AkshayUdhane21/playwright-framework` (or your actual repo URL)
- ✅ **Discard old builds**: Optional, but recommended
  - **Strategy**: Log Rotation
  - **Days to keep builds**: 30
  - **Max # of builds to keep**: 50

#### Step 3: Configure Build Triggers

Choose ONE of the following options:

**Option A: GitHub Webhook (Recommended)**
- ✅ **GitHub hook trigger for GITScm polling**: Check this
- This requires configuring webhook in GitHub (see below)

**Option B: Poll SCM**
- ✅ **Poll SCM**: Check this
- **Schedule**: `H/15 * * * *` (every 15 minutes)
  - Or `H * * * *` (every hour)
  - Or `H/5 * * * *` (every 5 minutes)

**Option C: Build Periodically**
- ✅ **Build periodically**: Check this
- **Schedule**: `0 2 * * *` (daily at 2 AM)

#### Step 4: Configure Pipeline Definition

**IMPORTANT**: Select **"Pipeline script from SCM"** (NOT "Pipeline script")

1. **Definition**: Select **"Pipeline script from SCM"**

2. **SCM**: Select **"Git"**

3. **Repositories**:
   - **Repository URL**: `https://github.com/AkshayUdhane21/playwright-framework.git`
     - Or use SSH: `git@github.com:AkshayUdhane21/playwright-framework.git`
   - **Credentials**: 
     - If public repo: Leave empty or select "none"
     - If private repo: Click **Add** → **Jenkins** → Enter GitHub username/password or token
   - **Branches to build**: 
     - `*/main` (for main branch)
     - `*/Akshay` (for Akshay branch)
     - Or `*/master` if that's your default branch

4. **Script Path**: `Jenkinsfile.groovy`
   - This tells Jenkins to use the pipeline script from your repository

5. **Lightweight checkout**: Uncheck this (we need full checkout for vcpkg)

#### Step 5: Advanced Pipeline Options

- **Use Groovy Sandbox**: 
  - ✅ Checked = More secure, but may limit some operations
  - ⬜ Unchecked = Full Groovy access (recommended if you trust the script)

#### Step 6: Save Configuration

Click **Save** at the bottom of the page.

---

### Part C: Configure GitHub Webhook (For Option A)

If you chose GitHub Webhook trigger:

1. Go to your GitHub repository
2. Click **Settings** → **Webhooks** → **Add webhook**
3. **Payload URL**: `http://your-jenkins-server:8080/github-webhook/`
   - Replace with your Jenkins server URL
4. **Content type**: `application/json`
5. **Which events**: Select **"Just the push event"** or **"Let me select individual events"**
   - Check: ✅ Pushes, ✅ Pull requests
6. **Active**: ✅ Checked
7. Click **Add webhook**

**Note**: If Jenkins is behind a firewall, you may need to:
- Use a service like ngrok to expose Jenkins
- Or use Poll SCM instead of webhooks

---

### Part D: Run Your First Build

1. Go to your Jenkins job dashboard
2. Click **Build Now** (on the left sidebar)
3. You should see a build appear in **Build History**
4. Click on the build number to see progress
5. Click **Console Output** to see detailed logs

---

## Pipeline Stages Explained

Your `Jenkinsfile.groovy` defines these stages:

### 1. **Checkout**
- Clones your repository
- Checks out the specified branch

### 2. **Environment Setup**
- Verifies vcpkg installation
- Sets up build environment variables
- Validates required tools are available

### 3. **Configure CMake**
- Runs CMake configuration
- Links vcpkg toolchain
- Generates build files

### 4. **Build**
- Compiles the project
- Builds test executables (`config_test`, `security_test`)
- Uses parallel compilation for speed

### 5. **Test - CTest**
- Runs all registered CTest tests
- Executes `ConfigTest` and `SecurityTest`
- Reports pass/fail status
- Fails pipeline if any test fails

### 6. **Post Actions**
- Always: Archives test executables
- Success: Prints success message
- Failure: Prints failure message

---

## Environment Variables

You can customize these in Jenkins:

1. Go to your job → **Configure** → **Pipeline** section
2. Scroll to **Pipeline** → **Advanced** → **Properties**
3. Add environment variables:
   - `VCPKG_ROOT`: Path to vcpkg (default: `C:\vcpkg`)
   - `BUILD_TYPE`: `Debug` or `Release` (default: `Debug`)

Or set them globally in **Manage Jenkins** → **Configure System** → **Global properties**.

---

## Troubleshooting

### Issue: "vcpkg toolchain file not found"
**Solution**: 
- Verify `VCPKG_ROOT` environment variable is set correctly
- Check that vcpkg is installed at the specified path
- Ensure `vcpkg.cmake` exists at `${VCPKG_ROOT}\scripts\buildsystems\vcpkg.cmake`

### Issue: "CMake not found"
**Solution**:
- Install CMake and add to system PATH
- Or specify full path in Jenkinsfile: `bat "C:\\Program Files\\CMake\\bin\\cmake.exe ..."`

### Issue: "Tests not found"
**Solution**:
- Ensure tests are registered with `add_test()` in CMakeLists.txt
- Verify build completed successfully before test stage
- Check that test executables exist in `${BUILD_DIR}/${BUILD_TYPE}/bin/`

### Issue: "GitHub webhook not triggering builds"
**Solution**:
- Verify webhook URL is correct and accessible
- Check Jenkins logs: **Manage Jenkins** → **System Log**
- Test webhook delivery in GitHub: **Settings** → **Webhooks** → Click on webhook → **Recent Deliveries**
- Consider using Poll SCM instead

### Issue: "Pipeline script from SCM not working"
**Solution**:
- Verify repository URL is correct
- Check branch name matches (case-sensitive)
- Ensure `Jenkinsfile.groovy` exists in repository root
- Check Jenkins console for Git errors

### Issue: "Build timeout"
**Solution**:
- Increase timeout in Jenkinsfile: `timeout(time: 60, unit: 'MINUTES')`
- Or disable timeout for debugging: Remove timeout option

### Issue: "Dependencies not found"
**Solution**:
- Run `vcpkg install` manually to ensure all dependencies are installed
- Check `vcpkg.json` has all required packages
- Verify vcpkg manifest mode is enabled

---

## Best Practices

1. **Use Feature Branches**: Test changes in feature branches before merging to main
2. **Monitor Builds**: Set up email notifications for build failures
3. **Keep Jenkinsfile in Repo**: Version control your pipeline configuration
4. **Use Build Parameters**: Add parameters for BUILD_TYPE, VCPKG_ROOT, etc.
5. **Archive Artifacts**: Keep build artifacts for debugging
6. **Clean Workspace**: Consider adding a cleanup stage to remove old build files

---

## Next Steps

1. ✅ Set up CI pipeline (choose GitHub Actions or Jenkins)
2. ✅ Test the pipeline with a small change
3. ✅ Set up notifications (email, Slack, etc.)
4. ✅ Add code coverage reporting (optional)
5. ✅ Set up CD (Continuous Deployment) if needed

---

## Support

For issues or questions:
- Check Jenkins console output for detailed error messages
- Review CMake and vcpkg documentation
- Check GitHub Actions logs if using GitHub Actions


