package com.BDFH.fakeGG.service;

import com.BDFH.fakeGG.dto.riotApi.rune.Root;
import com.BDFH.fakeGG.dto.riotApi.rune.Rune;
import com.BDFH.fakeGG.dto.riotApi.rune.Slot;
import com.BDFH.fakeGG.model.riotApi.EntryModel;
import com.BDFH.fakeGG.model.riotApi.MatchModel;
import com.BDFH.fakeGG.model.riotApi.SummonerModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class RiotApiServiceTest {

    @Autowired
    RiotApiService riotApiService;


    @Test
    void 스펠아이콘받기(){
        String result = riotApiService.getSpellIcon(3)    ;

        System.out.println(result);
    }
    @Test
    void 룬정보걸러내기() throws IOException {

        URL url = new URL("https://ddragon.leagueoflegends.com/cdn/10.6.1/data/en_US/runesReforged.json");

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String input = "";
        while ((input = br.readLine()) != null) {
            sb.append(input);
        }

        Gson gson = new Gson();

        List<Root> roots = gson.fromJson(sb.toString(), new TypeToken<List<Root>>() {}.getType());


        for (Root root : roots) {
            System.out.println(root.getName());
            List<Slot> slots = root.getSlots();
            for (Slot slot : slots) {
                List<Rune> runes = slot.getRunes();
                for (Rune rune : runes) {
                    System.out.println(rune.getIcon());
                    System.out.println(rune.getName());
                }

            }

        }

//
//        if (summonerDto == null) {
//            System.out.println("summonerModel 널널하네~");
//            return null;
//        }
//        System.out.println("여긴가?");
//        SummonerModel summoner = SummonerModel.builder()
//                .id(summonerDto.getId())
//                .accountId(summonerDto.getAccountId())
//                .puuid(summonerDto.getPuuid())
//                .name(summonerDto.getName())
//                .profileIconId(summonerDto.getProfileIconId())
//                .summonerLevel(summonerDto.getSummonerLevel())
//                .build();
//        System.out.println(summonerName + " 소환사 정보 찾아냈다~~");
//        return summoner;


    }

    @Test
    void 아캅스불러오기() {
        SummonerModel akaps = riotApiService.getSummoner("Akaps");
        System.out.println(akaps);

        assertNotNull(akaps);  // 객체가 null이 아닌지 확인
        assertEquals("Akaps", akaps.getName());  // "Akaps"인지 확인
    }

    @Test
    void 아캅스랭크정보() {
        EntryModel entry = riotApiService.getSummonerRank("Akaps");
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