package com.BDFH.fakeGG.controller;

import com.BDFH.fakeGG.dto.riotApi.Info;
import com.BDFH.fakeGG.dto.riotApi.LeagueEntry;
import com.BDFH.fakeGG.model.riotApi.EntryModel;
import com.BDFH.fakeGG.model.riotApi.MatchModel;
import com.BDFH.fakeGG.model.riotApi.SummonerModel;
import com.BDFH.fakeGG.service.RiotApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/riotApi/")
@RequiredArgsConstructor
public class RiotApiController {

    private final RiotApiService riotApiService;


    // 소환사 계정 찾기
    @GetMapping("summoner/{summonerName}")
    public SummonerModel getSummonerByName(@PathVariable String summonerName) {
        System.out.println("컨트롤러 시작..");
        return riotApiService.getSummoner(summonerName);
    }

    // 소환사 랭크 정보 불러오기
    @GetMapping("summoner/{summonerId}/info")
    public EntryModel getSummonerRank(@PathVariable String summonerId) {
        return riotApiService.getSummonerRank(summonerId);
    }

    // 소환사 전적 불러오기
    @GetMapping("summoner/{summonerName}/matches")
    public List<MatchModel> getSummonerMatches(@PathVariable String summonerName) {
        return null;
    }
}
