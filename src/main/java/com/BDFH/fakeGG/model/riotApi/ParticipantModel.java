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
    private String championName;
    private Integer championLevel;
    private String summonerName;
    private String rankTier;
    private Integer summonerLevel;  // 티어가 없으면 레벨로
    private String summoner1; //  스펠에 id값이 아닌 url에 적용하기 쉬운 id에 맞는 특정한 string 값으로 return
    private String summoner2;
    private String rune1;  //  룬도 마찬가지로 특정한 string 값으로 return
    private String rune2;
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
    private Integer teamId;
    private Integer item0;
    private Integer item1;
    private Integer item2;
    private Integer item3;
    private Integer item4;
    private Integer item5;
    private Integer item6;
}
