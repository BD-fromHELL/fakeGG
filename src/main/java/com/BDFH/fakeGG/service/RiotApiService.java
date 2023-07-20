package com.BDFH.fakeGG.service;

import com.BDFH.fakeGG.client.AsiaRiotApiClient;
import com.BDFH.fakeGG.client.KrRiotApiClient;
import com.BDFH.fakeGG.dto.riotApi.*;
import com.BDFH.fakeGG.model.riotApi.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RiotApiService {

    private final KrRiotApiClient krRiotApiClient;
    private final AsiaRiotApiClient asiaRiotApiClient;

    @Value("${app.riot-api.api-key}")
    private String apiKey;

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
        System.out.println(entryDtoList);
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
        String[] matchIdArray = asiaRiotApiClient.getMatchIdArray(summonerPuuid, 0, 20, apiKey);

        System.out.println("매치리스트 불러오기 완료!!");
        return matchIdArray;
    }

    public MatchModel getOneMatch(String matchId) {
        Match matchDto = asiaRiotApiClient.getMatch(matchId, apiKey);

        Info infoDto = matchDto.getInfo();


        List<TeamModel> teams = new ArrayList<>();
        System.out.println(infoDto.getTeams());

        for (Team teamDto : infoDto.getTeams()) {
            Objectives objectives = teamDto.getObjectives();
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
        List<ParticipantModel> participants = new ArrayList<>();

        for (Participant participantDto : infoDto.getParticipants()) {
            EntryModel entry = getSummonerRank(participantDto.getSummonerId());
            String rankTier = entry.getRank()+entry.getTier();

            ParticipantModel participant = ParticipantModel.builder()
                    .summonerName(participantDto.getSummonerName())
                    .summonerLevel(participantDto.getSummonerLevel())
                    .rankTier(rankTier)
                    .summoner1Id(participantDto.getSummoner1Id())
                    .summoner2Id(participantDto.getSummoner2Id())
                    .rune1(participantDto.getPerks().getStyles().get(0).getStyle())
                    .rune2(participantDto.getPerks().getStyles().get(1).getStyle())
                    .kill(participantDto.getKills())
                    .death(participantDto.getDeaths())
                    .assist(participantDto.getAssists())
                    .kda(participantDto.getChallenges().getKda())
                    .championId(participantDto.getChampionId())
                    .championLevel(participantDto.getChampLevel())
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
            participants.add(participant);
        }

        System.out.println("참가자들 정보 입력 완료");

        MatchModel matchModel = MatchModel.builder().gameType(infoDto.getGameType())
                .gameCreation(infoDto.getGameCreation())
                .gameDuration(infoDto.getGameDuration())
                .gameEndTimestamp(infoDto.getGameEndTimestamp())
                .teams(teams).participants(participants).build();

        return matchModel;
    }

    // 소환환사의 전적 정보 불러오기
    public List<MatchModel> getMatches(String summonerName) {
        System.out.println("전적검색중...");
        SummonerModel summoner = getSummoner(summonerName);

        String[] matchIdArray = getMatchIdArray(summoner.getPuuid());
        List<MatchModel> matches = new ArrayList<>();

        for (String matchId : matchIdArray) {
            MatchModel match = getOneMatch(matchId);

            matches.add(match);
        }
        System.out.println("전적 검색 완료!!");
        return matches;
    }

}
