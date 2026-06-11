package com.ymh.ymhaiagent.tools;

import com.ymh.ymhaiagent.advisor.MyLoggerAdvisor;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserQueryToolTest {
    @Resource
    private ChatModel dashscopeChatModel;
    @Test
    void userQuery() {
        String aiReply = ChatClient.create(dashscopeChatModel)
                .prompt("id为1的用户是谁?")
                .advisors(new MyLoggerAdvisor())
                .tools(new UserQueryTool())
                .toolContext(Map.of("requestId", "123"))
                .call()
                .content();

        assertNotNull(aiReply);
    }
}
