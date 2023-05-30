package com.BDFH.fakeGG.service;

import com.BDFH.fakeGG.entity.Summoner;
import com.BDFH.fakeGG.model.SummonerModel;
import com.BDFH.fakeGG.repository.SummonerRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class RiotApiService {

    private final SummonerRepository summonerRepository;

    private final String apiKey = "RGAPI-916d0ecb-511d-43ce-81c9-26fb97e30b44";

    // 소환사 정보 불러오기
    public Summoner getSummoner(String summonerName) {

        try {
            System.out.println("흠??");
            URL url = new URL("https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + summonerName
                    + "?api_key=" + apiKey);


            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

            StringBuilder sb = new StringBuilder();
            System.out.println("connection!");
            String input = "";
            while ((input = br.readLine()) != null) {
                sb.append(input);
            }

            Gson gson = new Gson();

            System.out.println("Gson gson = new Gson();");

            SummonerModel summonerModel = gson.fromJson(sb.toString(), SummonerModel.class);
            System.out.println("summonerModel 낫널");
            if (summonerModel == null) {
                System.out.println("summonerModel 널널하네~");
                return null;

            }

            String tempName = summonerName.replace(" ", "").toLowerCase();


            Summoner summonerEntity = summonerRepository.findByName(tempName);

            Summoner summoner;

            //db에 있으면 찾아서 넣고 없으면 없으면 api에서 받아서 넣고
            if (summonerEntity == null) {
                System.out.println("summonerEntity == null");
                summoner = Summoner.builder()
                        .summonerId(summonerModel.getId())
                        .accountId(summonerModel.getAccountId())
                        .name(summonerModel.getName())
                        .profileIconId(summonerModel.getProfileIconId())
                        .puuid(summonerModel.getPuuid())
                        .summonerLevel(summonerModel.getSummonerLevel())
                        .revisionDate(summonerModel.getRevisionDate())
                        .build();

            } else {

                summoner = Summoner.builder()
                        .id(summonerEntity.getId())
                        .summonerId(summonerModel.getId())
                        .accountId(summonerModel.getAccountId())
                        .name(summonerModel.getName())
                        .profileIconId(summonerModel.getProfileIconId())
                        .puuid(summonerModel.getPuuid())
                        .summonerLevel(summonerModel.getSummonerLevel())
                        .revisionDate(summonerModel.getRevisionDate())
                        .build();

            }

            summonerRepository.save(summoner);
            return summoner;

        } catch (Exception e) {
            System.out.println("소환사 정보가 없는데요?");

        }

        return null;
    }

//    // 소환사의 랭크 정보 불러오기
//    public Summoner getEntriesBySummonerName(String summonerId, String apiKey) {
//        try {
//            URL url = new URL("/lol/league/v4/entries/by-summoner/" + summonerId
//                    + "?api_key=" + apiKey);
//
//
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//
//            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
//
//            StringBuilder sb = new StringBuilder();
//
//            String input = "";
//            while ((input = br.readLine()) != null) {
//                sb.append(input);
//            }
//
//            Gson gson = new Gson();
//
//            Type listType = new TypeToken<ArrayList<ApiEntry>>() {
//            }.getType();
//
//            List<ApiEntry> apiEntries = gson.fromJson(sb.toString(), listType);
//
//            if (apiEntries == null || apiEntries.size() == 0) {
//                return null;
//            }
//
//            for (ApiEntry apiEntry : apiEntries) {
//                String division = "1";
//                if (apiEntry.getRank().equals("I")) {
//                    division = "1";
//                } else if (apiEntry.getRank().equals("II")) {
//                    division = "2";
//                } else if (apiEntry.getRank().equals("III")) {
//                    division = "3";
//                } else if (apiEntry.getRank().equals("IV")) {
//                    division = "4";
//                }
//
//                EntryModel entryEntity = entryRepository.findBySummonerIdAndQueueType(apiEntry.getSummonerId(),
//                        apiEntry.getQueueType());
//
//                EntryModel entryModel = null;
//
//                if (entryEntity == null) {
//
//                    entryModel = EntryModel.builder().leagueId(apiEntry.getLeagueId())
//                            .leaguePoints(apiEntry.getLeaguePoints()).queueType(apiEntry.getQueueType())
//                            .summonerId(apiEntry.getSummonerId()).summonerName(apiEntry.getSummonerName())
//                            .rank(apiEntry.getRank()).tier(apiEntry.getTier()).wins(apiEntry.getWins())
//                            .losses(apiEntry.getLosses()).tierRankId(apiEntry.getTier().toLowerCase() + "_" + division)
//                            .build();
//                } else {
//
//                    entryModel = EntryModel.builder().id(entryEntity.getId()).leagueId(apiEntry.getLeagueId())
//                            .leaguePoints(apiEntry.getLeaguePoints()).queueType(apiEntry.getQueueType())
//                            .summonerId(apiEntry.getSummonerId()).summonerName(apiEntry.getSummonerName())
//                            .rank(apiEntry.getRank()).tier(apiEntry.getTier()).wins(apiEntry.getWins())
//                            .losses(apiEntry.getLosses()).tierRankId(apiEntry.getTier().toLowerCase() + "_" + division)
//                            .build();
//
//                }
//
//                entryRepository.save(entryModel);
//
//            }
//
//            return apiEntries;
//
//        } catch (Exception e) {
//            System.out.println("엥 ???? 소환사 랭크 정보를 불러오는 데에 실패했음!");
//        }
//
//        return null;
//    }
}
