# Jenkins CI Pipeline - Quick Start Guide

## 🚀 Quick Setup (5 Minutes)

### Step 1: Create Jenkins Job
1. Open Jenkins → **New Item**
2. Name: `Yokogawa-Heartbeat-CI`
3. Type: **Pipeline** → Click **OK**

### Step 2: Configure Pipeline
1. Scroll to **Pipeline** section
2. **Definition**: Select **"Pipeline script from SCM"** ⚠️ (NOT "Pipeline script")
3. **SCM**: Select **"Git"**
4. **Repository URL**: `https://github.com/AkshayUdhane21/playwright-framework.git`
   - Replace with your actual repository URL
5. **Branches to build**: `*/main` or `*/Akshay` (your branch name)
6. **Script Path**: `Jenkinsfile.groovy`
7. Click **Save**

### Step 3: Configure Triggers (Choose One)

**Option A: GitHub Webhook (Recommended)**
- ✅ Check **"GitHub hook trigger for GITScm polling"**
- Then configure webhook in GitHub (see below)

**Option B: Poll SCM**
- ✅ Check **"Poll SCM"**
- **Schedule**: `H/15 * * * *` (every 15 minutes)

### Step 4: Set Environment Variables

**In Jenkins Job:**
1. Go to job → **Configure**
2. Scroll to **Pipeline** → **Advanced** → **Properties**
3. Add environment variables:
   - `VCPKG_ROOT` = `C:\vcpkg` (or your vcpkg path)
   - `BUILD_TYPE` = `Debug` (or `Release`)

**OR Globally:**
1. **Manage Jenkins** → **Configure System** → **Global properties**
2. Add same environment variables

### Step 5: Configure GitHub Webhook (If using Option A)

1. Go to GitHub repository → **Settings** → **Webhooks** → **Add webhook**
2. **Payload URL**: `http://your-jenkins-server:8080/github-webhook/`
3. **Content type**: `application/json`
4. **Events**: Select **"Just the push event"**
5. Click **Add webhook**

### Step 6: Run First Build

1. Click **Build Now** in Jenkins
2. Watch the build progress
3. Check **Console Output** for logs

---

## ✅ Verification Checklist

Before running the pipeline, ensure:

- [ ] vcpkg is installed at `C:\vcpkg` (or set `VCPKG_ROOT`)
- [ ] CMake is installed and in PATH
- [ ] Git is installed and in PATH
- [ ] Jenkins has access to your repository
- [ ] `Jenkinsfile.groovy` exists in repository root
- [ ] All required Jenkins plugins are installed

---

## 🔧 Common Issues & Fixes

### Issue: "Pipeline script from SCM" not working
**Fix**: 
- Verify repository URL is correct
- Check branch name (case-sensitive)
- Ensure `Jenkinsfile.groovy` exists in repo root

### Issue: "vcpkg toolchain file not found"
**Fix**:
- Set `VCPKG_ROOT` environment variable in Jenkins
- Verify vcpkg is installed at that path
- Check path: `${VCPKG_ROOT}\scripts\buildsystems\vcpkg.cmake`

### Issue: Build fails with "CMake not found"
**Fix**:
- Install CMake and add to system PATH
- Or specify full path in Jenkinsfile

### Issue: Tests not running
**Fix**:
- Verify build completed successfully
- Check test executables exist in `build/Debug/bin/`
- Review CTest output in console logs

---

## 📊 Pipeline Stages

Your pipeline runs these stages:

1. **Checkout** - Clones repository
2. **Environment Setup** - Verifies tools (vcpkg, CMake)
3. **Configure CMake** - Generates build files
4. **Build** - Compiles project and tests
5. **Test** - Runs ConfigTest and SecurityTest
6. **Post Actions** - Archives artifacts

---

## 🎯 Next Steps

1. ✅ Set up email notifications for build failures
2. ✅ Add build parameters (BUILD_TYPE, VCPKG_ROOT)
3. ✅ Set up multiple branches (main, develop, feature/*)
4. ✅ Add code coverage reporting
5. ✅ Configure deployment stage (if needed)

---

## 📚 Full Documentation

For detailed setup instructions, see: [CI_PIPELINE_SETUP.md](CI_PIPELINE_SETUP.md)

---

## 💡 Tips

- **Test locally first**: Run `build.ps1` locally before pushing to CI
- **Monitor first build**: Watch the console output to catch issues early
- **Use build parameters**: Make BUILD_TYPE configurable for flexibility
- **Keep Jenkinsfile in repo**: Version control your pipeline configuration


