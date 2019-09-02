package com.sony.tambola;

import org.apache.log4j.Logger;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by shivakumargudimalla on 8/31/19.
 */
public class GameImpl implements Game, Runnable {


    final static Logger logger = Logger.getLogger(GameImpl.class);

    private volatile  Boolean isTopRowWinnerAnnounced;
    private volatile Boolean isFirstFiveNumbersWinnerAnnounced;
    private volatile Boolean isFullHouseWinnerAnnounced;
    private AtomicBoolean isFullNumbersAnnounced;
    private Integer rows;
    private Integer columns;
    private Integer itemsPerRow;
    private Integer bound;
    private Integer numberOfPlayers;
    private List<Integer> announcedNumbers;
    private ThreadLocalRandom threadLocalRandom;
    private PropertyChangeSupport observable;
    private Dealer dealer;
    private List<Player> playerList;
    private AtomicBoolean stopGameFlag;
    private List<Integer> rangeOfNumbersToBeGenerated;
    private AtomicInteger nextNumberToGenerate;

    public GameImpl(Integer rows, Integer columns, Integer itemsPerRow, Integer bound, Integer numberOfPlayers) {

        this.setStopGameFlag(new AtomicBoolean(false));
        this.setRows(rows);
        this.setColumns(columns);
        this.setItemsPerRow(itemsPerRow);
        this.setBound(bound);
        this.setPlayerList(new ArrayList<>());
        this.setNumberOfPlayers(numberOfPlayers);
        observable = new PropertyChangeSupport(this);
        announcedNumbers = new ArrayList<>();
        threadLocalRandom = ThreadLocalRandom.current();
        this.setTopRowWinnerAnnounced(new Boolean(false));
        this.setFirstFiveNumbersWinnerAnnounced(new Boolean(false));
        this.setFullHouseWinnerAnnounced(new Boolean(false));
        this.setIsFullNumbersAnnounced(new AtomicBoolean(false));
        rangeOfNumbersToBeGenerated=new ArrayList<>();
        nextNumberToGenerate=new AtomicInteger(0);

    }

    public AtomicBoolean getStopGameFlag() {
        return stopGameFlag;
    }


    public AtomicBoolean getIsFullNumbersAnnounced() {
        return isFullNumbersAnnounced;
    }

    public void setIsFullNumbersAnnounced(AtomicBoolean isFullNumbersAnnounced) {
        this.isFullNumbersAnnounced = isFullNumbersAnnounced;
    }

    public void setStopGameFlag(AtomicBoolean stopGameFlag) {
        this.stopGameFlag = stopGameFlag;
    }

    protected Integer getNumberOfPlayers() {
        return numberOfPlayers;
    }

