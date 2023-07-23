package com.BDFH.fakeGG.dto.riotApi.summonerSpell;

import lombok.Getter;

import java.util.List;

@Getter
public class SummonerSpell {

    public String id;
    public String name;
    public String description;
    public String tooltip;
    public Integer maxrank;
    public List<Integer> cooldown;
    public String cooldownBurn;
    public List<Integer> cost;
    public String costBurn;
    public Datavalues datavalues;
    public List<Object> effect;
    public List<Object> effectBurn;
    public List<Object> vars;
    public Integer key;
    public Integer summonerLevel;
    public List<String> modes;
    public String costType;
    public String maxammo;
    public List<Integer> range;
    public String rangeBurn;
    public Image image;
    public String resource;

}
