package com.BDFH.fakeGG.dto.riotApi;

import lombok.Getter;

import java.util.List;

@Getter
public class Metadata {
    private String dataVersion;
    private String matchId;
    private List<String> participants;
}
