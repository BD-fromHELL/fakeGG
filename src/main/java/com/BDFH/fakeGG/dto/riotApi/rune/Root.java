package com.BDFH.fakeGG.dto.riotApi.rune;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Root {
    public int id;
    public String key;
    public String icon;
    public String name;
    public ArrayList<Slot> slots;
}
