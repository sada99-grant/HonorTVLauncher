# GitHub Actions 自动编译 APK

## 配置步骤

1. 在 GitHub 仓库页面，点击 `Settings` → `Actions` → `General`
2. 找到 "Workflow permissions"，选择 **Read and write permissions**
3. 点击 `Save`

## 触发编译

### 方法一：推送代码（自动触发）

把本工程所有文件推送到仓库的 `main` 分支，推送后会自动开始编译。

### 方法二：手动触发

1. 进入仓库页面的 `Actions` 标签
2. 左侧选择 `Build APK`
3. 点击 `Run workflow` → 选择分支 → 点击绿色按钮
4. 等待 3-8 分钟

## 下载成品

编译完成后：

1. 在 `Actions` 标签下点击刚刚的那次运行
2. 页面底部 `Artifacts` 区域会出现 `app-debug-apk`
3. 点击下载，解压得到 `app-debug.apk`

## 查看错误

如果编译失败，点击对应步骤展开日志，红色字即为错误原因。
