package com.BDFH.fakeGG.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class SummonerInfoResponseDto {
    private Long profileIconId;
    private Long summonerLevel;
    private String queueType;
    private String tier;
    private String rank;
    private String summonerName;
    private Integer leaguePoints;
    private Integer wins;
    private Integer losses;
    private Float winRate;
}
