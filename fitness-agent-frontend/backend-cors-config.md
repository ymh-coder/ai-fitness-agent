# 后端CORS配置说明

## 问题分析

前端SSE连接失败的主要原因可能是：

1. **CORS跨域问题** - 前端运行在 `http://localhost:3000`，后端在 `http://localhost:8123`
2. **SSE响应头配置** - 需要正确设置SSE相关的响应头
3. **接口路径问题** - 确保接口路径正确

## 后端配置建议

### 1. 添加CORS配置

在您的SpringBoot项目中添加以下配置：

```java
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000", "http://127.0.0.1:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
```

### 2. 修改AiController

```java
@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"})
public class AiController {

    @GetMapping(value = "/manus/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter doChatWithManus(String message) {
        SseEmitter emitter = new SseEmitter(30000L); // 30秒超时
        
        try {
            FitnessManus fitnessManus = new FitnessManus(allTools, dashscopeChatModel);
            return fitnessManus.runStream(message);
        } catch (Exception e) {
            emitter.completeWithError(e);
            return emitter;
        }
    }
}
```

### 3. 确保SSE响应头正确

SSE需要以下响应头：
- `Content-Type: text/event-stream`
- `Cache-Control: no-cache`
- `Connection: keep-alive`

## 测试步骤

1. 打开浏览器访问 `http://localhost:3000/test-connection.html`
2. 点击"测试连接"按钮
3. 查看控制台日志和错误信息
4. 根据错误信息调整后端配置

## 常见问题解决

### 问题1: CORS错误
```
Access to fetch at 'http://localhost:8123/api/ai/manus/chat' from origin 'http://localhost:3000' has been blocked by CORS policy
```
**解决方案**: 添加CORS配置

### 问题2: 连接被拒绝
```
Failed to load resource: net::ERR_CONNECTION_REFUSED
```
**解决方案**: 确保后端服务已启动

### 问题3: 404错误
```
GET http://localhost:8123/api/ai/manus/chat 404 (Not Found)
```
**解决方案**: 检查接口路径和RequestMapping配置