    protected void setNumberOfPlayers(Integer numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    protected void addPropertyChangeListener(PropertyChangeListener pcl) {
        observable.addPropertyChangeListener(pcl);
    }

    protected void removePropertyChangeListener(PropertyChangeListener pcl) {
        observable.removePropertyChangeListener(pcl);
    }

    protected List<Player> getPlayerList() {
        return playerList;
    }

    protected void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    protected Boolean isTopRowWinnerAnnounced() {
        return isTopRowWinnerAnnounced;
    }

    protected void setTopRowWinnerAnnounced(Boolean topRowWinnerAnnounced) {
        isTopRowWinnerAnnounced = topRowWinnerAnnounced;
    }

    protected Boolean isFirstFiveNumbersWinnerAnnounced() {
        return isFirstFiveNumbersWinnerAnnounced;
    }

    protected void setFirstFiveNumbersWinnerAnnounced(Boolean firstFiveNumbersWinnerAnnounced) {
        isFirstFiveNumbersWinnerAnnounced = firstFiveNumbersWinnerAnnounced;
    }

    protected Dealer getDealer() {
        return dealer;
    }

    protected void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    protected Boolean isFullHouseWinnerAnnounced() {
        return isFullHouseWinnerAnnounced;
    }

    protected List<Integer> getAnnouncedNumbers() {
        return announcedNumbers;
    }

    protected void setAnnouncedNumbers(List<Integer> announcedNumbers) {
        this.announcedNumbers = announcedNumbers;
    }

    protected void setFullHouseWinnerAnnounced(Boolean fullHouseWinnerAnnounced) {
        isFullHouseWinnerAnnounced = fullHouseWinnerAnnounced;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getColumns() {
        return columns;
    }

    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    public Integer getItemsPerRow() {
        return itemsPerRow;
    }

    public void setItemsPerRow(Integer itemsPerRow) {
        this.itemsPerRow = itemsPerRow;
    }

    public Integer getBound() {
        return bound;
    }

    public void setBound(Integer bound) {
        this.bound = bound;
    }

    public synchronized boolean checkTopRowWinner(Player player) {

        boolean isWinner = false;

        if (!this.isTopRowWinnerAnnounced()) {
            List<Integer> topRowTickerNumbers = player.getTicket().getTicketNumbers().get(0);


            Long firstRowMatchedNumbers = topRowTickerNumbers.stream().filter(integer -> this.getAnnouncedNumbers().contains(integer)).count();
            if (firstRowMatchedNumbers == this.getItemsPerRow().longValue()) {
                isWinner = true;
            }
        }

        return isWinner;
    }


    public synchronized boolean checkFullHouse(Player player) {
        boolean isWinner = false;
        if (!this.isFullHouseWinnerAnnounced()) {
            Integer rows = player.getTicket().getTicketNumbers().size();
            Long fullHouseMatchedCount = 0L;

            for (Integer row = 0; row < rows; row++) {
                fullHouseMatchedCount = fullHouseMatchedCount + player.getTicket().getTicketNumbers().get(row).stream().filter(integer -> this.getAnnouncedNumbers().contains(integer)).count();

                if (fullHouseMatchedCount == (this.getRows() * this.getItemsPerRow())) {
                    isWinner = true;
                }
            }
        }

        return isWinner;
    }


    public synchronized boolean checkFirstFiveNumbers(Player player) {
        boolean isWinner = false;
        if (!this.isFirstFiveNumbersWinnerAnnounced()) {
            Integer rows = player.getTicket().getTicketNumbers().size();
            Long count = 0L;

            for (Integer row = 0; row < rows; row++) {
                count = count + player.getTicket().getTicketNumbers().get(row).stream().filter(integer -> this.getAnnouncedNumbers().contains(integer)).count();

                if (count >= 5) {
                    isWinner = true;
                    break;
                }
            }
        }


        return isWinner;
    }


    public void generateRandomNumbers(Integer bound) throws IOException {

        while (!(this.getAnnouncedNumbers().size() == bound) && !(this.isFullHouseWinnerAnnounced())) {
            this.getRandomNumber();
        }
    }

    public void registerPlayersAndGenerateTickets(Integer numberOfPlayers, Integer rows, Integer columns, Integer itemsPerRow, Integer bound) {

        ThreadGroup playerGroup = new ThreadGroup("players");
        for (Integer counter = 0; counter < numberOfPlayers; counter++) {
            logger.info("Creating player thread player " + counter);
            Runnable player = new Player(new Ticket(rows, columns, itemsPerRow, bound), "Player" + (counter + 1), this, "Player1" + "@gmail.com");
            Thread playerThread = new Thread(playerGroup, player);
            playerThread.start();

        }
        logger.info("Tickets created");
    }

    @Override
    public void run() {
        registerPlayersAndGenerateTickets(this.getNumberOfPlayers(), this.getRows(), this.getColumns(), this.getItemsPerRow(), this.getBound());
        this.populateNumbersForRandomGenerator();
        this.generateRandomNumber();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("GAME SUMMARY is");
        this.getPlayerList().forEach(player -> {
            player.getWinningCombinations().forEach(winningCombinations -> {
                logger.info(player.getName()+" won "+winningCombinations);
            });
        });
        this.stopGame();
    }


    public  void generateRandomNumber() {
        try {
            if(this.getDealer().getIsCheckingWinnerInProgress().get()){
                try {
                    Thread.currentThread().sleep(5000);
                    logger.info("Pausing thread while announcement");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!(this.getIsFullNumbersAnnounced().get()) && !(this.isFullHouseWinnerAnnounced())) {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                logger.info("Generate Ticket Number: N ");
                StringBuffer answer = new StringBuffer();
                answer.append(br.readLine());
                logger.info(answer);
                if (answer.toString().equals("Q")) {
                    logger.info("You decided to quit the game, ending the game");
                    logger.info("Current group thread is " + Thread.currentThread().getThreadGroup());
                    this.getStopGameFlag().set(false);

                }

                if (!answer.toString().equals("N") && !answer.toString().equals("Q")) {
                    logger.info("Please provide valid request. 'N' to generate next number, 'Q' to quit the game");
                    generateRandomNumber();
                }
                logger.info("Pressed string is " + answer);
                logger.info(answer.equals("N"));
                if (answer.toString().equals("N")) {
                    Integer randomNumber=this.getRandomNumber();
                    Integer oldValue = this.getAnnouncedNumbers().size() > 0 ? this.getAnnouncedNumbers().get(this.getAnnouncedNumbers().size() - 1) : 0;
                    logger.info("Generated number is " + randomNumber);
                    storeAnnouncedNumbers(randomNumber);
                    observable.firePropertyChange("GeneratedNumber", oldValue, randomNumber);
                    nextNumberToGenerate.incrementAndGet();
                    this.generateRandomNumber();

                }


            }
        } catch (IOException e) {
            logger.error("Exception while reading input from user for number generation", e);
        }

    }

    public  Integer getRandomNumber() throws IOException {

        logger.info("Generating random number");
        return rangeOfNumbersToBeGenerated.get(nextNumberToGenerate.get());

    }

    private  void storeAnnouncedNumbers(Integer randomNumber) {
        this.getAnnouncedNumbers().add(randomNumber);
        if (this.getAnnouncedNumbers().size() == this.getBound()) {
            this.getIsFullNumbersAnnounced().set(true);
            logger.info("Announced all the numbers " + this.getAnnouncedNumbers().size() + this.getBound());
        }
    }

    private void populateNumbersForRandomGenerator() {
        rangeOfNumbersToBeGenerated = IntStream.rangeClosed(1, this.getBound())
                .boxed().collect(Collectors.toList());
        Collections.shuffle(rangeOfNumbersToBeGenerated);
    }

    public void stopGame() {
        this.getStopGameFlag().set(true);
    }

}
