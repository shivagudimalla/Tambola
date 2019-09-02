package com.sony.tambola;

import org.apache.log4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by shivakumargudimalla on 8/30/19.
 */
public class Dealer implements Runnable {

    final static Logger logger = Logger.getLogger(Dealer.class);
    private String name;
    private GameImpl game;
    private AtomicBoolean isCheckingWinnerInProgress;


    public Dealer(GameImpl game, String name) {
        this.setGame(game);
        this.setName(name);
        game.setDealer(this);
        this.setIsCheckingWinnerInProgress(new AtomicBoolean(false));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AtomicBoolean getIsCheckingWinnerInProgress() {
        return isCheckingWinnerInProgress;
    }

    public void setIsCheckingWinnerInProgress(AtomicBoolean isCheckingWinnerInProgress) {
        this.isCheckingWinnerInProgress = isCheckingWinnerInProgress;
    }

    public GameImpl getGame() {
        return game;
    }

    public void setGame(GameImpl game) {
        this.game = game;
    }


    public synchronized boolean checkTopRowWinner(Player player) {
        this.getIsCheckingWinnerInProgress().set(true);
        logger.info("Dealer checking Top row winner for the player " + player.getName());
        if (this.getGame().checkTopRowWinner(player)) {
            player.getWinningCombinations().add(WinningCombinations.TOPLINE);
            this.getGame().setTopRowWinnerAnnounced(true);
            logger.info("Top row numbers for  " + player.getName() + " is " + player.getTicket().getTicketNumbers().get(0));
            logger.info("Winner of Top Line is " + player.getName());
            this.getIsCheckingWinnerInProgress().set(false);
            return true;

        } else {
            this.getIsCheckingWinnerInProgress().set(false);
            return false;
        }
    }


    public synchronized boolean checkFullHouse(Player player) {
        this.getIsCheckingWinnerInProgress().set(true);
        logger.info("Dealer checking Full house for the player " + player.getName());

        if (this.getGame().checkFullHouse(player)) {
            logger.info("Adding winning combinations");
            player.getWinningCombinations().add(WinningCombinations.FULLHOUSE);
            this.getGame().setFullHouseWinnerAnnounced(true);
            logger.info("Winner of fullHouse is " + player.getName());
            this.getIsCheckingWinnerInProgress().set(false);
            return true;
        } else {
            this.getIsCheckingWinnerInProgress().set(false);
            return false;
        }

    }


    public synchronized boolean checkFirstFiveNumbers(Player player) {
        this.getIsCheckingWinnerInProgress().set(true);
        logger.info("Dealer checking FirstFiveNumbers for the player " + player.getName());

        if (this.getGame().checkFirstFiveNumbers(player)) {
            player.getWinningCombinations().add(WinningCombinations.EARLYFIVE);
            this.getGame().setFirstFiveNumbersWinnerAnnounced(true);
            logger.info("Winner of firstFiveNumber is " + player.getName());
            this.getIsCheckingWinnerInProgress().set(false);
            return true;
        } else {
            this.getIsCheckingWinnerInProgress().set(false);
            return false;
        }

    }

    @Override
    public void run() {
        ThreadGroup threadGroup = new ThreadGroup("GameThread");
        Thread thread = new Thread(threadGroup, this.getGame());
        thread.start();
    }
}
