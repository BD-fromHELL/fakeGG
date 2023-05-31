package com.BDFH.fakeGG.repository;

import com.BDFH.fakeGG.entity.Summoner;
import com.BDFH.fakeGG.model.SummonerModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public interface SummonerRepository extends JpaRepository<Summoner, Long> {

    Summoner findByName(String name);

}
