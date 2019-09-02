package com.sony.components;
import org.apache.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import static com.sony.components.GameConstants.EARLY_FIVE_COUNT_TO_CHECK;

/**
 * Created by shivakumargudimalla on 8/30/19.
 */
public class Player implements PropertyChangeListener,Runnable
{

    final static Logger logger = Logger.getLogger(Player.class);
    private String name;
    private String email;
    private Ticket ticket;
    private Game game;
    private GameValidator GameValidator;
    private List<Integer> numbersConsumed;
    private List<WinningCombinations> winningCombinations;

    public Player(Ticket ticket, String name, Game game, String email, GameValidator gameValidator) {
        this.setGameValidator(gameValidator);
        this.setNumbersConsumed(new ArrayList<>());
        this.setGame(game);
        this.setName(name);
        this.setTicket(ticket);
        this.setEmail(email);
        this.setWinningCombinations(new ArrayList<>());
    }


    public List<Integer> getNumbersConsumed() {
        return numbersConsumed;
    }

    public void setNumbersConsumed(List<Integer> numbersConsumed) {
        this.numbersConsumed = numbersConsumed;
    }

    public com.sony.components.GameValidator getGameValidator() {
        return GameValidator;
    }

    public void setGameValidator(com.sony.components.GameValidator gameValidator) {
        GameValidator = gameValidator;
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

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Integer valueAnnounced=(Integer)evt.getNewValue();
        this.getNumbersConsumed().add(valueAnnounced);
        logger.info("Player "+this.getName() +" consumed "+ valueAnnounced);
    }

    @Override
    public void run() {
        Integer fullCountCheckStart=this.getTicket().getRows()*this.getTicket().getItemsPerRow();
        Integer topRowCountCheckStart=this.getTicket().getItemsPerRow();
        while(this.getGame().getGameStatus()) {
            if (this.getNumbersConsumed().size() >= EARLY_FIVE_COUNT_TO_CHECK && !this.getGame().isFirstFiveNumbersWinnerAnnounced()) {
                if(this.getGameValidator().checkFirstFiveNumbers(this.getTicket(),this.getNumbersConsumed()))
                this.getGame().getDealer().checkFirstFiveNumbers(this);
            }

            if (this.getNumbersConsumed().size() >= fullCountCheckStart && !this.getGame().isFullHouseWinnerAnnounced()) {
                if (this.getGameValidator().checkFullHouse(this.getTicket(),this.getNumbersConsumed())) {
                    this.getGame().getDealer().checkFullHouse(this);
                }

            }

            if (this.getGame().getAnnouncedNumbers().size() >= topRowCountCheckStart && !this.getGame().isTopRowWinnerAnnounced()) {
                if(this.getGameValidator().checkTopRowWinner(this.getTicket(),this.getNumbersConsumed()))
                this.getGame().getDealer().checkTopRowWinner(this);
            }
        }

    }
}
