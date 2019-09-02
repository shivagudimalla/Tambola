package com.sony.tambola;
import org.apache.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shivakumargudimalla on 8/30/19.
 */
public class Player implements PropertyChangeListener,Runnable
{

    final static Logger logger = Logger.getLogger(Player.class);
    private String name;
    private String email;
    private Ticket ticket;
    private GameImpl game;
    private List<WinningCombinations> winningCombinations;

    public Player(Ticket ticket, String name, GameImpl game, String email) {
        this.setGame(game);
        this.setName(name);
        this.setTicket(ticket);
        this.setEmail(email);
        this.setWinningCombinations(new ArrayList<>());
        this.getGame().getPlayerList().add(this);
        this.getGame().addPropertyChangeListener(this);
    }



    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        logger.info("Ticket of " +this.getName()+" is "+ticket.getTicketNumbers().toString());
        this.ticket = ticket;
    }

    public List<WinningCombinations> getWinningCombinations() {
        return winningCombinations;
    }

    public void setWinningCombinations(List<WinningCombinations> winningCombinations) {
        this.winningCombinations = winningCombinations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public GameImpl getGame() {
        return game;
    }

    public void setGame(GameImpl game) {
        this.game = game;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Integer valueAnnounced=(Integer)evt.getNewValue();
        logger.info("Player "+this.getName() +" consumed "+ valueAnnounced);
    }

    @Override
    public void run() {


        while(!this.getGame().getStopGameFlag().get()) {
            if (this.getGame().getAnnouncedNumbers().size() >= 5 && !this.getGame().isFirstFiveNumbersWinnerAnnounced()) {
                if(this.getGame().checkFirstFiveNumbers(this))
                this.getGame().getDealer().checkFirstFiveNumbers(this);
            }

            if (this.getGame().getAnnouncedNumbers().size() >= 5 && !this.getGame().isFullHouseWinnerAnnounced()) {
                if (this.getGame().checkFullHouse(this)) {
                    this.getGame().getDealer().checkFullHouse(this);
                }

            }

            if (this.getGame().getAnnouncedNumbers().size() >= 5 && !this.getGame().isTopRowWinnerAnnounced()) {
                if(this.getGame().checkTopRowWinner(this))
                this.getGame().getDealer().checkTopRowWinner(this);
            }
        }

    }
}
