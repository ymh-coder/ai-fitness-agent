# AI Fitness Agent

基于 Spring Boot + Vue 3 的智能健身助手，集成 MCP 协议实现图片搜索与 AI 对话能力。

##项目结构

```
ai-fitness-agent/
├── ymh-ai-agent/              # AI 健身助手后端（Spring Boot 3 + DashScope）
├── image-search-mcp-server/   # MCP 图片搜索服务
└── fitness-agent-frontend/    # 前端聊天界面（Vue 3 + Vite）
```

## 功能特性

- 实时流式 AI 对话（SSE），支持打字机效果
- MCP 协议集成，Agent 可调用图片搜索等外部工具
- 专业健身指导：训练计划、饮食建议、动作纠正
- 现代渐变 UI 设计，响应式布局
- Swagger / Knife4j API 文档

## 技术栈

| 模块 | 技术 |
|------|------|
| 后端 | Spring Boot 3.4、Spring AI、DashScope (Qwen) |
| MCP | Spring AI MCP Client、MCP Server |
| 前端 | Vue 3.3、Vite 4、Axios、SSE |
| 工具 | Lombok、Knife4j、Maven Wrapper |

## 快速启动

### 前置要求

- Java 21+
- Node.js 18+
- Maven 3.9+

### 1. 启动 MCP 图片搜索服务

```bash
cd image-search-mcp-server
./mvnw spring-boot:run
```

### 2. 启动 AI 健身助手后端

```bash
cd ymh-ai-agent
./mvnw spring-boot:run
```

服务运行在 `http://localhost:8123/api`

### 3. 启动前端

```bash
cd fitness-agent-frontend
npm install
npm run dev
```

打开浏览器访问 Vite 开发服务器地址即可使用。

### API 文档

启动后端后访问：
- Swagger UI: `http://localhost:8123/api/swagger-ui.html`
- Knife4j: `http://localhost:8123/api/doc.html`
