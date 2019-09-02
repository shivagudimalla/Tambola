package com.sony.services;

import com.sony.Exception.InvalidPropertiesException;
import com.sony.components.Dealer;
import com.sony.components.Game;
import com.sony.validator.GameValidator;
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

    public void startGame() throws InvalidPropertiesException {
        gameValidator.validatePropertiesBeforeStartingGame(game.getRows(), game.getColumns(), game.getItemsPerRow(), game.getBound(), game.getNumberOfPlayers());
        Thread thread=new Thread(dealer);
        thread.start();

    }


}
