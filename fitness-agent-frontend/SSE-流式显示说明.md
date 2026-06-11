# SSE流式显示功能说明

## 🎯 功能特性

### 1. 双模式连接
- **SSE流式连接**：实时接收后端流式数据，支持打字机效果
- **HTTP普通请求**：传统请求方式，用于测试连接

### 2. 流式消息处理
- **实时拼接**：在一个消息气泡中持续拼接SSE返回的内容
- **流式显示**：消息内容实时更新，无需等待完整响应
- **打字机效果**：可选的逐字符显示效果

### 3. 智能显示模式
- **流式显示**（默认）：直接显示接收到的内容，适合快速响应
- **打字机效果**：逐字符显示，适合营造AI思考的视觉效果

## 🔧 技术实现

### SSE消息处理流程

```javascript
// 1. 建立SSE连接
const eventSource = new EventSource(url)

// 2. 监听消息流
eventSource.onmessage = (event) => {
  // 累积完整响应
  aiResponse += data.content
  
  // 实时更新消息内容
  updateMessageContent(aiResponse, messageIndex)
}

// 3. 流式显示更新
const updateMessageContent = (content, messageIndex) => {
  messages.value[messageIndex].content = content
  scrollToBottom()
}
```

### 打字机效果实现

```javascript
const typeWriterEffect = (fullText, messageIndex) => {
  const typeWriterTimer = setInterval(() => {
    if (currentIndex < targetText.length) {
      currentIndex++
      messages.value[messageIndex].content = targetText.substring(0, currentIndex)
      scrollToBottom()
    }
  }, 20) // 每20ms显示一个字符
}
```

## 🎮 使用方法

### 界面控制
1. **切换连接模式**：点击"切换到HTTP"按钮
2. **控制打字机效果**：点击"开启打字机"按钮
3. **查看当前状态**：界面显示当前连接模式和效果设置

### 推荐设置
- **开发测试**：使用HTTP模式 + 关闭打字机
- **生产环境**：使用SSE模式 + 关闭打字机（流式显示）
- **演示效果**：使用SSE模式 + 开启打字机

## 🔍 调试功能

### 控制台日志
- 连接状态：`SSE连接已打开`
- 消息接收：`收到SSE消息: [内容]`
- 错误信息：详细的错误诊断

### 错误处理
- **连接失败**：检查后端服务状态
- **CORS错误**：检查后端CORS配置
- **超时处理**：30秒自动超时保护

## 📋 后端要求

### SSE接口规范
```java
@GetMapping(value = "/manus/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public SseEmitter doChatWithManus(String message) {
    // 返回SSE流
    return fitnessManus.runStream(message);
}
```

### 数据格式
- **JSON格式**：`{"content": "消息内容"}`
- **结束标记**：`[DONE]`
- **响应头**：`Content-Type: text/event-stream`

## 🚀 性能优化

### 内存管理
- 自动清理打字机定时器
- 防止重复创建消息气泡
- 智能滚动到底部

### 用户体验
- 实时响应，无需等待
- 流畅的滚动效果
- 清晰的加载状态指示

## 🐛 常见问题

### Q: 消息显示不完整？
A: 检查后端SSE流是否正确发送`[DONE]`标记

### Q: 打字机效果卡顿？
A: 调整打字机间隔时间（当前20ms）

### Q: 连接频繁断开？
A: 检查后端CORS配置和网络连接

### Q: 消息重复显示？
A: 确保后端不会重复发送相同内容

