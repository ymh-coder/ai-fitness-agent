package com.ymh.ymhaiagent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FileOperationToolTest {
    @Test
    void writeFile(){
        String result = new FileOperationTool().writeFile(
                "编程导航.txt",
                "https://www.codefather.cn 程序员编程学习交流社区"
        );
        System.out.println(result);
        assertNotNull(result);
    }

    @Test
    void readFile(){
        String result = new FileOperationTool().readFile("编程导航.txt");
        assertNotNull(result);
        System.out.println(result);
    }
}
