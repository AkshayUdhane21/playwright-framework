# How to Run SonarQube with GitHub Actions

## Step-by-Step Guide

### Step 1: Set Up SonarQube/SonarCloud Account

#### Option A: Using SonarCloud (Free for Public Repos)

1. Go to [https://sonarcloud.io](https://sonarcloud.io)
2. Click **"Log in"** and sign in with your GitHub account
3. Click **"Analyze a new project"**
4. Select your GitHub organization/user
5. Select this repository (`youkugawaheartbeat-master` or your repo name)
6. SonarCloud will create a project and show you the **Project Key** (e.g., `your-org_ats-yokogawa-conn-service`)

#### Option B: Using Self-Hosted SonarQube

1. Access your SonarQube server
2. Log in and create a new project
3. Use project key: `ats-yokogawa-conn-service` (or update `sonar-project.properties`)

### Step 2: Generate SonarQube Token

**For SonarCloud:**
1. In SonarCloud, click your profile icon (top right)
2. Go to **"My Account"** → **"Security"**
3. Under **"Generate Tokens"**, enter a name (e.g., "GitHub Actions")
4. Click **"Generate"**
5. **Copy the token immediately** (you won't see it again!)

**For Self-Hosted SonarQube:**
1. Log in to SonarQube
2. Click your profile → **"My Account"** → **"Security"**
3. Generate a token and copy it

### Step 3: Configure GitHub Secrets

1. Go to your GitHub repository
2. Click **"Settings"** (top menu)
3. In the left sidebar, click **"Secrets and variables"** → **"Actions"**
4. Click **"New repository secret"**
5. Add these two secrets:

   **Secret 1:**
   - Name: `SONAR_TOKEN`
   - Value: Paste the token you copied in Step 2
   - Click **"Add secret"**

   **Secret 2:**
   - Name: `SONAR_HOST_URL`
   - Value: 
     - For SonarCloud: `https://sonarcloud.io`
     - For Self-Hosted: Your SonarQube URL (e.g., `https://sonarqube.yourcompany.com`)
   - Click **"Add secret"**

### Step 4: Verify Project Key (if using SonarCloud)

If SonarCloud generated a different project key:

1. Open `sonar-project.properties` in your repository
2. Update the `sonar.projectKey` line to match your SonarCloud project key
3. Commit and push the change

### Step 5: Run SonarQube Analysis

You have **3 ways** to trigger the workflow:

#### Method 1: Push to GitHub (Automatic)
```bash
git add .
git commit -m "Add GitHub Actions workflows"
git push origin main  # or master/develop
```
The workflow will run automatically!

#### Method 2: Create a Pull Request
1. Create a new branch
2. Make some changes
3. Push the branch
4. Create a Pull Request to `main`, `master`, or `develop`
5. The workflow will run automatically

#### Method 3: Manual Trigger (Workflow Dispatch)
1. Go to your GitHub repository
2. Click the **"Actions"** tab
3. In the left sidebar, select **"SonarQube Analysis"** workflow
4. Click **"Run workflow"** button (top right)
5. Select the branch and click **"Run workflow"**

### Step 6: View Results

#### In GitHub Actions:
1. Go to **"Actions"** tab in your repository
2. Click on the workflow run to see progress
3. Expand each step to see detailed logs

#### In SonarQube/SonarCloud:
1. Go to your SonarQube/SonarCloud dashboard
2. Select your project
3. You'll see:
   - Code quality metrics
   - Code smells
   - Security vulnerabilities
   - Code coverage (if configured)
   - Technical debt

## Quick Checklist

- [ ] SonarQube/SonarCloud account created
- [ ] Project created in SonarQube/SonarCloud
- [ ] Token generated and copied
- [ ] `SONAR_TOKEN` secret added to GitHub
- [ ] `SONAR_HOST_URL` secret added to GitHub
- [ ] Project key matches in `sonar-project.properties`
- [ ] Code pushed to GitHub or workflow manually triggered

## Troubleshooting

### Workflow Fails with "SONAR_TOKEN not found"
- Make sure you added the secret in: **Settings → Secrets and variables → Actions**
- Secret name must be exactly: `SONAR_TOKEN` (case-sensitive)

### Workflow Fails with "Authentication failed"
- Verify your token is correct
- Check if token has expired (generate a new one)
- For SonarCloud, ensure you're using `https://sonarcloud.io` as `SONAR_HOST_URL`

### Build Wrapper Fails
- Check the build logs in GitHub Actions
- Ensure CMake configuration is successful before the build wrapper step

### No Results in SonarQube
- Wait a few minutes after the workflow completes
- Check SonarQube/SonarCloud dashboard
- Verify the project key matches

## What Happens When It Runs?

1. **Checkout**: Downloads your code
2. **Setup vcpkg**: Downloads and sets up the package manager
3. **Install Dependencies**: Installs CURL, nlohmann-json, spdlog, OpenSSL, open62541pp, gtest
4. **Download Build Wrapper**: Gets SonarQube's C/C++ build wrapper
5. **Configure CMake**: Sets up the build system
6. **Build**: Compiles your code (wrapped by SonarQube to capture compilation info)
7. **Test**: Runs your tests
8. **Scan**: Analyzes code and uploads results to SonarQube/SonarCloud

## Expected Duration

- First run: ~10-15 minutes (installing dependencies)
- Subsequent runs: ~5-8 minutes (with caching)

---

**Need help?** Check the detailed setup guide: [GITHUB_ACTIONS_SETUP.md](./GITHUB_ACTIONS_SETUP.md)








