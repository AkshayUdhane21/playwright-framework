@echo off
echo Fixing Map.of() compatibility issues...

REM Create a simple Java file to replace Map.of() with HashMap
echo Creating compatibility fix...

REM Use PowerShell to replace Map.of() with HashMap in all Java files
powershell -Command "
$files = Get-ChildItem -Path 'src\test\java\utils' -Filter '*.java' -Recurse
foreach ($file in $files) {
    $content = Get-Content $file.FullName -Raw
    if ($content -match 'Map\.of\(') {
        Write-Host \"Fixing $($file.Name)...\"
        # Replace Map.of() with HashMap
        $content = $content -replace 'Map\.of\(', 'new HashMap<String, Object>() {{'
        $content = $content -replace '\)', '}}'
        Set-Content $file.FullName -Value $content -NoNewline
    }
}
"

echo Map.of() compatibility fixes applied!
pause

