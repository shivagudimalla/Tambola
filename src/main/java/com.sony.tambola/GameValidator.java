package com.sony.tambola;

import java.util.List;

public class GameValidator {

    public synchronized boolean checkTopRowWinner(Ticket ticket,List<Integer> announcedNumbers) {

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


}
