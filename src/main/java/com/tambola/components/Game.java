package com.tambola.components;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.tambola.constants.GameConstants.*;

/**
 * Created by shivakumargudimalla on 8/31/19.
 */
@Component
public class Game implements Runnable {

    final static Logger logger = Logger.getLogger(Game.class);

    private volatile Boolean isTopRowWinnerAnnounced;
    private volatile Boolean isFirstFiveNumbersWinnerAnnounced;
    private volatile Boolean isFullHouseWinnerAnnounced;
    private AtomicBoolean isFullNumbersAnnounced;

    @Autowired
    private Integer rows;
    @Autowired
    private Integer columns;
    @Autowired
    private Integer itemsPerRow;
    @Autowired
    private Integer bound;
    @Autowired
    private Integer numberOfPlayers;
    private List<Integer> announcedNumbers;
    private ThreadLocalRandom threadLocalRandom;
    private PropertyChangeSupport observable;
    @Autowired
    private Dealer dealer;
    private List<Player> playerList;
    private AtomicBoolean isGameRunning;
    private List<Integer> rangeOfNumbersToBeGenerated;
    private AtomicInteger nextNumberToGenerate;
    private Map<WinningCombinations, Player> summary;


    public Game(Integer rows, Integer columns, Integer itemsPerRow, Integer bound, Integer numberOfPlayers) {

        this.setGameRunning(new AtomicBoolean(true));
        this.setRows(rows);
        this.setColumns(columns);
        this.setItemsPerRow(itemsPerRow);
        this.setBound(bound);
        this.setPlayerList(new ArrayList<>());
        this.setNumberOfPlayers(numberOfPlayers);
        this.setObservable(new PropertyChangeSupport(this));
        this.setAnnouncedNumbers(new ArrayList<>());
        this.setTopRowWinnerAnnounced(false);
        this.setFirstFiveNumbersWinnerAnnounced(false);
        this.setFullHouseWinnerAnnounced(false);
        this.setIsFullNumbersAnnounced(new AtomicBoolean(false));
        this.setRangeOfNumbersToBeGenerated(new ArrayList<>());
        this.setNextNumberToGenerate(new AtomicInteger(0));
        this.setSummary(new HashMap<>());
        this.setDealer(dealer);
    }

    public List<Integer> getRangeOfNumbersToBeGenerated() {
        return rangeOfNumbersToBeGenerated;
    }

    public void setRangeOfNumbersToBeGenerated(List<Integer> rangeOfNumbersToBeGenerated) {
        this.rangeOfNumbersToBeGenerated = rangeOfNumbersToBeGenerated;
    }

    public AtomicInteger getNextNumberToGenerate() {
        return nextNumberToGenerate;
    }

    public void setNextNumberToGenerate(AtomicInteger nextNumberToGenerate) {
        this.nextNumberToGenerate = nextNumberToGenerate;
    }

    public PropertyChangeSupport getObservable() {
        return observable;
    }

    public void setObservable(PropertyChangeSupport observable) {
        this.observable = observable;
    }

    public AtomicBoolean getIsGameRunningFlag() {
        return isGameRunning;
    }

    public Map<WinningCombinations, Player> getSummary() {
        return summary;
    }

    public void setSummary(Map<WinningCombinations, Player> summary) {
        this.summary = summary;
    }

    public AtomicBoolean getIsFullNumbersAnnounced() {
        return isFullNumbersAnnounced;
    }

    public void setIsFullNumbersAnnounced(AtomicBoolean isFullNumbersAnnounced) {
        this.isFullNumbersAnnounced = isFullNumbersAnnounced;
    }

    public void setGameRunning(AtomicBoolean stopGameFlag) {
        this.isGameRunning = stopGameFlag;
    }

