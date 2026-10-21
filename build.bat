@echo off
chcp 65001 >nul
setlocal

echo ============================================
echo   荣耀电视轻量桌面 - 构建脚本
echo ============================================
echo.

where java >nul 2>nul
if errorlevel 1 (
    echo [错误] 未检测到 Java。
    echo 请先安装 JDK 17，并配置 JAVA_HOME。
    pause
    exit /b 1
)

echo 正在检查 Java 版本...
java -version 2>&1 | findstr /i "version" 

echo.
echo 正在检查 Android SDK...
if not defined ANDROID_HOME if not defined ANDROID_SDK_ROOT (
    echo [警告] 未设置 ANDROID_HOME 或 ANDROID_SDK_ROOT。
    echo 请确保已安装 Android SDK Build-Tools 28.0.3 和 Android 28 平台。
    echo.
)

if defined ANDROID_HOME echo ANDROID_HOME=%ANDROID_HOME%
if defined ANDROID_SDK_ROOT echo ANDROID_SDK_ROOT=%ANDROID_SDK_ROOT%

echo.
echo 请使用 Android Studio 打开本工程并点击 Build - Make Project。
echo 或在本目录下执行：
echo.
echo   gradlew.bat assembleDebug
echo.
echo 输出位置： app\build\outputs\apk\debug\app-debug.apk
echo.
pause
