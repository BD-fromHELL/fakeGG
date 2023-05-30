package com.BDFH.fakeGG.service;

import com.BDFH.fakeGG.entity.Summoner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class RiotApiServiceTest {

    @Autowired
    RiotApiService riotApiService;


    @Test
    void 아캅스불러오기() {
        Summoner akaps = riotApiService.getSummoner("Akaps");
        System.out.println(akaps);

        assertNotNull(akaps);  // rankInfo 객체가 null이 아닌지 확인
        assertEquals("Akaps", akaps.getName());  // rankInfo의 name 필드가 "Akaps"인지 확인
    }

    @Test
    void 아캅스랭크정보() {
        System.out.println("테스트 잘되는가");
    }
}