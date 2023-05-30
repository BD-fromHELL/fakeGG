package com.BDFH.fakeGG.service;

import com.BDFH.fakeGG.entity.Summoner;
import com.BDFH.fakeGG.model.EntriesModel;
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
                System.out.println("summonerEntity !not! null");
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

    // 소환사의 랭크 정보 불러오기
    public EntriesModel getEntries(String summonerId) {
        try {
            URL url = new URL("https://kr.api.riotgames.com/lol/league/v4/entries/by-summoner/" + summonerId
                    + "?api_key=" + apiKey);


            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

            StringBuilder sb = new StringBuilder();
            String input = "";
            while ((input = br.readLine()) != null) {
                sb.append(input);
            }

            Gson gson = new Gson();
            EntriesModel[] arr = gson.fromJson(sb.toString(),EntriesModel[].class);

            EntriesModel entriesModel = arr[0];

            return entriesModel;

        } catch (Exception e) {
            System.out.println(e);
            System.out.println("엥 ???? 소환사 랭크 정보를 불러오는 데에 실패했음!");
        }

        return null;
    }

    // 소환환사의 전적 정보 불러오기

}
