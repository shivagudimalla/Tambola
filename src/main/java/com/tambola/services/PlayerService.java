package com.tambola.services;

import com.tambola.components.Player;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerService {
    private List<Player> playerList;

    /**
     * This method creates the players based on the number of players input
     *
     * @param numberOfPlayers
     * @return returns the list of players created
     */
    public List<Player> createPlayers(Integer numberOfPlayers) {
        playerList = new ArrayList<>();
        for (Integer i = 0; i < numberOfPlayers; i++) {
            String name = "player" + (i + 1);
            String email = name + "@mail.com";
            Player player = new Player(name, email);
            this.getPlayerList().add(player);
        }
        return playerList;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }
}
