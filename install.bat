@echo off
chcp 65001 >nul
setlocal

echo ============================================
echo   荣耀电视轻量桌面 - 安装说明
echo ============================================
echo.
echo 注意：荣耀智慧屏 X1 的系统未开放 ADB 网络调试，
echo 以下命令仅在其他支持 ADB 的安卓设备上使用。
echo.
echo 推荐做法（适用于 X1）：
echo.
echo 1. 在电脑上构建 APK：
echo      app\build\outputs\apk\debug\app-debug.apk
echo.
echo 2. 将 APK 复制到 U 盘根目录。
echo.
echo 3. 在电视上打开自带文件管理器，
echo    进入 U 盘，点击 APK 进行安装。
echo.
echo 4. 安装后不要立即禁用原厂桌面。
echo.
echo 5. 进入：
echo      设置 - 应用 - 默认应用 - 主屏幕应用
echo    选择“荣耀电视轻量桌面”。
echo.
echo 6. 若系统没有“主屏幕应用”选项，
echo    说明本电视不支持替换桌面，请直接卸载本应用。
echo.
echo 7. 恢复原厂桌面的方法：
echo     设置 - 应用 - 荣耀电视轻量桌面 - 卸载
echo     或恢复出厂设置。
echo.
echo --------------------------------------------
echo 仅在支持 ADB 的设备上使用下面命令：
echo   adb install -r app-debug.apk
echo   adb shell pm set-home-activity com.honor.tvlauncher/.LauncherActivity
echo --------------------------------------------
echo.
pause
