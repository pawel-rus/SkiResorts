package main.java.skiresorts;

import java.net.InetAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InternetConnectionChecker {
    private static Logger logger = LogManager.getLogger(Kotelnica.class);

    InternetConnectionChecker(){
        if (isInternetAvailable()) {
            logger.info("Internet connection is available.");
        } else {
            logger.warn("No internet connection.");
        }
    }

    public static boolean isInternetAvailable() {
        try {
            InetAddress inetAddress = InetAddress.getByName("www.google.com");
            return inetAddress.isReachable(5000); // Timeout 5s
        } catch (Exception e) {
            logger.error("An error occurred while checking internet availability: " + e.getMessage());
            return false;
        }
    }
}
