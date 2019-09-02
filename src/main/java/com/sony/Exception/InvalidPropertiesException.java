package com.sony.Exception;

import com.sony.components.Dealer;
import org.apache.log4j.Logger;

public class InvalidPropertiesException extends RuntimeException {
    final static Logger logger = Logger.getLogger(Dealer.class);
    public InvalidPropertiesException(String errorMessage) {
        super(errorMessage);
        logger.info("Error while starting game");
    }
}
