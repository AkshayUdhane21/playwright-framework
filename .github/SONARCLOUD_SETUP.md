# SonarCloud Setup - Quick Guide

## ⚡ Quick Setup (5 minutes)

### Step 1: Get Your SonarCloud Token

1. Go to [https://sonarcloud.io](https://sonarcloud.io)
2. Sign in with your GitHub account (AkshayUdhane21)
3. Go to: **My Account → Security** → [Direct Link](https://sonarcloud.io/account/security)
4. Click **"Generate Token"**
5. Name it: `GitHub Actions`
6. **Copy the token** (you won't see it again!)

### Step 2: Add GitHub Secrets

1. Go to your GitHub repository
2. Click **Settings** → **Secrets and variables** → **Actions**
3. Click **"New repository secret"**

   **Secret 1:**
   - Name: `SONAR_TOKEN`
   - Value: Paste your SonarCloud token from Step 1

   **Secret 2:**
   - Name: `SONAR_HOST_URL`
   - Value: `https://sonarcloud.io` (exactly this, no trailing slash)

### Step 3: Create SonarCloud Project

1. Go to [SonarCloud](https://sonarcloud.io)
2. Click **"Analyze a new project"**
3. Select organization: **AkshayUdhane21**
4. Select your repository
5. **Important**: When creating the project, use this project key:
   - `AkshayUdhane21_ats-yokogawa-conn-service`
   - (This matches your `sonar-project.properties` file)

### Step 4: Test with a PR

1. Create a new branch:
   ```bash
   git checkout -b test-sonarcloud
   git commit --allow-empty -m "Test SonarCloud"
   git push origin test-sonarcloud
   ```

2. Create a Pull Request from `test-sonarcloud` to `main` (or `master`)

3. The workflow will automatically run!

4. Check results:
   - Go to **Actions** tab to see the workflow
   - Go to **Checks** tab on your PR to see SonarCloud results
   - Go to [SonarCloud](https://sonarcloud.io) to see detailed analysis

## ✅ Verification Checklist

- [ ] SonarCloud token generated and copied
- [ ] GitHub Secret `SONAR_TOKEN` added
- [ ] GitHub Secret `SONAR_HOST_URL` added with value `https://sonarcloud.io`
- [ ] SonarCloud project created with key: `AkshayUdhane21_ats-yokogawa-conn-service`
- [ ] Test PR created and workflow ran successfully

## 🔍 Troubleshooting

**Error: "Failed to query server version"**
- Check that both secrets are set correctly
- Verify `SONAR_HOST_URL` is exactly `https://sonarcloud.io` (no trailing slash)
- Make sure the token was copied correctly (no extra spaces)

**Error: "Project key does not exist"**
- Verify the project key in SonarCloud matches: `AkshayUdhane21_ats-yokogawa-conn-service`
- Check `sonar-project.properties` file

**Workflow not running on PR**
- Make sure you're creating a PR to `main`, `master`, `develop`, or `akshay` branch
- Check the "Actions" tab to see if the workflow was triggered

## 📝 Current Configuration

- **Organization**: `AkshayUdhane21`
- **Project Key**: `AkshayUdhane21_ats-yokogawa-conn-service`
- **Host URL**: `https://sonarcloud.io`

These are already configured in `sonar-project.properties` ✅

