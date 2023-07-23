package com.BDFH.fakeGG.controller;

import com.BDFH.fakeGG.dto.SummonerInfoResponseDto;
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


    @GetMapping("summoner/{summonerName}")
    public SummonerModel getSummoner(@PathVariable String summonerName) {
        return riotApiService.getSummoner(summonerName);
    }

    //     소환사 랭크 정보 불러오기
    @GetMapping("summoner/{summonerName}/info")
    public SummonerInfoResponseDto getSummonerInfo(@PathVariable String summonerName) {
        return riotApiService.getSummonerInfo(summonerName);
    }

    //     소환사 전적 불러오기
    @GetMapping("summoner/{summonerName}/matches")
    public List<MatchModel> getSummonerMatches(@PathVariable String summonerName) {
        return riotApiService.getMatches(summonerName);
    }

}
