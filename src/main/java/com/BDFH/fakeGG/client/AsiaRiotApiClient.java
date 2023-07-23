package com.BDFH.fakeGG.client;

import com.BDFH.fakeGG.dto.riotApi.Match;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "${app.riot-api.asia.url}", name = "asia-riot-api")
public interface AsiaRiotApiClient {

    @GetMapping("/lol/match/v5/matches/by-puuid/{summonerPuuid}/ids")
    String[] getMatchIdArray(@PathVariable("summonerPuuid") String summonerPuuid,
                             @RequestParam(value = "start", defaultValue = "0") int start,
                             @RequestParam(value = "count", defaultValue = "5") int count,
                             @RequestParam("api_key") String apiKey);

    @GetMapping("/lol/match/v5/matches/{matchId}")
    Match getMatch(@PathVariable("matchId") String matchId,
                   @RequestParam("api_key") String apiKey);

}
