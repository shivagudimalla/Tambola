package com.tambola.app;

import com.tambola.components.Dealer;
import com.tambola.components.Game;
import com.tambola.services.GameService;
import com.tambola.services.PlayerService;
import com.tambola.validator.GameValidator;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertTrue;

/**
 * Integration test to check the application
 */

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:game.yml")
public class GameServiceIntegrationTest {

    final static Logger logger = Logger.getLogger(GameServiceIntegrationTest.class);
    @Autowired
    private GameService gameService;
    @Autowired
    private Game game;
    @Autowired
    private GameValidator gameValidator;
    @Autowired
    private PlayerService playerService;
    @Value("${game.bound:90}")
    private Integer bound;

    @Before
    public void setup() throws Exception {
        StringBuilder input = new StringBuilder();
        for (int i = 1; i <= bound; i++) {
            input.append("N\n");
        }
        System.setIn(new ByteArrayInputStream(input.toString().getBytes()));
    }

    @Test
    public void testTambolaApplicationWithValidProperties() throws InterruptedException {

        gameService.startGame();
        Thread.sleep(9000);
        assertTrue(game.getPlayerList().size() == 2);
        assertTrue(game.getSummary().size() > 0);

    }

    @TestConfiguration
    static class TambolaApplicationTestContextConfiguration {
        @Value("${game.rows:3}")
        private Integer rows;
        @Value("${game.columns:5}")
        private Integer columns;
        @Value("${game.itemsPerRow:3}")
        private Integer itemsPerRow;
        @Value("${game.bound:90}")
        private Integer bound;
        @Value("${game.numberOfPlayers:5}")
        private Integer numberOfPlayers;

        @Bean(name = "rows")
        public Integer getRows(@Value("${game.rows:5}") Integer rows) {
            return rows;
        }

        @Bean(name = "columns")
        public Integer getColumns(@Value("${game.columns:10}") Integer columns) {
            return columns;
        }

        @Bean(name = "itemsPerRow")
        public Integer getItemsPerRow(@Value("${game.itemsPerRow:5}") Integer itemsPerRow) {
            return itemsPerRow;
        }

        @Bean(name = "bound")
        public Integer getBound(@Value("${game.bound:90}") Integer bound) {
            return bound;
        }

        @Bean(name = "numberOfPlayers")
        public Integer getNumberOfPlayers(@Value("${game.numberOfPlayers:5}") Integer numberOfPlayers) {
            return numberOfPlayers;
        }

        @Bean
        public Game game() {
            Game game = new Game(rows, columns, itemsPerRow, bound, numberOfPlayers);
            return game;
        }

        @Bean
        public GameValidator gameValidator() {
            return new GameValidator();
        }

        @Bean
        public Dealer dealer() {
            return new Dealer(new Game(rows, columns, itemsPerRow, bound, numberOfPlayers), new GameValidator());
        }

        @Bean
        public GameService gameService() {
            return new GameService();
        }

        @Bean
        public PlayerService playerService() {
            return new PlayerService();
        }

    }

}
