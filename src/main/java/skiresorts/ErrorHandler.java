package main.java.skiresorts;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Class for handling errors.
 */
public class ErrorHandler {

	/**
	 * Handles the error when fetching weather data.
	 * 
	 * @param frame Reference to the current JFrame to be closed.
	 */
	public void handleWebScrapperError(JFrame frame) {
		int option = JOptionPane.showConfirmDialog(null,
				"Error while fetching web data.\n"
				+ "Do you want to close the current window?",
				"Error", JOptionPane.YES_NO_OPTION,
				JOptionPane.ERROR_MESSAGE);

		if (option == JOptionPane.YES_OPTION) {
            // Close only the current window, not the entire application
			SwingUtilities.getWindowAncestor(frame).dispose();
		}
	}
	
	public void handleConnectionError() {
		 JOptionPane.showMessageDialog(null,
	                "No internet connection, it isn't possible to display information about ski resorts.",
	                "Error", JOptionPane.ERROR_MESSAGE);
	}
}
