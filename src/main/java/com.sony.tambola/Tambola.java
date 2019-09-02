package com.sony.tambola;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by shivakumargudimalla on 8/29/19.
 */
public class Tambola {

    final static Logger logger = Logger.getLogger(Tambola.class);
    private static Scanner inputScanner = new Scanner(System.in);
    public static void main(String args[]) throws IOException {

        Integer bound = getNumbersRange(inputScanner);
        Integer numberOfPlayers = getNumberOfPlayers(inputScanner);
        Integer rows = getNumberOfRows(inputScanner);
        Integer columns = getNumberOfColumns(inputScanner);
        Integer itemsPerRow = getNumberOfItemsPerRow(inputScanner);
        GameValidator gameValidator=new GameValidator();
        Game game = new Game(rows, columns, itemsPerRow, bound, numberOfPlayers);
        Runnable dealer = new Dealer(game, "Dealer",gameValidator);
        ThreadGroup threadGroup = new ThreadGroup("Dealer");
        Thread dealerThread = new Thread(threadGroup, dealer, "dealerThread");

        logger.info("Dealer is starting the game");
        dealerThread.start();

    }

    public static Integer getNumbersRange(Scanner inputScanner) {
        Integer bound  = 0;
        while (bound < 1)
        {
            try
            {
                logger.info("provide a valid number range greater than zero: ");
                bound = inputScanner.nextInt();
            }
            catch ( InputMismatchException ime)
            {
                inputScanner.next();
            }
        }
        return bound;
    }

    public static Integer getNumberOfPlayers(Scanner inputScanner) {

        Integer numberOfPlayers  = 0;
        while (numberOfPlayers < 1)
        {
            try
            {
                logger.info("provide valid number of players playing the game: ");
                numberOfPlayers = inputScanner.nextInt();
            }
            catch ( InputMismatchException ime)
            {
                inputScanner.next();
            }
        }
        return numberOfPlayers;

    }

    public static Integer getNumberOfRows(Scanner inputScanner) {

        //Scanner rowScanner = new Scanner(System.in);
        Integer rows  = 0;
        while (rows < 1)
        {
            try
            {
                logger.info("provide  valid rows count greater than 0 : ");
                rows = inputScanner.nextInt();
            }
            catch ( InputMismatchException ime)
            {
                inputScanner.next();
            }
        }
        return rows;
    }

    public static Integer getNumberOfColumns(Scanner inputScanner) {

        Integer columns  = 0;
        while (columns < 1)
        {
            try
            {
                logger.info("provide valid columns count greater than 0 ");
                columns = inputScanner.nextInt();
            }
            catch ( InputMismatchException ime)
            {
                inputScanner.next();
            }
        }
        return columns;
    }


    public static Integer getNumberOfItemsPerRow(Scanner inputScanner) {

        Integer itemsPerRow  = 0;
        while (itemsPerRow < 1)
        {
            try
            {
                logger.info("provide valid Items per row count greater than 0");
                itemsPerRow = inputScanner.nextInt();
            }
            catch ( InputMismatchException ime)
            {
                inputScanner.next();
            }
        }
        return itemsPerRow;
    }

}
