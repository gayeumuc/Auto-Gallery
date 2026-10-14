@echo off
set SDK=%LOCALAPPDATA%\Android\Sdk
set INI=%~dp0bmw_screen.ini
echo ADB forward baslatiliyor...
"%SDK%\platform-tools\adb.exe" forward tcp:5277 tcp:5277
echo DHU baslatiliyor...
"%SDK%\extras\google\auto\desktop-head-unit.exe" -c "%INI%"
pause