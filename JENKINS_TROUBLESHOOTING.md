# Jenkins Pipeline Troubleshooting Guide

## Issues Fixed

### 1. Git Connectivity Issues
**Problem**: Jenkins cannot connect to GitHub (port 443 timeout)
```
fatal: unable to access 'https://github.com/AkshayUdhane21/playwright-framework.git/': 
Failed to connect to github.com port 443 after 21075 ms: Could not connect to server
```

**Solutions Applied**:
- Added Git environment variables for better connectivity
- Added Git configuration stage to disable SSL verification temporarily
- Increased buffer sizes and timeout settings

### 2. Pipeline Context Issues
**Problem**: `publishHTML` step missing required context
```
Required context class hudson.FilePath is missing
Perhaps you forgot to surround the publishHTML step with a step that provides this, such as: node
```

**Solution Applied**:
- Wrapped `publishHTML` and related steps in a `node` block within the `post` section

## Additional Troubleshooting Steps

### For Git Connectivity Issues:

1. **Check Jenkins Server Network**:
   ```bash
   # Test connectivity from Jenkins server
   curl -I https://github.com
   telnet github.com 443
   ```

2. **Configure Proxy (if behind corporate firewall)**:
   ```bash
   # In Jenkins Global Tool Configuration
   git config --global http.proxy http://proxy.company.com:8080
   git config --global https.proxy https://proxy.company.com:8080
   ```

3. **Alternative Git URLs**:
   - Try SSH instead of HTTPS: `git@github.com:AkshayUdhane21/playwright-framework.git`
   - Use GitHub's IP directly if DNS issues

4. **Jenkins Git Plugin Configuration**:
   - Go to Jenkins → Manage Jenkins → Configure System
   - Check Git configuration
   - Ensure Git executable path is correct

### For Pipeline Issues:

1. **Verify Required Plugins**:
   - HTML Publisher Plugin
   - TestNG Results Plugin
   - Email Extension Plugin

2. **Check Pipeline Syntax**:
   ```bash
   # Validate Jenkinsfile syntax
   curl -X POST -F "jenkinsfile=<Jenkinsfile" http://jenkins-server/pipeline-model-converter/validate
   ```

## Updated Jenkinsfile Features

1. **Git Connectivity Stage**: Automatically configures Git for better connectivity
2. **Proper Context Management**: All file operations now have proper context
3. **Error Handling**: Added try-catch blocks for critical operations
4. **Environment Variables**: Added Git-specific environment variables

## Testing the Fix

1. **Commit and Push Changes**:
   ```bash
   git add Jenkinsfile JENKINS_TROUBLESHOOTING.md
   git commit -m "Fix Jenkins pipeline Git connectivity and context issues"
   git push origin main
   ```

2. **Trigger Jenkins Build**:
   - Go to Jenkins dashboard
   - Click on your pipeline job
   - Click "Build Now"

3. **Monitor Build Logs**:
   - Check if Git connectivity stage passes
   - Verify all stages complete successfully
   - Check test reports are generated properly

## Common Issues and Solutions

### Issue: Still getting Git connection errors
**Solution**: 
- Check if Jenkins server has internet access
- Verify firewall settings
- Try using SSH keys instead of HTTPS

### Issue: Pipeline still fails with context errors
**Solution**:
- Ensure all file operations are within `node` blocks
- Check that required plugins are installed and enabled

### Issue: Email notifications not working
**Solution**:
- Configure SMTP settings in Jenkins
- Check email extension plugin configuration
- Verify email addresses are valid

## Next Steps

1. Test the updated pipeline
2. Monitor build success rates
3. Fine-tune Git configuration based on your network environment
4. Consider adding more robust error handling if needed

## Support

If issues persist:
1. Check Jenkins system logs: `/var/log/jenkins/jenkins.log`
2. Review pipeline console output for specific error messages
3. Verify all required Jenkins plugins are installed and up-to-date
