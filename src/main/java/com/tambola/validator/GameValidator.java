package com.tambola.validator;

import com.tambola.Exception.InvalidPropertiesException;
import com.tambola.components.Ticket;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class GameValidator {

    /**
     * this method checks the top row combination for the given ticket and announced numbers
     *
     * @param ticket
     * @param announcedNumbers
     * @return returns true if the ticket has Top row combination otherwise false
     */
    public synchronized boolean checkTopRowWinner(Ticket ticket, List<Integer> announcedNumbers) {

        boolean isWinner = false;
        List<Integer> topRowTickerNumbers = ticket.getTicketNumbers().get(0);
        Long firstRowMatchedNumbers = topRowTickerNumbers.stream().filter(integer -> announcedNumbers.contains(integer)).count();
        if (firstRowMatchedNumbers == ticket.getItemsPerRow().longValue()) {
            isWinner = true;
        }
        return isWinner;
    }

    /**
     * this method checks the full house combination for the given ticket and announced numbers
     * @param ticket
     * @param announcedNumbers
     * @return returns true if the ticket has full house combination otherwise false
     */

    public synchronized boolean checkFullHouse(Ticket ticket, List<Integer> announcedNumbers) {
        boolean isWinner = false;
        Integer rows = ticket.getTicketNumbers().size();
        Long fullHouseMatchedCount = 0L;

        for (Integer row = 0; row < rows; row++) {
            fullHouseMatchedCount = fullHouseMatchedCount + ticket.getTicketNumbers().get(row).stream().filter(integer -> announcedNumbers.contains(integer)).count();

            if (fullHouseMatchedCount == (ticket.getRows() * ticket.getItemsPerRow())) {
                isWinner = true;
            }
        }

        return isWinner;
    }

    /**
     * this method checks the early five combination for the given ticket and announced numbers
     * @param ticket
     * @param announcedNumbers
     * @return true if the given ticket has first five combination otherwise returns false
     */

    public synchronized boolean checkFirstFiveNumbers(Ticket ticket, List<Integer> announcedNumbers) {
        boolean isWinner = false;

        Integer rows = ticket.getTicketNumbers().size();
        Long count = 0L;

        for (Integer row = 0; row < rows; row++) {
            count = count + ticket.getTicketNumbers().get(row).stream().filter(integer -> announcedNumbers.contains(integer)).count();

            if (count >= 5) {
                isWinner = true;
                break;
            }
        }

        return isWinner;
    }

    /**
     * this method takes the game rules provided  and validates it. if everything is good, game gets stared otherwise asked to provide the
     * valid values
     * @param rows
     * @param columns
     * @param itemsPerRow numbers of items to be filled per row
     * @param bound range of numbers to be announced for the game
     * @param numberOfPlayers
     * @throws InvalidPropertiesException throws this exception if the given rules doesnt meet the criteria
     */
    public void validatePropertiesBeforeStartingGame(Integer rows, Integer columns, Integer itemsPerRow, Integer bound, Integer numberOfPlayers) throws InvalidPropertiesException {
        if (rows < 1) {
            throw new InvalidPropertiesException("Please enter valid number of rows for ticket, should be greater than zero");
        }
        if (columns < 1) {
            throw new InvalidPropertiesException("Please enter valid number of columns for ticket, should be greater than zero");
        }
        if (itemsPerRow < 1) {
            throw new InvalidPropertiesException("Please enter valid number of Items per row for ticket, should be greater than zero");

        }
        if (itemsPerRow > columns) {
            throw new InvalidPropertiesException("Items per row should be less than columns size");
        }

        if (bound < 1) {
            throw new InvalidPropertiesException("Please enter valid range of numbers to generate ticket, should be product of rows and items per row");

        }

        if (bound < (rows * itemsPerRow)) {

            throw new InvalidPropertiesException("Range provided is less than product of rows and items per row , tickets cannot be created");
        }
    }


}
