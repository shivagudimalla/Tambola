package com.tambola.app;

import com.tambola.services.GameService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootConfiguration
@ComponentScan(basePackages = {"com.tambola.*"})
@PropertySources({
        @PropertySource("classpath:game.yml")
})
public class TambolaApplication implements CommandLineRunner {

    final static Logger logger = Logger.getLogger(TambolaApplication.class);
    @Autowired
    private GameService gameService;

    public static void main(String[] args) throws Exception {
        logger.info("Game starting");
        SpringApplication.run(TambolaApplication.class, args);


    }

    @Bean(name = "numberOfPlayers")
    public Integer getNumberOfPlayers(@Value("${numberOfPlayers:5}") Integer numberOfPlayers) {
        return numberOfPlayers;
    }

    @Bean(name = "rows")
    public Integer getRows(@Value("${rows:5}") Integer rows) {
        return rows;
    }

    @Bean(name = "columns")
    public Integer getColumns(@Value("${columns:10}") Integer columns) {
        return columns;
    }

    @Bean(name = "itemsPerRow")
    public Integer getItemsPerRow(@Value("${itemsPerRow:5}") Integer itemsPerRow) {
        return itemsPerRow;
    }

    @Bean(name = "bound")
    public Integer getBound(@Value("${bound:90}") Integer bound) {
        return bound;
    }

    @Override
    public void run(String... args) throws Exception {
        gameService.startGame();
    }

}
