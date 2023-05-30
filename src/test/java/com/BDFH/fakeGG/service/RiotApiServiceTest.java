package com.BDFH.fakeGG.service;

import com.BDFH.fakeGG.entity.Summoner;
import com.BDFH.fakeGG.model.EntriesModel;
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

        assertNotNull(akaps);  // 객체가 null이 아닌지 확인
        assertEquals("Akaps", akaps.getName());  // "Akaps"인지 확인
    }

    @Test
    void 아캅스랭크정보() {
        Summoner akaps = riotApiService.getSummoner("Akaps");
        String summonerId = akaps.getSummonerId();
        System.out.println(summonerId);
        EntriesModel entries = riotApiService.getEntries(summonerId);
        System.out.println(entries);
        assertNotNull(entries);
    }

}