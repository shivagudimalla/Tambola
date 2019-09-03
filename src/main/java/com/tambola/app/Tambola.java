package com.tambola.app;

import com.tambola.components.Dealer;
import com.tambola.components.Game;
import com.tambola.validator.GameValidator;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by shivakumargudimalla on 8/29/19.
 */
public class Tambola {

    final static Logger logger = Logger.getLogger(Tambola.class);
    private static Integer bound;
    private static Integer numberOfPlayers;
    private static Integer rows;
    private static Integer columns;
    private static Integer itemsPerRow;

    private static Scanner inputScanner = new Scanner(System.in);
    public static void main(String args[]) throws IOException {

        rows = getNumberOfRows(inputScanner);
        columns = getNumberOfColumns(inputScanner);
        itemsPerRow = getNumberOfItemsPerRow(inputScanner);
        numberOfPlayers = getNumberOfPlayers(inputScanner);
        bound = getNumbersRange(inputScanner);

        GameValidator gameValidator=new GameValidator();
        Game game = new Game(rows, columns, itemsPerRow, bound, numberOfPlayers);
        Runnable dealer = new Dealer(game,gameValidator);
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
                if (bound < (Tambola.rows * Tambola.itemsPerRow)) {
                    logger.info("Range provided is less than product of rows and items per row , tickets cannot be created");
                    inputScanner.next();
                }
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
                if (itemsPerRow > Tambola.columns) {
                    logger.info("Items per row should be less than columns size");
                }
            }
            catch ( InputMismatchException ime)
            {
                inputScanner.next();
            }
        }
        return itemsPerRow;
    }

}
