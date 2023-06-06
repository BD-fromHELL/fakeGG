package com.BDFH.fakeGG.dto.riotApi;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class Info {
    private Long gameCreation;
    private Integer gameDuration;
    private Long gameEndTimestamp;
    private Long gameId;
    private String gameMode;
    private String gameName;
    private Long gameStartTimestamp;
    private String gameType;
    private String gameVersion;
    private Integer mapId;
    private List<Participant> participants;
    private String platformId;
    private Integer queueId;
    private List<Team> teams;
    private String tournamentCode;
}
