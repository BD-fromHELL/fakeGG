package com.BDFH.fakeGG.model.riotApi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SummonerModel {
    private String accountId;
    private Long profileIconId;
    private String name;
    private String id;
    private String puuid;
    private Long summonerLevel;
}
