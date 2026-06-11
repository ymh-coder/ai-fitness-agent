# AI健身大师前端应用

这是一个基于Vue3的AI健身大师聊天应用，提供实时对话功能。

## 功能特性

- 🎯 聊天室界面设计，用户消息在右侧，AI消息在左侧
- 💬 实时SSE连接，支持流式对话
- 🎨 现代化UI设计，渐变背景和动画效果
- 📱 响应式布局，适配不同屏幕尺寸
- ⚡ 基于Vue3 Composition API开发

## 技术栈

- Vue 3.3.4
- Vite 4.4.9
- Axios 1.5.0
- Server-Sent Events (SSE)

## 项目结构

```
fitness-agent-frontend/
├── src/
│   ├── App.vue          # 主应用组件
│   ├── main.js          # 应用入口
│   └── style.css        # 全局样式
├── index.html           # HTML模板
├── package.json         # 项目配置
├── vite.config.js       # Vite配置
└── README.md            # 项目说明
```

## 安装和运行

### 1. 安装依赖
```bash
npm install
```

### 2. 启动开发服务器
```bash
npm run dev
```

### 3. 构建生产版本
```bash
npm run build
```

### 4. 预览生产版本
```bash
npm run preview
```

## 后端接口

项目需要连接后端API：
- 接口地址：`http://localhost:8123/api/ai/manus/chat`
- 方法：GET
- 参数：`message` (用户输入的消息)
- 返回：Server-Sent Events流

## 使用说明

1. 打开应用后，AI会自动发送开场白
2. 在输入框中输入您的问题
3. 点击发送按钮或按回车键发送消息
4. AI会通过SSE实时返回响应内容

## 注意事项

- 确保后端服务已启动并运行在 `http://localhost:8123`
- 如果遇到PowerShell执行策略问题，请运行：
  ```powershell
  Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
  ```
