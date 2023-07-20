package com.BDFH.fakeGG.controller;

import com.BDFH.fakeGG.model.riotApi.EntryModel;
import com.BDFH.fakeGG.model.riotApi.MatchModel;
import com.BDFH.fakeGG.model.riotApi.SummonerModel;
import com.BDFH.fakeGG.service.RiotApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/riotApi/")
@RequiredArgsConstructor
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class RiotApiController {

    private final RiotApiService riotApiService;


    @GetMapping("summoner/{summonerName}")
    public SummonerModel getSummoner(@PathVariable String summonerName) {
        return riotApiService.getSummoner(summonerName);
    }

    //     소환사 랭크 정보 불러오기
    @GetMapping("summoner/{summonerId}/info")
    public EntryModel getSummonerRank(@PathVariable String summonerId) {
        return riotApiService.getSummonerRank(summonerId);
    }

    //     소환사 전적 불러오기
    @GetMapping("summoner/{summonerName}/matches")
    public List<MatchModel> getSummonerMatches(@PathVariable String summonerName) {
        return riotApiService.getMatches(summonerName);
    }

    @GetMapping("match/{matchId}")
    public MatchModel getOneMatch(@PathVariable String matchId) {
        return riotApiService.getOneMatch(matchId);
    }
}
