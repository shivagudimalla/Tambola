package com.tambola.components;

import com.tambola.validator.GameValidator;
import org.apache.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import static com.tambola.constants.GameConstants.EARLY_FIVE_COUNT_TO_CHECK;

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

    public Player(String name, String email) {
        this.setNumbersConsumed(new ArrayList<>());
        this.setName(name);
        this.setEmail(email);
        this.setWinningCombinations(new ArrayList<>());
    }


    public List<Integer> getNumbersConsumed() {
        return numbersConsumed;
    }

    public void setNumbersConsumed(List<Integer> numbersConsumed) {
        this.numbersConsumed = numbersConsumed;
    }

    public com.tambola.validator.GameValidator getGameValidator() {
        return GameValidator;
    }

    public void setGameValidator(com.tambola.validator.GameValidator gameValidator) {
        GameValidator = gameValidator;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
        logger.info("Ticket of " + this.getName() + " is " + ticket.getTicketNumbers().toString());

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


    /**
     * Whenever game thread announces a number, player will get to know through this method
     *
     * @param evt
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Integer valueAnnounced=(Integer)evt.getNewValue();
        this.getNumbersConsumed().add(valueAnnounced);
        logger.info("Player "+this.getName() +" consumed "+ valueAnnounced);
    }


    /**
     * Once the game thread starts the player threads, based on the numbers announced players will cross check and validate
     * with the game validator to see whether they have won or not. if they win , they tell the dealer, dealer will cross verify
     * and announce the winner
     */
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
