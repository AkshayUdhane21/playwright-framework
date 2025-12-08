# Jenkins UI Configuration - Step by Step

This guide shows exactly what to select in the Jenkins configuration page.

## 📋 Configuration Checklist

### ✅ General Section

- [ ] **Description**: (Optional) Add a description of your project
- [ ] **Discard old builds**: ✅ Check this (recommended)
  - **Strategy**: Log Rotation
  - **Days to keep builds**: 30
  - **Max # of builds to keep**: 50
- [ ] **GitHub project**: ✅ **CHECK THIS**
  - **Project url**: `https://github.com/AkshayUdhane21/playwright-framework`
    - ⚠️ Replace with your actual repository URL
- [ ] **Do not allow concurrent builds**: ⬜ Leave unchecked (unless you need sequential builds)
- [ ] **Do not allow the pipeline to resume if the controller restarts**: ⬜ Leave unchecked

---

### ✅ Triggers Section

**Choose ONE of these options:**

#### Option 1: GitHub Webhook (Recommended) ⭐
- [ ] **GitHub hook trigger for GITScm polling**: ✅ **CHECK THIS**
- [ ] **Poll SCM**: ⬜ Leave unchecked
- [ ] **Build periodically**: ⬜ Leave unchecked
- [ ] **Build after other projects are built**: ⬜ Leave unchecked

**Then configure webhook in GitHub:**
1. Go to your GitHub repo → **Settings** → **Webhooks**
2. Click **Add webhook**
3. **Payload URL**: `http://your-jenkins-server:8080/github-webhook/`
4. **Content type**: `application/json`
5. **Events**: Select **"Just the push event"**
6. Click **Add webhook**

#### Option 2: Poll SCM (Alternative)
- [ ] **Poll SCM**: ✅ **CHECK THIS**
  - **Schedule**: `H/15 * * * *` (every 15 minutes)
    - Or `H/5 * * * *` (every 5 minutes)
    - Or `H * * * *` (every hour)
- [ ] **GitHub hook trigger for GITScm polling**: ⬜ Leave unchecked
- [ ] **Build periodically**: ⬜ Leave unchecked

#### Option 3: Build Periodically (Scheduled)
- [ ] **Build periodically**: ✅ **CHECK THIS**
  - **Schedule**: `0 2 * * *` (daily at 2 AM)
- [ ] **Poll SCM**: ⬜ Leave unchecked
- [ ] **GitHub hook trigger for GITScm polling**: ⬜ Leave unchecked

---

### ✅ Pipeline Section (MOST IMPORTANT!)

