# Quick Start: GitHub Actions & SonarQube

## 🚀 Quick Setup (5 minutes)

### 1. Configure GitHub Secrets

Go to your repository: **Settings → Secrets and variables → Actions**

Add these secrets:

| Secret Name | Value | Description |
|------------|-------|-------------|
| `SONAR_TOKEN` | Your SonarQube/SonarCloud token | Generated from SonarQube/SonarCloud account |
| `SONAR_HOST_URL` | `https://sonarcloud.io` or your SonarQube URL | Your SonarQube server URL |

### 2. Get SonarQube Token

**For SonarCloud:**
1. Go to [sonarcloud.io](https://sonarcloud.io) and sign in
2. Create/select your project
3. Go to: **Account → Security → Generate Token**
4. Copy the token to `SONAR_TOKEN` secret

**For Self-Hosted SonarQube:**
1. Log in to your SonarQube server
2. Go to: **My Account → Security → Generate Token**
3. Copy the token to `SONAR_TOKEN` secret
4. Set `SONAR_HOST_URL` to your server URL

### 3. Verify Project Key

Check `sonar-project.properties` - the `sonar.projectKey` should match your SonarQube project key.

### 4. Push to GitHub

The workflows will automatically run on:
- Push to `main`, `master`, or `develop` branches
- Pull requests to those branches
- Manual trigger from Actions tab

## 📋 What Gets Built

- ✅ **Linux** builds (Ubuntu)
- ✅ **Windows** builds
- ✅ **Tests** execution
- ✅ **SonarQube** code analysis

## 📚 Full Documentation

See [GITHUB_ACTIONS_SETUP.md](./GITHUB_ACTIONS_SETUP.md) for detailed setup instructions and troubleshooting.









