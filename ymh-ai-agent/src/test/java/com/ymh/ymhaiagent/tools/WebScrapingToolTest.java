package com.ymh.ymhaiagent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class WebScrapingToolTest {

    @Test
    void scrapeWebPageTest(){
        WebScrapingTool webScrapingTool = new WebScrapingTool();
        String result = webScrapingTool.scrapeWebPage("https://www.codefather.cn");
        System.out.println("result= "+result);
        assertNotNull(result);
    }
}
