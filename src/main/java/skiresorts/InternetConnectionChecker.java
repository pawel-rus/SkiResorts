package main.java.skiresorts;

import java.net.InetAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class to check the availability of an internet connection.
 */
public class InternetConnectionChecker {
    private static Logger logger = LogManager.getLogger(InternetConnectionChecker.class);
    /**
     * Constructor for the InternetConnectionChecker class.
     * Logs a message indicating that an object of this class has been created.
     */
    InternetConnectionChecker(){
        logger.info("Object of InternetConnectionChecker class created.");
    }

    /**
     * Checks the availability of the internet connection by attempting to reach www.google.com.
     * Logs the results
     * @return true if the internet is available, false otherwise.
     */
    public boolean isInternetAvailable() {
        try {
            InetAddress inetAddress = InetAddress.getByName("www.google.com");
            logger.info("Internet connection is available.");
            return inetAddress.isReachable(5000); // Timeout 5s
        } catch (Exception e) {
            logger.error("An error occurred while checking internet availability: " + e.getMessage());
            logger.warn("No internet connection.");
            return false;
        }
    }
}
