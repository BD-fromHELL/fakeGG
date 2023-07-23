package com.BDFH.fakeGG.client;

import com.BDFH.fakeGG.dto.riotApi.LeagueEntry;
import com.BDFH.fakeGG.dto.riotApi.Summoner;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(url = "${app.riot-api.kr.url}", name = "kr-riot-api")
public interface KrRiotApiClient {

    @GetMapping("/lol/summoner/v4/summoners/by-name/{summonerName}")
    Summoner getSummonerByName(@PathVariable("summonerName") String summonerName,
                               @RequestParam("api_key") String apiKey);

    @GetMapping("/lol/league/v4/entries/by-summoner/{summonerId}")
    List<LeagueEntry> getSummonerEntry(@PathVariable("summonerId") String summonerId,
                                       @RequestParam("api_key") String apiKey);


}
