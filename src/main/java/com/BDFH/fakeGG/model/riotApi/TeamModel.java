package com.BDFH.fakeGG.model.riotApi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamModel {
    private long teamId;
    private Boolean win;
    private long towerKills;
    private long inhibitorKills;
    private long baronKills;
    private long dragonKills;
    private long totalKills;
    private long totalGolds;
}
