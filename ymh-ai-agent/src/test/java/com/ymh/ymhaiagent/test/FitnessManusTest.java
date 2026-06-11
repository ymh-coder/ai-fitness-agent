package com.ymh.ymhaiagent.test;

import com.ymh.ymhaiagent.agent.FitnessManus;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class FitnessManusTest {
    @Resource
    private FitnessManus fitnessManus;

    @Test
    void chat(){
        String userPrompt = """  
                我的好兄弟居住在深圳宝安区，请帮我找到 5 公里内合适的健身地点
                """;
        String answer = fitnessManus.run(userPrompt);
        assertNotNull(answer);
    }
}
