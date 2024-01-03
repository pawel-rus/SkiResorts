package main.java.skiresorts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Kotelnica {
    private static Logger logger = LogManager.getLogger(Kotelnica.class);

	private JFrame frame = new JFrame("Kotelnica Białczańska");
	private JPanel titlePanel  = new JPanel();
	private JPanel mainPanel  = new JPanel();
	private JLabel nameField = new JLabel();
	
	Kotelnica(){
		initFrame();
		initTitlePanel();
		initMainPanel();
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		logger.info("Kotelnica initialized.");
	}
	
	void initFrame() {
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setBackground(new Color(255,255,204));
		frame.setLayout(new BorderLayout());
		
	}
	
	private void initMainPanel() {
		// TODO Auto-generated method stub
		
	}
	
	void initTitlePanel(){
		titlePanel.setLayout(new BorderLayout());
		
		nameField.setBackground(new Color(255,255,204));
		nameField.setFont(new Font("Helvetica",Font.BOLD,60));
		nameField.setHorizontalAlignment(JLabel.CENTER);
		nameField.setText("Kotelnica Białczańska");
		nameField.setOpaque(true); 
		
		titlePanel.add(nameField, BorderLayout.CENTER);
		frame.add(titlePanel, BorderLayout.NORTH);
	}
}
