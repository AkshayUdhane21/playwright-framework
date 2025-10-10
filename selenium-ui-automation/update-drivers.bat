@echo off
echo Updating WebDriver versions to latest...

REM Clean Maven cache to force fresh downloads
echo Cleaning Maven cache...
mvn dependency:purge-local-repository -DmanualInclude="io.github.bonigarcia:webdrivermanager"

REM Clear WebDriverManager cache
echo Clearing WebDriverManager cache...
if exist "%USERPROFILE%\.cache\selenium" rmdir /s /q "%USERPROFILE%\.cache\selenium"

REM Force download latest drivers
echo Downloading latest drivers...
mvn clean compile -DskipTests

echo.
echo ========================================
echo DRIVER UPDATE COMPLETED
echo ========================================
echo.
echo Latest drivers have been downloaded and cached.
echo You can now run your tests with the latest browser versions.
echo.

pause







