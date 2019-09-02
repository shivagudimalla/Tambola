package com.sony.app;
import com.sony.services.GameService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;

@SpringBootConfiguration
@ComponentScan(basePackages = {"com.sony.*"})
public class GameRunner implements CommandLineRunner {

    final static Logger logger = Logger.getLogger(GameRunner.class);
    @Autowired
    private GameService gameService;

    @Bean
    @Primary
    public Integer getNumberOfPlayers(@Value("${numberOfPlayers:5}") Integer numberOfPlayers) {
        return numberOfPlayers;
    }

    @Bean
    @Qualifier("rows")
    public Integer getRows(@Value("${rows:5}") Integer rows) {
        return rows;
    }

    @Bean
    public Integer getColumns(@Value("${columns:10}") Integer columns) {
        return columns;
    }

    @Bean
    public Integer getItemsPerRow(@Value("${itemsPerRow:5}") Integer itemsPerRow) {
        return itemsPerRow;
    }

    @Bean
    public Integer getBound(@Value("${bound:90}") Integer bound) {
        return bound;
    }

    public static void main(String[] args) throws Exception {
        logger.info("Game starting");
        SpringApplication.run(GameRunner.class, args);


    }

    @Override
    public void run(String... args) throws Exception {
        gameService.startGame();
    }

}
