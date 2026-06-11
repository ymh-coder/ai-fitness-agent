package com.ymh.ymhaiagent.controller;

import com.ymh.ymhaiagent.agent.FitnessManus;
import com.ymh.ymhaiagent.app.FitnessApp;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
public class AiController {
    @Resource
    private FitnessApp fitnessApp;

    @Resource
    private ToolCallback[] allTools;

    @Resource
    private ChatModel dashscopeChatModel;

    @GetMapping("fitness_app/chat/sync")
    public String doChatWithFitnessAppSync(String message, String chatId) {
        return fitnessApp.doChat(message, chatId);
    }

    @GetMapping(value = "/fitness_app/chat/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithFitnessAppSSE(String message, String chatId) {
        return fitnessApp.doChatByStream(message, chatId);
    }

    /**
     * 流式调用 Manus 超级智能体
     *
     * @param message
     * @return
     */
    @GetMapping("/manus/chat")
    public SseEmitter doChatWithManus(String message) {
        FitnessManus fitnessManus = new FitnessManus(allTools, dashscopeChatModel);
        return fitnessManus.runStream(message);
    }
}
