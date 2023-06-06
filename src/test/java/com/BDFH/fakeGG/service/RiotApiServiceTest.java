package com.BDFH.fakeGG.service;

import com.BDFH.fakeGG.dto.riotApi.LeagueEntry;
import com.BDFH.fakeGG.model.riotApi.EntryModel;
import com.BDFH.fakeGG.model.riotApi.MatchModel;
import com.BDFH.fakeGG.model.riotApi.SummonerModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class RiotApiServiceTest {

    @Autowired
    RiotApiService riotApiService;


    @Test
    void 아캅스불러오기() {
        SummonerModel akaps = riotApiService.getSummoner("Akaps");
        System.out.println(akaps);

        assertNotNull(akaps);  // 객체가 null이 아닌지 확인
        assertEquals("Akaps", akaps.getName());  // "Akaps"인지 확인
    }

    @Test
    void 아캅스랭크정보() {
        EntryModel entry= riotApiService.getSummonerRank("Akaps");
        System.out.println(entry);
        assertNotNull(entry);
    }

    @Test
    void 아캅스전적찾아보기() {
        System.out.println("시작!!");
        List<MatchModel> matches = riotApiService.getMatches("Akaps");
        System.out.println("끝남....");
        for (MatchModel match : matches) {
            System.out.println(match);
        }

    }
}