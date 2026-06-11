package com.ymh.ymhaiagent.tools;

import com.ymh.ymhaiagent.advisor.MyLoggerAdvisor;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class WeatherToolTest {

    @Resource
    private ChatModel dashscopeChatModel;

    @Test
    void getWeather1(){
        ChatClient chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultAdvisors(
                        new MyLoggerAdvisor()
                )
                .defaultTools(
                        new WeatherTool()
                )
                .build();
        String aiReply=chatClient.prompt("南京今天天气怎么样")
                .tools(new WeatherTool())
                .call()
                .content();
        assertNotNull(aiReply);
    }

    @Test
    void getWeather2(){
        // 在一次对话中提供工具
        String aiReply = ChatClient.create(dashscopeChatModel)
                .prompt("南京今天天气怎么样")
                .advisors(new MyLoggerAdvisor())
                .tools(new WeatherTool())
                .call()
                .content();
        assertNotNull(aiReply);
    }

    @Test
    void getWeather3(){
        ToolCallingChatOptions toolCallingChatOptions = ToolCallingChatOptions.builder()
                .toolCallbacks(
                        ToolCallbacks.from(new WeatherTool())
                )
                .build();

        String aiReply = ChatClient.create(dashscopeChatModel)
                .prompt(new Prompt("南京今天天气怎么样,如果你不知道答案，或者需要最新信息，请调用 weatherTool 工具来获取南京的天气。", toolCallingChatOptions))
                .advisors(new MyLoggerAdvisor())
                .tools(new WeatherTool())
                .call()
                .content();
        assertNotNull(aiReply);
    }



}
