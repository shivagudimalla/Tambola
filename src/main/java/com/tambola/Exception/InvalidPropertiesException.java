package com.tambola.Exception;

import com.tambola.components.Dealer;
import org.apache.log4j.Logger;

public class InvalidPropertiesException extends RuntimeException {
    final static Logger logger = Logger.getLogger(Dealer.class);

    /**
     * catches the InvalidPropertiesException
     *
     * @param errorMessage
     */
    public InvalidPropertiesException(String errorMessage) {
        super(errorMessage);
        logger.info("Error while starting game");
    }
}
