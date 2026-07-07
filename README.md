# 莫尔斯输入法 (Morse Input Method)

一款基于 Jetpack Compose 的 Android 莫尔斯码输入法应用，支持手动键和自动键两种输入模式。

## ✨ 特性

- **手动键模式**：单一大按钮，按压力度区分点(·)和划(-)
- **自动键模式**：左右分布点划按钮，快速输入
- **智能空格**：停顿1秒自动添加空格分隔字符
- **实时转码**：输入时实时显示莫尔斯码和解码结果
- **拟物设计**：现代化的深色/浅色主题
- **音效反馈**：支持按键音效和震动反馈

## 🚀 使用方法

### 手动键模式
- **短按** (< 300ms)：输入点 (·)
- **长按** (≥ 300ms)：输入划 (-)
- 停顿1秒后自动添加空格

### 自动键模式
- 点击红色圆点按钮：输入点 (·)
- 点击蓝色长条按钮：输入划 (-)
- 停顿1秒后自动添加空格

### 功能键
- **退格**：删除最后一个字符
- **空格**：手动添加空格
- **Clear**：清空所有输入

## 🛠️ 技术栈

- **Android SDK**：API 26+
- **Jetpack Compose**：2024.06.00
- **Kotlin**：1.9.24
- **Material3**：最新稳定版

## 📁 项目结构

```
app/src/main/java/kg/edu/yjut/morseinputmethod/
├── services/
│   └── MorseInputService.kt      # 输入法服务
├── view/
│   └── MorseKeyboardScreen.kt    # Compose 键盘界面
├── viewmodel/
│   └── MorseViewModel.kt         # 状态管理
├── entity/
│   └── MorseCode.kt              # 数据模型
└── helper/
    └── AssetsDatabaseManager.kt  # 数据库管理
```

## 📥 安装

1. 克隆仓库：
   ```bash
   git clone https://github.com/trueWangSyutung/Morse-input-method-OpenSource.git
   ```

2. 使用 Android Studio 打开项目

3. 构建并运行到设备或模拟器

## 📝 莫尔斯码表

| 字母 | 码 | 字母 | 码 |
|------|-----|------|-----|
| A | ·- | N | -· |
| B | -··· | O | --- |
| C | -·-· | P | ·--· |
| D | -·· | Q | --·- |
| E | · | R | ·-· |
| F | ··-· | S | ··· |
| G | --· | T | - |
| H | ···· | U | ··- |
| I | ·· | V | ···- |
| J | ·--- | W | ·-- |
| K | -·- | X | -··- |
| L | ·-·· | Y | -·-- |
| M | -- | Z | --·· |

## 📄 许可证

MIT License

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

---

**Made with ❤️ for Morse Code enthusiasts**