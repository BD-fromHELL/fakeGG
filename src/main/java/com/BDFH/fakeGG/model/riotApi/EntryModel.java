package com.BDFH.fakeGG.model.riotApi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntryModel {
    private String queueType;
    private String tier;
    private String rank;
    private String summonerName;
    private Integer leaguePoints;
    private Integer wins;
    private Integer losses;

}
