package com.BDFH.fakeGG.service;

import com.BDFH.fakeGG.dto.riotApi.*;
import com.BDFH.fakeGG.model.riotApi.*;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RiotApiService {

    private final String apiKey = "RGAPI-07494577-c54f-4fe2-b152-520ba5638ab8";


    ////HttpURLConnection 대신 HttpClient 클래스 사용해보기??

    // 소환사 정보 불러오기
    public SummonerModel getSummoner(String summonerName) {

        try {
            System.out.println("흠??"+summonerName);
            URL url = new URL("https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + summonerName
                    + "?api_key=" + apiKey);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String input = "";
            while ((input = br.readLine()) != null) {
                sb.append(input);
            }
            Gson gson = new Gson();
            System.out.println(sb.toString());

            Summoner summonerDto = gson.fromJson(sb.toString(), Summoner.class);
            if (summonerDto == null) {
                System.out.println("summonerModel 널널하네~");
                return null;
            }
            System.out.println("여긴가?");
            SummonerModel summoner = SummonerModel.builder()
                    .id(summonerDto.getId())
                    .accountId(summonerDto.getAccountId())
                    .puuid(summonerDto.getPuuid())
                    .name(summonerDto.getName())
                    .profileIconId(summonerDto.getProfileIconId())
                    .summonerLevel(summonerDto.getSummonerLevel())
                    .build();
            System.out.println(summonerName+" 소환사 정보 찾아냈다~~");
            return summoner;

        } catch (Exception e) {
            System.out.println("소환사 정보가 없는데요?");

        }

        return null;
    }

    // 소환사의 랭크 정보 불러오기
    public EntryModel getSummonerRank(String summonerName) {
        try {
            SummonerModel summoner = getSummoner(summonerName);

            URL url = new URL("https://kr.api.riotgames.com/lol/league/v4/entries/by-summoner/" + summoner.getId()
                    + "?api_key=" + apiKey);


            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

            StringBuilder sb = new StringBuilder();
            String input = "";
            while ((input = br.readLine()) != null) {
                sb.append(input);
            }

            Gson gson = new Gson();
            LeagueEntry entryDto = gson.fromJson(sb.toString(), LeagueEntry[].class)[0];

            if (entryDto == null) {
                System.out.println("entry가 널널하네~");
                return null;
            }

            EntryModel entry = EntryModel.builder()
                    .summonerName(entryDto.getSummonerName())
                    .leaguePoints(entryDto.getLeaguePoints())
                    .queueType(entryDto.getQueueType())
                    .rank(entryDto.getRank())
                    .tier(entryDto.getTier())
                    .wins(entryDto.getWins())
                    .losses(entryDto.getLosses())
                    .build();

            return entry;

        } catch (Exception e) {
            System.out.println(e);
            System.out.println("엥 ???? 소환사 랭크 정보를 불러오는 데에 실패했음!");
        }

        return null;
    }

    // 소환사의 전적 matchId List 불러오기
    public String[] getMatchIdArray(String summonerPuuid) {
        try {
            URL url = new URL("https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/" + summonerPuuid
                    + "/ids?api_key=" + apiKey);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

            StringBuilder sb = new StringBuilder();
            String input = "";
            while ((input = br.readLine()) != null) {
                sb.append(input);
            }

            Gson gson = new Gson();

            String[] matchIdArray = gson.fromJson(sb.toString(), String[].class);

            return matchIdArray;
        } catch (Exception e) {
            System.out.println("전적이 안구해지는데요??");
        }

        return null;
    }

    // 소환환사의 전적 정보 불러오기
    public List<MatchModel> getMatches(String summonerName) {
        System.out.println("전적검색중...");
        SummonerModel summoner = getSummoner(summonerName);

        String[] matchIdArray = getMatchIdArray(summoner.getPuuid());
        List<MatchModel> matches = new ArrayList<>();

        for (String matchId : matchIdArray) {
            try {

                URL url = new URL("https://asia.api.riotgames.com/lol/match/v5/matches/" + matchId
                        + "?api_key=" + apiKey);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

                StringBuilder sb = new StringBuilder();
                String input = "";
                while ((input = br.readLine()) != null) {
                    sb.append(input);
                }

                Gson gson = new Gson();
                System.out.println(sb.toString());
                Info infoDto = new Info();

                try {
                    Match matchDto= gson.fromJson(sb.toString(), Match.class);
                    infoDto = matchDto.getInfo();
                }catch (JsonParseException e) {
                    e.printStackTrace();
                }


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

                    ParticipantModel participant = ParticipantModel.builder()
                            .summonerName(participantDto.getSummonerName())
                            .summonerLevel(getSummoner(participantDto.getSummonerName()).getSummonerLevel())
                            .rankTier(getSummonerRank(participantDto.getSummonerName()).getRank() + getSummonerRank(participantDto.getSummonerName()).getTier())
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
                            .피해량(participantDto.getTotalDamageDealtToChampions())
                            .받은피해량(participantDto.getTotalDamageTaken())
                            .분당cs((float) ((participantDto.getTotalMinionsKilled() + participantDto.getNeutralMinionsKilled()) / (infoDto.getGameDuration() / 60.0)))
                            .와드설치(participantDto.getWardsPlaced())
                            .와드제거(participantDto.getWardsKilled())
                            .제어와드(participantDto.getDetectorWardsPlaced())
                            .킬관여(Math.round(participantDto.getChallenges().getKillParticipation()))
                            .build();
                    participants.add(participant);
                }

                System.out.println("참가자들 정보 입력 완료");

                MatchModel matchModel = MatchModel.builder().gameType(infoDto.getGameType())
                        .gameCreation(infoDto.getGameCreation())
                        .gameDuration(infoDto.getGameDuration())
                        .gameEndTimestamp(infoDto.getGameEndTimestamp())
                        .teams(teams).participants(participants).build();

                matches.add(matchModel);
            } catch (Exception e) {
                System.out.println("흠.. 전적이 안 찾아 지는데??");
                e.printStackTrace();
            }

        }
        return matches;
    }

}
