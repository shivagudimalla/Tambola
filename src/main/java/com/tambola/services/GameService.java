package com.tambola.services;

import com.tambola.Exception.InvalidPropertiesException;
import com.tambola.components.Dealer;
import com.tambola.components.Game;
import com.tambola.validator.GameValidator;
import org.apache.log4j.Logger;
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
    final static Logger logger = Logger.getLogger(Dealer.class);
    public void startGame() throws InvalidPropertiesException {
        logger.info("Validating given properties");
        gameValidator.validatePropertiesBeforeStartingGame(game.getRows(), game.getColumns(), game.getItemsPerRow(), game.getBound(), game.getNumberOfPlayers());
        Thread thread=new Thread(dealer);
        thread.start();

    }


}
