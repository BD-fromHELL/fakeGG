package com.BDFH.fakeGG.controller;

import com.BDFH.fakeGG.entity.Summoner;
import com.BDFH.fakeGG.model.EntriesModel;
import com.BDFH.fakeGG.service.RiotApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/riotApi/")
@RequiredArgsConstructor
public class RiotApiController {

    private final RiotApiService riotApiService;


    @GetMapping("summoner/{summonerName}")
    public Summoner getSummonerByName(@PathVariable String summonerName) {
        System.out.println("컨트롤러 시작..");
        return riotApiService.getSummoner(summonerName);
    }

    @GetMapping("summoner/dd/{summonerId}")
    public EntriesModel getSummonerRank(@PathVariable String summonerId) {
        return riotApiService.getEntries(summonerId);
    }
}
