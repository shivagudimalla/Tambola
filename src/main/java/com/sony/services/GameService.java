package com.sony.services;

import com.sony.components.Dealer;
import com.sony.components.Game;
import com.sony.components.GameValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    @Autowired
    private Game game;
    @Autowired
    private GameValidator gameValidator;
    @Autowired
    private Dealer dealer;

    public void startGame() {

        Thread thread=new Thread(dealer);
        thread.start();

    }

}
