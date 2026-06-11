package com.ymh.ymhaiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
public class TerminalOperationToolTest {
//    @Test
    void executeTerminalCommand() {
        String result = new TerminalOperationTool().executeTerminalCommand("echo Hello World");
        System.out.println("result = " + result);
        Assertions.assertNotNull(result);
    }
}