#### Step 1: Definition Dropdown
- **Definition**: Select **"Pipeline script from SCM"** ⚠️
  - ⚠️ **DO NOT** select "Pipeline script" (that's for inline scripts)
  - ✅ **SELECT** "Pipeline script from SCM" (loads from repository)

#### Step 2: SCM Configuration
After selecting "Pipeline script from SCM", you'll see:

- **SCM**: Select **"Git"** from dropdown

#### Step 3: Repository Configuration
- **Repositories**:
  - **Repository URL**: 
    ```
    https://github.com/AkshayUdhane21/playwright-framework.git
    ```
    - ⚠️ Replace with your actual repository URL
    - For SSH: `git@github.com:AkshayUdhane21/playwright-framework.git`
  
  - **Credentials**: 
    - If **public repo**: Leave empty or select "none"
    - If **private repo**: Click **Add** → **Jenkins** → Enter:
      - **Username**: Your GitHub username
      - **Password**: Your GitHub Personal Access Token (not password!)
      - Or use SSH key credentials
  
  - **Branches to build**: 
    - `*/main` (for main branch)
    - `*/Akshay` (for Akshay branch)
    - `*/master` (for master branch)
    - Or `**` (all branches)

#### Step 4: Script Path
- **Script Path**: `Jenkinsfile.groovy`
  - This tells Jenkins which file in your repo contains the pipeline script
  - ⚠️ Must match the filename exactly (case-sensitive)

#### Step 5: Additional Options
- **Lightweight checkout**: ⬜ **UNCHECK THIS**
  - We need full checkout for vcpkg to work properly

---

### ✅ Advanced Options (Optional)

- **Use Groovy Sandbox**: 
  - ✅ **Checked** = More secure (recommended if script is trusted)
  - ⬜ **Unchecked** = Full Groovy access (use if you need advanced features)

---

## 🎯 Complete Configuration Summary

Here's what your configuration should look like:

```
✅ General
  ✅ GitHub project
    Project url: https://github.com/AkshayUdhane21/playwright-framework
  ✅ Discard old builds

✅ Triggers
  ✅ GitHub hook trigger for GITScm polling
  (OR)
  ✅ Poll SCM
    Schedule: H/15 * * * *

✅ Pipeline
  Definition: Pipeline script from SCM ⚠️
  SCM: Git
  Repository URL: https://github.com/AkshayUdhane21/playwright-framework.git
  Branches: */main (or */Akshay)
  Script Path: Jenkinsfile.groovy
  Lightweight checkout: ⬜ Unchecked
  Use Groovy Sandbox: ✅ Checked
```

---

## ⚠️ Common Mistakes to Avoid

1. ❌ **Selecting "Pipeline script" instead of "Pipeline script from SCM"**
   - This will show a text editor instead of SCM options
   - You'd have to copy-paste the entire Jenkinsfile manually

2. ❌ **Wrong Script Path**
   - Must be exactly: `Jenkinsfile.groovy`
   - Case-sensitive!

3. ❌ **Wrong Branch Name**
   - Use `*/main` not `main`
   - Use `*/Akshay` not `Akshay`
   - The `*/` prefix is required

4. ❌ **Forgetting to Configure Webhook**
   - If using "GitHub hook trigger", you MUST configure webhook in GitHub
   - Otherwise builds won't trigger automatically

5. ❌ **Using Password Instead of Token**
   - For private repos, use GitHub Personal Access Token
   - Not your GitHub password

---

## 🔍 Verification Steps

After saving configuration:

1. ✅ Go to job dashboard
2. ✅ Click **Build Now**
3. ✅ Check **Console Output**
4. ✅ Verify it says "Checking out source code..."
5. ✅ Verify it finds `Jenkinsfile.groovy`
6. ✅ Watch the build progress

If you see errors:
- Check repository URL is correct
- Verify branch name matches
- Ensure `Jenkinsfile.groovy` exists in repo root
- Check Jenkins console for detailed error messages

---

## 📸 What You Should See

After correct configuration:

1. **Pipeline Definition** section should show:
   - Definition: **"Pipeline script from SCM"** (selected)
   - SCM: **"Git"** (selected)
   - Repository URL: Your GitHub URL
   - Script Path: `Jenkinsfile.groovy`

2. **Triggers** section should show:
   - Either "GitHub hook trigger" checked
   - OR "Poll SCM" checked with schedule

3. **General** section should show:
   - "GitHub project" checked
   - Project URL filled in

---

## 🚀 Next Steps

1. ✅ Save the configuration
2. ✅ Click **Build Now** to test
3. ✅ Check console output for any errors
4. ✅ If successful, push a change to trigger automatic build
5. ✅ Monitor build results

---

## 💡 Pro Tips

- **Test with "Build Now" first** before relying on automatic triggers
- **Check console output** - it shows exactly what Jenkins is doing
- **Use "Pipeline Syntax" link** to generate Groovy code snippets
- **Keep Jenkinsfile in version control** - changes are tracked
- **Use build parameters** for flexibility (BUILD_TYPE, VCPKG_ROOT, etc.)

---

For more details, see:
- [JENKINS_QUICK_START.md](JENKINS_QUICK_START.md) - Quick setup guide
- [CI_PIPELINE_SETUP.md](CI_PIPELINE_SETUP.md) - Complete documentation


