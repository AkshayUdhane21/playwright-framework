@echo off
echo ========================================
echo Fixing All Map.of() Compatibility Issues
echo ========================================

echo Replacing Map.of() with HashMap in MockServiceManager.java...

powershell -Command "
$file = 'src\test\java\utils\MockServiceManager.java'
$content = Get-Content $file -Raw

# Replace Map.of() patterns
$content = $content -replace 'Map\.of\(([^)]+)\)', 'new HashMap<String, Object>() {{ put($1) }}'

# Fix the put() calls to be proper
$content = $content -replace 'put\(([^,]+), ([^,]+), ([^)]+)\)', 'put($1, $2); put($3, $4)'
$content = $content -replace 'put\(([^,]+), ([^,]+), ([^,]+), ([^,]+), ([^)]+)\)', 'put($1, $2); put($3, $4); put($5, $6)'

Set-Content $file -Value $content -NoNewline
"

echo Replacing Map.of() with HashMap in MockBackendServer.java...

powershell -Command "
$file = 'src\test\java\utils\MockBackendServer.java'
$content = Get-Content $file -Raw

# Replace Map.of() patterns
$content = $content -replace 'Map\.of\(([^)]+)\)', 'new HashMap<String, Object>() {{ put($1) }}'

Set-Content $file -Value $content -NoNewline
"

echo ✅ All Map.of() compatibility issues fixed!
echo.
echo The utils package should now compile without errors.
echo.
pause

