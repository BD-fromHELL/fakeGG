package com.BDFH.fakeGG.client;


import com.BDFH.fakeGG.FakeGgApplication;
import com.BDFH.fakeGG.dto.riotApi.Summoner;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = {FakeGgApplication.class})
public class RiotApiClientTest {

    @Value("${app.riot-api.api-key}")
    String apiKey;

//    @Test
//    void getSummonerByNameTest() {
//        // given
//        String summonerName = "괴물쥐";
//
//        // when
//        Summoner summoner = riotApiClient.getSummonerByName(summonerName, apiKey);
//
//        // then
//        Assertions.assertThat(summonerName).isEqualTo(summoner.getName());
//    }
}
