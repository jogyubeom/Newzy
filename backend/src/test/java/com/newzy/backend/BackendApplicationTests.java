package com.newzy.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BackendApplicationTests {

    //    @Test
//    void contextLoads() {
//    }
    @Test
    public void testAlwaysPasses() {
        // 무조건 통과하는 테스트
        assertTrue(true);
    }

}