    public Integer getNumberOfPlayers() {
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

    public List<Player> getPlayerList() {
        return playerList;
    }

    void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    Boolean isTopRowWinnerAnnounced() {
        return isTopRowWinnerAnnounced;
    }

    void setTopRowWinnerAnnounced(Boolean topRowWinnerAnnounced) {
        isTopRowWinnerAnnounced = topRowWinnerAnnounced;
    }

    Boolean isFirstFiveNumbersWinnerAnnounced() {
        return isFirstFiveNumbersWinnerAnnounced;
    }

    void setFirstFiveNumbersWinnerAnnounced(Boolean firstFiveNumbersWinnerAnnounced) {
        isFirstFiveNumbersWinnerAnnounced = firstFiveNumbersWinnerAnnounced;
    }

    Dealer getDealer() {
        return dealer;
    }

    @Autowired
    void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    Boolean isFullHouseWinnerAnnounced() {
        return isFullHouseWinnerAnnounced;
    }

    List<Integer> getAnnouncedNumbers() {
        return announcedNumbers;
    }

    void setAnnouncedNumbers(List<Integer> announcedNumbers) {
        this.announcedNumbers = announcedNumbers;
    }

    void setFullHouseWinnerAnnounced(Boolean fullHouseWinnerAnnounced) {
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

    public void registerPlayersAndGenerateTickets(Integer numberOfPlayers, Integer rows, Integer columns, Integer itemsPerRow, Integer bound) {

        ThreadGroup playerGroup = new ThreadGroup("players");
        for (Integer counter = 0; counter < numberOfPlayers; counter++) {
            logger.info("Creating player thread player " + counter);
            Player player = new Player(new Ticket(rows, columns, itemsPerRow, bound), "Player" + (counter + 1), this, "Player1" + "@gmail.com", this.getDealer().getGameValidator());
            this.addPropertyChangeListener(player);
            this.getPlayerList().add(player);
            Thread playerThread = new Thread(playerGroup, player);
            playerThread.start();

        }
        logger.info("Tickets created");
    }

    @Override
    public void run() {
        registerPlayersAndGenerateTickets(this.getNumberOfPlayers(), this.getRows(), this.getColumns(), this.getItemsPerRow(), this.getBound());
        this.populateNumbersForRandomGenerator();
        InputStream inputStream = System.in;
        Scanner randomGeneratorScanner = new Scanner(inputStream);
        this.generateRandomNumber(randomGeneratorScanner);
        this.stopGame();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            logger.error("Error while interrupting the thread");
        }
        printSummary();
    }


    private void printSummary() {
        if (this.getSummary().size() > 0) {
            logger.info(GAME_SUMMARY);
            this.getSummary().forEach((winningCombinations, player) -> {
                logger.info(player.getName() + " " + WON + " " + winningCombinations);
            });
        }
    }

    private void generateRandomNumber(Scanner randomGeneratorScanner) {
        try {

            while (!(this.getIsFullNumbersAnnounced().get()) && !(this.isFullHouseWinnerAnnounced()) && this.getIsGameRunningFlag().get()) {
                logger.info("Generate Ticket Number: N ");
                String answer = null;
                answer = randomGeneratorScanner.next().toUpperCase();
                if (answer.equals(USER_INPUT_TO_QUIT_GAME)) {
                    logger.info("ending the game");
                    logger.info("Current group thread is " + Thread.currentThread().getThreadGroup());
                    stopGame();

                }

                if (!answer.equals(USER_INPUT_TO_GENERATE_NUMBER) && !answer.equals(USER_INPUT_TO_QUIT_GAME)) {
                    logger.info("Please provide valid request. 'N' to generate next number, 'Q' to quit the game");
                }
                if (answer.equals(USER_INPUT_TO_GENERATE_NUMBER)) {
                    Integer randomNumber = this.getRandomNumber();
                    Integer oldValue = this.getAnnouncedNumbers().size() > 0 ? this.getAnnouncedNumbers().get(this.getAnnouncedNumbers().size() - 1) : 0;
                    logger.info("Generated number is " + randomNumber);
                    storeAnnouncedNumbers(randomNumber);
                    observable.firePropertyChange("GeneratedNumber", oldValue, randomNumber);
                    nextNumberToGenerate.incrementAndGet();

                }


            }
        } catch (InputMismatchException e) {
            logger.info("Please provide valid request. 'N' to generate next number, 'Q' to quit the game");
            randomGeneratorScanner.next();
            logger.error("Exception while reading input from user for number generation", e);
        } catch (NoSuchElementException ne) {
            throw ne;
        }

    }

    public Integer getRandomNumber() {
        logger.info(GENERATING_RANDOM_NUMBER);
        return rangeOfNumbersToBeGenerated.get(nextNumberToGenerate.get());

    }

    private void storeAnnouncedNumbers(Integer randomNumber) {
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
        this.getIsGameRunningFlag().set(false);
    }

    public boolean getGameStatus() {
        return this.getIsGameRunningFlag().get();
    }

}
