package com.BDFH.fakeGG.model.riotApi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchModel {
    private String gameType;
    private Long gameCreation;
    private Integer gameDuration;
    private Long gameEndTimestamp;
    private Boolean isTeamWin;
    private ParticipantModel hero;

    private List<TeamModel> teams;
    private List<ParticipantModel> participantsA;
    private List<ParticipantModel> participantsB;
}
