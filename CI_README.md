# CI/CD Pipeline Documentation

Welcome! This directory contains everything you need to set up Continuous Integration for your Yokogawa Heartbeat project.

## 📚 Documentation Files

### Quick Start Guides
1. **[JENKINS_QUICK_START.md](JENKINS_QUICK_START.md)** ⚡
   - 5-minute setup guide for Jenkins
   - Essential steps only
   - Perfect for getting started quickly

2. **[JENKINS_UI_CONFIGURATION.md](JENKINS_UI_CONFIGURATION.md)** 🖱️
   - Step-by-step UI configuration
   - Shows exactly what to check/select
   - Visual guide for Jenkins interface

### Complete Documentation
3. **[CI_PIPELINE_SETUP.md](CI_PIPELINE_SETUP.md)** 📖
   - Comprehensive setup guide
   - Both GitHub Actions and Jenkins options
   - Troubleshooting section
   - Best practices

## 🚀 Quick Decision Guide

### Which CI Solution Should I Use?

#### ✅ Use GitHub Actions If:
- Your project is on GitHub
- You want the easiest setup
- You don't have a Jenkins server
- You want free CI for public repos

**Setup Time**: ~2 minutes  
**File**: `.github/workflows/ci.yml` (already created!)

#### ✅ Use Jenkins If:
- You have a Jenkins server already
- You need more control/customization
- You're using Jenkins for other projects
- You need on-premises CI

**Setup Time**: ~10 minutes  
**File**: `Jenkinsfile.groovy` (already created!)

## 📁 Pipeline Files

### GitHub Actions
- **Location**: `.github/workflows/ci.yml`
- **Triggers**: Push and Pull Requests
- **Platform**: Windows (GitHub-hosted runners)
- **Status**: ✅ Ready to use

### Jenkins
- **Location**: `Jenkinsfile.groovy` (in repo root)
- **Triggers**: Configurable (webhook, polling, scheduled)
- **Platform**: Windows (your Jenkins server)
- **Status**: ✅ Ready to use

## 🎯 What the Pipeline Does

Both pipelines perform the same steps:

1. **Checkout** - Clones your repository
2. **Setup** - Installs/verifies vcpkg and dependencies
3. **Configure** - Runs CMake configuration
4. **Build** - Compiles project and test executables
5. **Test** - Runs ConfigTest and SecurityTest
6. **Archive** - Saves test artifacts

## ⚙️ Prerequisites

Before setting up CI, ensure you have:

- [x] **CMake** (3.15+) installed
- [x] **vcpkg** installed (default: `C:\vcpkg`)
- [x] **Git** installed
- [x] **C++ Compiler** (MSVC on Windows)

## 🏃 Getting Started

### Option 1: GitHub Actions (Easiest)

1. **Push the workflow file** (already created at `.github/workflows/ci.yml`)
2. **Go to GitHub** → Your repo → **Actions** tab
3. **Enable workflows** if prompted
4. **Push a change** to trigger the pipeline
5. **View results** in the Actions tab

That's it! 🎉

### Option 2: Jenkins

1. **Read**: [JENKINS_QUICK_START.md](JENKINS_QUICK_START.md)
2. **Follow**: Step-by-step instructions
3. **Configure**: Jenkins job using [JENKINS_UI_CONFIGURATION.md](JENKINS_UI_CONFIGURATION.md)
4. **Test**: Click "Build Now"
5. **Monitor**: Check console output

## 🔧 Configuration

### Environment Variables

Both pipelines use these environment variables:

- `VCPKG_ROOT`: Path to vcpkg (default: `C:\vcpkg`)
- `BUILD_TYPE`: Build configuration (default: `Debug`)
- `BUILD_DIR`: Build directory (default: `build`)

### Customization

#### GitHub Actions
Edit `.github/workflows/ci.yml` to:
- Change build type
- Add more platforms (Linux, macOS)
- Modify triggers
- Add deployment steps

#### Jenkins
Edit `Jenkinsfile.groovy` to:
- Add build parameters
- Change timeout
- Add more stages
- Customize notifications

## 📊 Pipeline Status

After setup, you can:

- ✅ See build status in GitHub Actions or Jenkins
- ✅ Get notified on failures
- ✅ View detailed logs
- ✅ Download build artifacts
- ✅ See test results

## 🐛 Troubleshooting

### Common Issues

1. **"vcpkg not found"**
   - Set `VCPKG_ROOT` environment variable
   - Verify vcpkg installation path

2. **"CMake not found"**
   - Install CMake and add to PATH
   - Or specify full path in pipeline

3. **"Tests not running"**
   - Verify build completed successfully
   - Check test executables exist
   - Review CTest output

4. **"Pipeline script not found"** (Jenkins)
   - Verify `Jenkinsfile.groovy` exists in repo root
   - Check Script Path is correct
   - Ensure branch name matches

### Get Help

- Check [CI_PIPELINE_SETUP.md](CI_PIPELINE_SETUP.md) troubleshooting section
- Review console output for detailed error messages
- Verify all prerequisites are met

## 📈 Next Steps

After CI is working:

1. ✅ **Add notifications** (email, Slack, etc.)
2. ✅ **Set up code coverage** reporting
3. ✅ **Add more test types** (integration, performance)
4. ✅ **Configure CD** (Continuous Deployment) if needed
5. ✅ **Add build badges** to README

## 📝 Project Structure

```
youkugawaheartbeat-master/
├── .github/
│   └── workflows/
│       └── ci.yml              # GitHub Actions workflow
├── Jenkinsfile.groovy          # Jenkins pipeline script
├── CI_README.md                # This file
├── CI_PIPELINE_SETUP.md        # Complete documentation
├── JENKINS_QUICK_START.md      # Quick Jenkins guide
└── JENKINS_UI_CONFIGURATION.md # Jenkins UI guide
```

## ✅ Checklist

Use this checklist to verify your setup:

### GitHub Actions
- [ ] Workflow file exists (`.github/workflows/ci.yml`)
- [ ] Actions tab is enabled in GitHub
- [ ] Pushed a change to trigger pipeline
- [ ] Build completed successfully
- [ ] Tests passed

### Jenkins
- [ ] Jenkins server is running
- [ ] Required plugins installed
- [ ] Job created and configured
- [ ] Repository URL is correct
- [ ] Script Path is `Jenkinsfile.groovy`
- [ ] Environment variables set
- [ ] First build completed successfully
- [ ] Webhook configured (if using webhook trigger)

## 🎓 Learning Resources

- **Jenkins Pipeline**: https://www.jenkins.io/doc/book/pipeline/
- **GitHub Actions**: https://docs.github.com/en/actions
- **CMake**: https://cmake.org/documentation/
- **vcpkg**: https://vcpkg.io/

## 💬 Support

If you encounter issues:

1. Check the troubleshooting section in [CI_PIPELINE_SETUP.md](CI_PIPELINE_SETUP.md)
2. Review console/build logs for error messages
3. Verify all prerequisites are installed
4. Ensure configuration matches the guides

---

**Happy CI/CD! 🚀**


