package com.sony.validator;

import com.sony.Exception.InvalidPropertiesException;
import com.sony.components.Ticket;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class GameValidator {

    public synchronized boolean checkTopRowWinner(Ticket ticket, List<Integer> announcedNumbers) {

        boolean isWinner = false;
            List<Integer> topRowTickerNumbers = ticket.getTicketNumbers().get(0);
            Long firstRowMatchedNumbers = topRowTickerNumbers.stream().filter(integer -> announcedNumbers.contains(integer)).count();
            if (firstRowMatchedNumbers == ticket.getItemsPerRow().longValue()) {
                isWinner = true;
            }
        return isWinner;
    }


    public synchronized boolean checkFullHouse(Ticket ticket,List<Integer> announcedNumbers) {
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


    public synchronized boolean checkFirstFiveNumbers(Ticket ticket,List<Integer> announcedNumbers) {
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

    public void validatePropertiesBeforeStartingGame(Integer rows, Integer columns, Integer itemsPerRow, Integer bound, Integer numberOfPlayers) throws InvalidPropertiesException {
        if (rows < 1) {
            throw new InvalidPropertiesException("Please enter valid number of rows should be greater than zero");
        }
        if (columns < 1) {
            throw new InvalidPropertiesException("Please enter valid number of columns should be greater than zero");
        }
        if (itemsPerRow < 1) {
            throw new InvalidPropertiesException("Please enter valid number of Items per row should be greater than zero");

        }
        if (itemsPerRow > columns) {
            throw new InvalidPropertiesException("Items per row should be less than columns size");
        }

        if (bound < 1) {
            throw new InvalidPropertiesException("Please enter valid range should be greater than zero");

        }

        if (bound < (rows * itemsPerRow)) {

            throw new InvalidPropertiesException("Range provided is less than product of rows and items per row , tickets cannot be created");
        }
    }


}
