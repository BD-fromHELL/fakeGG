package com.BDFH.fakeGG.dto.riotApi;

import lombok.Getter;

import java.util.List;

@Getter
public class Team {

    private List<Object> bans;
    private Objectives objectives;
    private Integer teamId;
    private Boolean win;
}
