package com.BDFH.fakeGG.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Summoner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "summonerId", unique = true)
    private String summonerId;

    @Column(name = "accountId",unique = true)
    private String accountId;

    @Column(name = "puuid",unique = true)
    private String puuid;

    @Column(name = "name")
    private String name;

    @Column(name = "profileIconId")
    private Long profileIconId;

    @Column(name = "revisionDate")
    private Long revisionDate;

    @Column(name = "summonerLevel")
    private Long summonerLevel;

}

