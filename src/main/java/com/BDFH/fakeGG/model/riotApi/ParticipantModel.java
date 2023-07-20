package com.BDFH.fakeGG.model.riotApi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipantModel {
    private Integer championId;
    private Integer championLevel;
    private String summonerName;
    private String rankTier;
    private Integer summonerLevel;  // 티어가 없으면 레벨로
    private Integer summoner1Id;
    private Integer summoner2Id;
    private Integer rune1;
    private Integer rune2;
    private Integer kill;
    private Integer death;
    private Integer assist;
    private Float kda;
    private Integer killParticipation;
    private Integer damageDealt;
    private Integer damageTaken;
    private Integer detectorWardsPlaced;
    private Integer wardsPlaced;
    private Integer wardsKilled;
    private Integer cs;
    private Float csPerMinute;
    private Integer item0;
    private Integer item1;
    private Integer item2;
    private Integer item3;
    private Integer item4;
    private Integer item5;
    private Integer item6;
}
