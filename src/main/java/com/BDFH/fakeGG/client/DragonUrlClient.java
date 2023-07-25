package com.BDFH.fakeGG.client;

import com.BDFH.fakeGG.dto.riotApi.Summoner;
import com.BDFH.fakeGG.dto.riotApi.rune.Root;
import com.BDFH.fakeGG.dto.riotApi.summonerSpell.Spell;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(url = "http://ddragon.leagueoflegends.com/cdn/13.14.1/data/en_US", name = "dragon-json")
public interface DragonUrlClient {

    @GetMapping("/summoner.json")
    Spell getSpellIcon();

    @GetMapping("/runesReforged.json")
    List<Root> getRuneIcon();
}
