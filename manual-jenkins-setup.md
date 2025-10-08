# Manual Jenkins Setup Guide

Since the automated setup is failing due to authentication issues, here's a step-by-step manual setup guide for your Jenkins instance at `http://localhost:8086`.

## 🔐 Step 1: Get Your Jenkins Credentials

1. Go to `http://localhost:8086`
2. If you see a login page, note down your username and password
3. If you don't know your credentials, check:
   - Jenkins installation directory for `initialAdminPassword` file
   - Or look for credentials in your Jenkins configuration

## 🛠️ Step 2: Install Required Plugins

1. **Login to Jenkins** at `http://localhost:8086`
2. Go to **Manage Jenkins** → **Manage Plugins**
3. Click on **Available** tab
4. Search and install these plugins:
   - ✅ **Pipeline**
   - ✅ **Git**
   - ✅ **Allure Jenkins Plugin**
   - ✅ **HTML Publisher**
   - ✅ **Email Extension**
   - ✅ **Docker Pipeline**
   - ✅ **Build Timeout**
   - ✅ **Credentials Binding**
   - ✅ **Blue Ocean** (optional, for better UI)

5. Click **Install without restart** or **Download now and install after restart**

## 🔧 Step 3: Configure Global Tools

1. Go to **Manage Jenkins** → **Global Tool Configuration**
2. **JDK Configuration:**
   - Click **Add JDK**
   - Name: `JDK11`
   - Install automatically: ✅
   - Version: `11` or `17`

3. **Maven Configuration:**
   - Click **Add Maven**
   - Name: `Maven3`
   - Install automatically: ✅
   - Version: `3.9.6`

4. Click **Save**

## 📋 Step 4: Create the Pipeline Job

1. Go to **New Item** (or **+ New Item**)
2. Enter name: `playwright-test-pipeline`
3. Select **Pipeline**
4. Click **OK**

## ⚙️ Step 5: Configure the Pipeline Job

1. **General Tab:**
   - ✅ **Discard old builds**
   - Days to keep builds: `30`
   - Max # of builds to keep: `50`

2. **Build Triggers Tab:**
   - ✅ **Poll SCM**
   - Schedule: `H/15 * * * *` (every 15 minutes)
   - ✅ **Build periodically**
   - Schedule: `H 2 * * *` (daily at 2 AM)

3. **Pipeline Tab:**
   - Definition: **Pipeline script from SCM**
   - SCM: **Git**
   - Repository URL: `https://github.com/AkshayUdhane21/playwright-framework.git`
   - Credentials: Add your Git credentials if needed
   - Branch Specifier: `*/main` or `*/master`
   - Script Path: `Jenkinsfile`

4. **Parameters Tab:**
   - Click **Add Parameter** → **Choice Parameter**
   - Name: `TEST_ENV`
   - Choices: `local`, `staging`, `production`
   - Description: `Test environment to run against`

   - Click **Add Parameter** → **Choice Parameter**
   - Name: `BROWSER`
   - Choices: `chromium`, `firefox`, `webkit`
   - Description: `Browser to use for testing`

   - Click **Add Parameter** → **Boolean Parameter**
   - Name: `HEADLESS`
   - Default value: `true`
   - Description: `Run tests in headless mode`

   - Click **Add Parameter** → **String Parameter**
   - Name: `PARALLEL_THREADS`
   - Default value: `3`
   - Description: `Number of parallel test threads`

5. Click **Save**

## 🚀 Step 6: Test the Pipeline

1. Go to your `playwright-test-pipeline` job
2. Click **Build with Parameters**
3. Select your desired parameters:
   - TEST_ENV: `local`
   - BROWSER: `chromium`
   - HEADLESS: `true`
   - PARALLEL_THREADS: `3`
4. Click **Build**

## 📊 Step 7: Monitor the Build

1. Click on the build number to see details
2. Click **Console Output** to see real-time logs
3. Check **Test Results** for test execution results
4. View **Allure Report** (if available)

## 🔧 Troubleshooting

### If Build Fails:

1. **Check Console Output:**
   - Look for error messages
   - Check if all dependencies are installed

2. **Check Git Repository:**
   - Ensure the repository URL is correct
   - Verify you have access to the repository

3. **Check Jenkins Logs:**
   - Go to **Manage Jenkins** → **System Log**
   - Look for error messages

### Common Issues:

1. **Git Access Denied:**
   - Add Git credentials in **Manage Jenkins** → **Manage Credentials**
   - Use the credentials in the job configuration

2. **Maven Build Fails:**
   - Check if Maven is properly configured
   - Verify the `pom.xml` file is correct

3. **Docker Issues:**
   - Ensure Docker is running
   - Check Docker permissions

## 📝 Next Steps

After successful setup:

1. **Update Repository URL:** Change the Git repository URL to your actual repository
2. **Configure Email Notifications:** Set up email notifications for build results
3. **Add More Parameters:** Add additional parameters as needed
4. **Schedule Builds:** Configure build schedules according to your needs

## 🆘 Need Help?

If you encounter issues:

1. Check the Jenkins logs
2. Verify all plugins are installed
3. Ensure your credentials are correct
4. Check the console output for specific error messages

The pipeline should now be ready to run your Playwright tests!

