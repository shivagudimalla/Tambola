package com.sony.tambola;

import org.apache.log4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by shivakumargudimalla on 8/30/19.
 */
public class Dealer implements Runnable {

    final static Logger logger = Logger.getLogger(Dealer.class);
    private String name;
    private Game game;
    private GameValidator gameValidator;

    public Dealer(Game game, String name, GameValidator gameValidator) {
        this.setGame(game);
        this.setName(name);
        this.setGameValidator(gameValidator);
        game.setDealer(this);
    }

    public String getName() {
        return name;
    }


    public GameValidator getGameValidator() {
        return gameValidator;
    }

    public void setGameValidator(GameValidator gameValidator) {
        this.gameValidator = gameValidator;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }


    public synchronized boolean checkTopRowWinner(Player player) {
        logger.info("Dealer checking Top row winner for the player " + player.getName());
        if(!this.getGame().isTopRowWinnerAnnounced()) {
            if (this.getGameValidator().checkTopRowWinner(player.getTicket(), this.getGame().getAnnouncedNumbers())) {
                player.getWinningCombinations().add(WinningCombinations.TOPLINE);
                this.getGame().getSummary().put(WinningCombinations.TOPLINE,player);
                this.getGame().setTopRowWinnerAnnounced(true);
                logger.info("Top row numbers for  " + player.getName() + " is " + player.getTicket().getTicketNumbers().get(0));
                logger.info("Winner of Top Line is " + player.getName());
                return true;

            }
        }
        return false;
    }


    public synchronized boolean checkFullHouse(Player player) {
        logger.info("Dealer checking Full house for the player " + player.getName());

        if(!this.getGame().isFullHouseWinnerAnnounced()) {
            if (this.getGameValidator().checkFullHouse(player.getTicket(), this.getGame().getAnnouncedNumbers())) {
                this.getGame().setFullHouseWinnerAnnounced(true);
                logger.info("Adding winning combinations");
                player.getWinningCombinations().add(WinningCombinations.FULLHOUSE);
                this.getGame().getSummary().put(WinningCombinations.FULLHOUSE,player);
                logger.info("Winner of fullHouse is " + player.getName());
                return true;
            }
        }
        return false;

    }


    public synchronized boolean checkFirstFiveNumbers(Player player) {
        logger.info("Dealer checking FirstFiveNumbers for the player " + player.getName());

        if(!this.getGame().isFirstFiveNumbersWinnerAnnounced()) {
            if (this.getGameValidator().checkFirstFiveNumbers(player.getTicket(), this.getGame().getAnnouncedNumbers())) {
                player.getWinningCombinations().add(WinningCombinations.EARLYFIVE);
                this.getGame().getSummary().put(WinningCombinations.EARLYFIVE,player);
                this.getGame().setFirstFiveNumbersWinnerAnnounced(true);
                logger.info("Winner of firstFiveNumber is " + player.getName());
                return true;
            }
        }
        return false;

    }

    @Override
    public void run() {
        ThreadGroup threadGroup = new ThreadGroup("GameThread");
        Thread thread = new Thread(threadGroup, this.getGame());
        thread.start();
    }
}
