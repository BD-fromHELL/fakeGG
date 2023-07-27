package com.BDFH.fakeGG.service;

import com.BDFH.fakeGG.client.AsiaRiotApiClient;
import com.BDFH.fakeGG.client.DragonUrlClient;
import com.BDFH.fakeGG.client.KrRiotApiClient;
import com.BDFH.fakeGG.dto.SummonerInfoResponseDto;
import com.BDFH.fakeGG.dto.riotApi.*;
import com.BDFH.fakeGG.dto.riotApi.rune.Root;
import com.BDFH.fakeGG.dto.riotApi.rune.Rune;
import com.BDFH.fakeGG.dto.riotApi.rune.Slot;
import com.BDFH.fakeGG.dto.riotApi.summonerSpell.Data;
import com.BDFH.fakeGG.dto.riotApi.summonerSpell.Spell;
import com.BDFH.fakeGG.dto.riotApi.summonerSpell.SummonerSpell;
import com.BDFH.fakeGG.model.riotApi.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RiotApiService {

    private final KrRiotApiClient krRiotApiClient;
    private final AsiaRiotApiClient asiaRiotApiClient;
    private final DragonUrlClient dragonUrlClient;

    @Value("${app.riot-api.api-key}")
    private String apiKey;

    public String getRuneIcon(Integer rootId, Integer detailId) {


            List<Root> roots = dragonUrlClient.getRuneIcon();
            String icon = new String();
            for (Root root : roots) {
                if (root.getId() == rootId) {
                    if(detailId== null) {
                        return root.getIcon();
                    }
                    List<Slot> slots = root.getSlots();
                    for (Slot slot : slots) {
                        List<Rune> runes = slot.getRunes();
                        for (Rune rune : runes) {
                            if (rune.getId() == detailId) {
                                return rune.getIcon();
                            }
                        }
                    }
                }
            }
        return null;
    }
    public String getSpellIcon(Integer spellId) {

        Spell spell = dragonUrlClient.getSpellIcon();
        Data data = spell.getData();

        for (Field field : Data.class.getDeclaredFields()) {
            field.setAccessible(true);
            SummonerSpell summonerSpell = null;
            try {
                summonerSpell = (SummonerSpell) field.get(data);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            System.out.println(summonerSpell.getKey());
            if (summonerSpell.getKey().equals(spellId)) {
                System.out.println(summonerSpell.getImage().getFull());
                return summonerSpell.getImage().getFull();

            }
        }
        return null;
    }

    // 소환사 정보 불러오기
    public SummonerModel getSummoner(String summonerName) {

        System.out.println("전적불러오는중..");
        Summoner summonerDto = krRiotApiClient.getSummonerByName(summonerName, apiKey);
        // 해당 소환사가 없을 경우 404로 내려주기 때문에 예외 처리 해야됨.
        // catch로 모든 예외를 처리하면 안됨. 404는
        // 기존 DB 적재 및 관련 코드 삭제 -> 로컬 캐시로 처리
        SummonerModel summoner = SummonerModel.builder()
                .id(summonerDto.getId())
                .accountId(summonerDto.getAccountId())
                .puuid(summonerDto.getPuuid())
                .name(summonerDto.getName())
                .profileIconId(summonerDto.getProfileIconId())
                .summonerLevel(summonerDto.getSummonerLevel())
                .build();
        System.out.println("전적불러오기 완료!!");
        return summoner;

    }

    // 소환사의 랭크 정보 불러오기
    public EntryModel getSummonerRank(String summonerId) {
        System.out.println("랭크불러오는중..");

        List<LeagueEntry> entryDtoList = krRiotApiClient.getSummonerEntry(summonerId, apiKey);
        if (entryDtoList.isEmpty()) {
            return null;
        }
        LeagueEntry entryDto = entryDtoList.get(0);

        EntryModel entry = EntryModel.builder()
                .summonerName(entryDto.getSummonerName())
                .leaguePoints(entryDto.getLeaguePoints())
                .queueType(entryDto.getQueueType())
                .rank(entryDto.getRank())
                .tier(entryDto.getTier())
                .wins(entryDto.getWins())
                .losses(entryDto.getLosses())
                .build();

        System.out.println("랭크불러오기 완료!!");
        return entry;
    }

    // 소환사의 전적 matchId List 불러오기
    public String[] getMatchIdArray(String summonerPuuid) {
        System.out.println("매치리스트 불러오는중..");
        String[] matchIdArray = asiaRiotApiClient.getMatchIdArray(summonerPuuid, 0, 5, apiKey);

        System.out.println("매치리스트 불러오기 완료!!");
        return matchIdArray;
    }

    public MatchModel getOneMatch(String summonerName, String matchId) {
        Match matchDto = asiaRiotApiClient.getMatch(matchId, apiKey);

        Info infoDto = matchDto.getInfo();
        if (!infoDto.getGameMode().equals("CLASSIC")) {
            return null;
        }

        List<TeamModel> teams = new ArrayList<>();
        Integer winTeamId = null;
        for (Team teamDto : infoDto.getTeams()) {
            Objectives objectives = teamDto.getObjectives();
            if (teamDto.getWin()) {
                winTeamId = teamDto.getTeamId();
            }
            TeamModel team = TeamModel.builder()
                    .teamId(teamDto.getTeamId())
                    .win(teamDto.getWin())
                    .baronKills(objectives.getBaron().getKills())
                    .dragonKills(objectives.getDragon().getKills())
                    .inhibitorKills(objectives.getInhibitor().getKills())
                    .towerKills(objectives.getTower().getKills())
                    .totalGolds(0)
                    .totalKills(objectives.getChampion().getKills())
                    .build();

            teams.add(team);
        }
        System.out.println("Team정보 입력");
        List<ParticipantModel> participantsA = new ArrayList<>();
        List<ParticipantModel> participantsB = new ArrayList<>();

        Boolean isTeamWin = false;
        ParticipantModel hero = null;
        int i = 0;
        for (Participant participantDto : infoDto.getParticipants()) {
            EntryModel entry = getSummonerRank(participantDto.getSummonerId());
            String rankTier;
            if (entry == null) {
                rankTier = "unranked";
            } else {
                rankTier = entry.getRank() + entry.getTier();
            }


            if (summonerName.replace(" ","").equalsIgnoreCase(participantDto.getSummonerName().replace(" ", ""))) {
                if (winTeamId.equals(participantDto.getTeamId())) {
                    isTeamWin = true;
                } else {
                    isTeamWin = false;
                }
            }

            Integer rootId1 = participantDto.getPerks().getStyles().get(0).getStyle();
            Integer detailId1 = participantDto.getPerks().getStyles().get(0).getSelections().get(0).getPerk();
            Integer rootId2 = participantDto.getPerks().getStyles().get(1).getStyle();

            String rune1 = getRuneIcon(rootId1, detailId1);
            String rune2 = getRuneIcon(rootId2, null);
            String summoner1 = getSpellIcon(participantDto.getSummoner1Id());
            System.out.println(summoner1);
            String summoner2 = getSpellIcon(participantDto.getSummoner2Id());
            System.out.println(summoner2);
            ParticipantModel participant = ParticipantModel.builder()
                    .summonerName(participantDto.getSummonerName())
                    .summonerLevel(participantDto.getSummonerLevel())
                    .rankTier(rankTier)
                    .summoner1(summoner1)
                    .summoner2(summoner2)
                    .rune1(rune1)
                    .rune2(rune2)
                    .kill(participantDto.getKills())
                    .death(participantDto.getDeaths())
                    .assist(participantDto.getAssists())
                    .kda(participantDto.getChallenges().getKda())
                    .championName(participantDto.getChampionName())
                    .championLevel(participantDto.getChampLevel())
                    .teamId(participantDto.getTeamId())
                    .item0(participantDto.getItem0())
                    .item1(participantDto.getItem1())
                    .item2(participantDto.getItem2())
                    .item3(participantDto.getItem3())
                    .item4(participantDto.getItem4())
                    .item5(participantDto.getItem5())
                    .item6(participantDto.getItem6())
                    .cs(participantDto.getTotalMinionsKilled() + participantDto.getNeutralMinionsKilled())
                    .damageDealt(participantDto.getTotalDamageDealtToChampions())
                    .damageTaken(participantDto.getTotalDamageTaken())
                    .csPerMinute((float) ((participantDto.getTotalMinionsKilled() + participantDto.getNeutralMinionsKilled()) / (infoDto.getGameDuration() / 60.0)))
                    .wardsPlaced(participantDto.getWardsPlaced())
                    .wardsKilled(participantDto.getWardsKilled())
                    .detectorWardsPlaced(participantDto.getDetectorWardsPlaced())
                    .killParticipation(Math.round(participantDto.getChallenges().getKillParticipation()))
                    .build();
            if(i<5){
                participantsA.add(participant);
            }
            else{
                participantsB.add(participant);
            }

            if (summonerName.replace(" ","").equalsIgnoreCase(participantDto.getSummonerName().replace(" ", ""))) {
                hero = participant;
            }
            i++;
        }

        System.out.println("참가자들 정보 입력 완료");

        MatchModel matchModel = MatchModel.builder().gameType(infoDto.getGameType())
                .isTeamWin(isTeamWin)
                .hero(hero)
                .gameCreation(infoDto.getGameCreation())
                .gameDuration(infoDto.getGameDuration())
                .gameEndTimestamp(infoDto.getGameEndTimestamp())
                .teams(teams).participantsA(participantsA)
                .participantsB(participantsB)
                .build();

        return matchModel;
    }

    // 소환환사의 전적 정보 불러오기
    public List<MatchModel> getMatches(String summonerName) {
        System.out.println("전적검색중...");
        int i = 1;
        SummonerModel summoner = getSummoner(summonerName);

        String[] matchIdArray = getMatchIdArray(summoner.getPuuid());
        List<MatchModel> matches = new ArrayList<>();

        for (String matchId : matchIdArray) {
            System.out.println(i++);

            MatchModel match = getOneMatch(summonerName, matchId);

            if (match != null) {
                matches.add(match);
            }
        }
        System.out.println("전적 검색 완료!!");
        return matches;
    }

    // 소환사의 info 불러오기
    public SummonerInfoResponseDto getSummonerInfo(String summonerName) {
        SummonerModel summoner = getSummoner(summonerName);
        EntryModel entryModel = getSummonerRank(summoner.getId());
        Float winRate = Float.valueOf(100 * entryModel.getWins() / (entryModel.getWins() + entryModel.getLosses()));
        SummonerInfoResponseDto summonerInfo = SummonerInfoResponseDto.builder()
                .profileIconId(summoner.getProfileIconId())
                .summonerLevel(summoner.getSummonerLevel())
                .queueType(entryModel.getQueueType())
                .tier(entryModel.getTier())
                .rank(entryModel.getTier())
                .summonerName(entryModel.getSummonerName())
                .leaguePoints(entryModel.getLeaguePoints())
                .wins(entryModel.getWins())
                .losses(entryModel.getLosses())
                .winRate(winRate)
                .build();
        return summonerInfo;
    }
}
