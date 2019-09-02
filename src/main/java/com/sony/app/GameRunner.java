package com.sony.app;

import com.sony.components.GameProperties;
import com.sony.services.GameService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootConfiguration
@ComponentScan(basePackages = {"com.sony.*"})
@PropertySources({
        @PropertySource("classpath:game.yml")
})
public class GameRunner implements CommandLineRunner {

    final static Logger logger = Logger.getLogger(GameRunner.class);
    @Autowired
    private GameService gameService;
    @Autowired
    private GameProperties gameProperties;
    public static void main(String[] args) throws Exception {
        logger.info("Game starting");
        SpringApplication.run(GameRunner.class, args);


    }

    @Override
    public void run(String... args) throws Exception {
        gameService.startGame();
    }

}
