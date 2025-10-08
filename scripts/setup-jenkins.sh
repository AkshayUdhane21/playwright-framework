#!/bin/bash

# Jenkins Setup Script for Playwright Test Framework
# This script sets up Jenkins with required plugins and configurations

set -e

echo "Setting up Jenkins for Playwright Test Framework..."

# Jenkins URL (update this with your Jenkins URL)
JENKINS_URL="${JENKINS_URL:-http://localhost:8086}"

# Jenkins admin credentials (update these)
JENKINS_USER="${JENKINS_USER:-admin}"
JENKINS_PASS="${JENKINS_PASS:-admin}"

# Required Jenkins plugins
PLUGINS=(
    "workflow-aggregator"
    "git"
    "allure-jenkins-plugin"
    "extent-reports"
    "htmlpublisher"
    "email-ext"
    "build-timeout"
    "credentials-binding"
    "timestamper"
    "ws-cleanup"
    "ant"
    "gradle"
    "maven-plugin"
    "docker-workflow"
    "docker-plugin"
    "blueocean"
    "pipeline-stage-view"
    "build-trigger-badge"
    "build-timeout"
    "parameterized-trigger"
    "copyartifact"
    "envinject"
    "config-file-provider"
    "job-dsl"
    "configuration-as-code"
    "slack"
    "telegram-notifications"
    "discord-notifier"
    "mattermost"
    "jira"
    "confluence"
    "sonar"
    "checkstyle"
    "pmd"
    "findbugs"
    "warnings-ng"
    "dependency-check-jenkins-plugin"
    "owasp-dependency-check"
    "snyk-security-scanner"
    "aqua-security-scanner"
    "veracode-scanner"
    "fortify"
    "coverity"
    "jacoco"
    "cobertura"
    "clover"
    "performance"
    "blazemeter"
    "gatling"
    "jmeter"
    "artifactory"
    "nexus-artifact-uploader"
    "deployit"
    "deploy"
    "ssh"
    "ssh-slaves"
    "ssh-credentials"
    "ssh-agent"
    "publish-over-ssh"
    "ssh-steps"
    "ssh-credentials"
    "ssh-agent"
    "publish-over-ssh"
    "ssh-steps"
)

# Function to install Jenkins plugin
install_plugin() {
    local plugin=$1
    echo "Installing plugin: $plugin"
    java -jar jenkins-cli.jar -s "$JENKINS_URL" -auth "$JENKINS_USER:$JENKINS_PASS" install-plugin "$plugin" || true
}

# Download Jenkins CLI
echo "Downloading Jenkins CLI..."
wget -O jenkins-cli.jar "$JENKINS_URL/jnlpJars/jenkins-cli.jar" || {
    echo "Failed to download Jenkins CLI. Please ensure Jenkins is running and accessible."
    exit 1
}

# Install plugins
echo "Installing required plugins..."
for plugin in "${PLUGINS[@]}"; do
    install_plugin "$plugin"
done

# Restart Jenkins
echo "Restarting Jenkins..."
java -jar jenkins-cli.jar -s "$JENKINS_URL" -auth "$JENKINS_USER:$JENKINS_PASS" restart || true

# Wait for Jenkins to restart
echo "Waiting for Jenkins to restart..."
sleep 60

# Create credentials for GitHub (if needed)
echo "Setting up credentials..."
cat > credentials.xml << EOF
<?xml version='1.1' encoding='UTF-8'?>
<com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl>
  <scope>GLOBAL</scope>
  <id>github-credentials</id>
  <description>GitHub credentials for repository access</description>
  <username>\${GITHUB_USERNAME}</username>
  <password>\${GITHUB_TOKEN}</password>
</com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl>
EOF

# Create global tool configurations
echo "Setting up global tools..."
cat > tools.xml << EOF
<?xml version='1.1' encoding='UTF-8'?>
<tool>
  <installations>
    <hudson.model.JDK>
      <name>JDK11</name>
      <properties>
        <hudson.tools.InstallSourceProperty>
          <installers>
            <hudson.tools.JDKInstaller>
              <id>11</id>
            </hudson.tools.JDKInstaller>
          </installers>
        </hudson.tools.InstallSourceProperty>
      </properties>
    </hudson.model.JDK>
    <hudson.tasks.Maven>
      <name>Maven3</name>
      <properties>
        <hudson.tools.InstallSourceProperty>
          <installers>
            <hudson.tools.MavenInstaller>
              <id>3.9.6</id>
            </hudson.tools.MavenInstaller>
          </installers>
        </hudson.tools.InstallSourceProperty>
      </properties>
    </hudson.tasks.Maven>
  </installations>
</tool>
EOF

# Create job from XML
echo "Creating Jenkins job..."
java -jar jenkins-cli.jar -s "$JENKINS_URL" -auth "$JENKINS_USER:$JENKINS_PASS" create-job "playwright-test-pipeline" < jenkins-job-config.xml || {
    echo "Failed to create job. Please check the job configuration."
}

echo "Jenkins setup completed!"
echo "Access Jenkins at: $JENKINS_URL"
echo "Job created: playwright-test-pipeline"

# Cleanup
rm -f jenkins-cli.jar credentials.xml tools.xml

echo "Setup script completed successfully!"
