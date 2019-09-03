package com.tambola.services;

import com.tambola.Exception.InvalidPropertiesException;
import com.tambola.components.Dealer;
import com.tambola.components.Game;
import com.tambola.components.Player;
import com.tambola.validator.GameValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {


    private Game game;

    private GameValidator gameValidator;

    private Dealer dealer;

    private PlayerService playerService;

    final static Logger logger = Logger.getLogger(Dealer.class);

    /**
     * This service calls gameValidator to validate the game properties.If game properties are valid,
     * It creates Players and starts the dealer thread
     *
     * @throws InvalidPropertiesException throws this exception if the given rules doesnt meet the criteria
     */
    public void startGame() throws InvalidPropertiesException {
        logger.info("Validating given properties");
        gameValidator.validatePropertiesBeforeStartingGame(this.getGame().getRows(), this.getGame().getColumns(), this.getGame().getItemsPerRow(), this.getGame().getBound(), this.getGame().getNumberOfPlayers());
        List<Player> players = playerService.createPlayers(this.getGame().getNumberOfPlayers());
        game.setPlayerList(players);
        players.forEach(player -> {
            player.setGame(game);
            player.setGameValidator(gameValidator);

        });
        Thread thread=new Thread(dealer);
        thread.start();

    }

    public Game getGame() {
        return game;
    }

    @Autowired
    public void setGame(Game game) {
        this.game = game;
    }

    public GameValidator getGameValidator() {
        return gameValidator;
    }

    @Autowired
    public void setGameValidator(GameValidator gameValidator) {
        this.gameValidator = gameValidator;
    }

    public Dealer getDealer() {
        return dealer;
    }

    @Autowired
    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public PlayerService getPlayerService() {
        return playerService;
    }

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }
}
