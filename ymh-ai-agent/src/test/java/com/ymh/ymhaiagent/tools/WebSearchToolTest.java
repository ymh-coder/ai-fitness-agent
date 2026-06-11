package com.ymh.ymhaiagent.tools;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class WebSearchToolTest {

    @Value("${search-api.api-key}")
    private String apiKey;

    @Test
    void searchWeb(){
        String result = new WebSearchTool(apiKey).searchWeb("编程导航");
        System.out.println(result);
        assertNotNull(result);
    }
}
