package com.BDFH.fakeGG.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntriesModel {
    public String leagueId;
    public String queueType;
    public String tier;
    public String rank;
    public String summonerId;
    public String summonerName;
    public Integer leaguePoints;
    public Integer wins;
    public Integer losses;
    public Boolean veteran;
    public Boolean inactive;
    public Boolean freshBlood;
    public Boolean hotStreak;

}
