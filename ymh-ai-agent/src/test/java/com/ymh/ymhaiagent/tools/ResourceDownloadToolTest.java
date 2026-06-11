package com.ymh.ymhaiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ResourceDownloadToolTest {

    @Test
    public void testDownLoadResource() {
        String result = new ResourceDownloadTool().downloadResource(
                "https://pic.code-nav.cn/jianqiezhushou/advertisement/1752524663997030402/zx3aIzSx-jianqiezhushou_advertisement.png", "jinqiezhushou.png"
        );
        System.out.println("result = " + result);
        Assertions.assertNotNull(result);
    }
}
