package main.java.skiresorts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.apache.logging.log4j.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ResortsList implements ActionListener {
    private static Logger logger = LogManager.getLogger(ResortsList.class);

	private JFrame mainFrame = new JFrame("Ski Resorts App");
	private JPanel titlePanel  = new JPanel();
	private JPanel listPanel  = new JPanel();
	private JLabel nameField = new JLabel();
    private String[] resorts = {"Kotelnica Białczańska", "Korbielów"};
	JButton[] buttons = new JButton[resorts.length];
	Color myColor = new Color(255,255,204);
	Boolean connection;
	
	ResortsList(){
		initFrame();
		initTitlePanel();
		initListPanel();
		
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
		logger.info("ResortsList initialized.");
		connection = new InternetConnectionChecker().isInternetAvailable();
	}
	
	private void initFrame() {
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//mainFrame.setSize(600,600);
		mainFrame.getContentPane().setBackground(myColor);
		mainFrame.setLayout(new BorderLayout());
		//mainFrame.setLocationRelativeTo(null);
		
	}
	private void initTitlePanel(){
		titlePanel.setLayout(new BorderLayout());
		//titlePanel.setBounds(0,0,600,80);
		
		nameField.setBackground(myColor);
		//nameField.setForeground(new Color(25,255,0));
		nameField.setFont(new Font("Helvetica",Font.BOLD,60));
		nameField.setHorizontalAlignment(JLabel.CENTER);
		nameField.setText("Ski Resorts App");
		nameField.setOpaque(true); //przezroczystosc elementu
		
		titlePanel.add(nameField, BorderLayout.CENTER);
		mainFrame.add(titlePanel, BorderLayout.NORTH);
	}
	
	private void initListPanel() {
        listPanel.setLayout(new GridLayout(resorts.length,1));
		//listPanel.setBounds(0,80,600,520);
		listPanel.setBackground(myColor);
		
		//initialize buttons
		for(int i=0; i<resorts.length; i++) {
			buttons[i] = new JButton(resorts[i]);
			buttons[i].setFont(new Font("Helvetica",Font.BOLD,55));
			buttons[i].setForeground(new Color (25,255,200));
			buttons[i].setFocusable(false);
			buttons[i].addActionListener(this);
			buttons[i].setBackground(myColor);
			listPanel.add(buttons[i]);
			
			int finalI = i; 
			buttons[i].addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
		        public void mouseEntered(java.awt.event.MouseEvent evt) {
		            buttons[finalI].setBackground(new Color(173, 216, 230)); // Zmiana koloru po najechaniu myszą
		        }
				@Override
		        public void mouseExited(java.awt.event.MouseEvent evt) {
		            buttons[finalI].setBackground(myColor); // back to myColor
		        }
		    });
		}
		
		mainFrame.add(listPanel, BorderLayout.CENTER);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(!connection) {
			logger.fatal("No internet connection, it isn't possible to display informations about ski resorts.");
			ErrorHandler connectionHandler = new ErrorHandler();
			connectionHandler.handleConnectionError();
			connection = new InternetConnectionChecker().isInternetAvailable();
			logger.error("Method handleConnectionError() called.");
		}else {
			int index = 0;
			for (int i = 0; i < buttons.length; i++) {
	            if (e.getSource() == buttons[i]) {
	                index = i;
	                break;
	            }
	        }
			switch(index) {
				case 0:
					logger.info("Opening Kotelnica Details.");
					new Kotelnica();
					break;
				case 1:
					logger.info("Opening Korbielów Details.");
					new Korbielow();
					break;
				case 2:
					logger.info("Opening Resort3 Details.");
					new Szczyrk();
					break;
				default:
					break;
			}
		}
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
