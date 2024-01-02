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
import javax.swing.JPanel;

public class ResortsList implements ActionListener {
    private static Logger logger = LogManager.getLogger(ResortsList.class);

	private JFrame mainFrame = new JFrame("Ski Resorts App");
	private JPanel titlePanel  = new JPanel();
	private JPanel listPanel  = new JPanel();
	private JLabel nameField = new JLabel();
    private String[] resorts = {"Szczyrk Mountain Resort", "Kotelnica Białczańska", "Resort 3"};
	JButton[] buttons = new JButton[resorts.length];

	
	
	ResortsList(){
		initFrame();
		initTitlePanel();
		initListPanel();
		
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}
	
	private void initFrame() {
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//mainFrame.setSize(600,600);
		mainFrame.getContentPane().setBackground(new Color(255,255,204));
		mainFrame.setLayout(new BorderLayout());
		//mainFrame.setLocationRelativeTo(null);
		
	}
	private void initTitlePanel(){
		titlePanel.setLayout(new BorderLayout());
		//titlePanel.setBounds(0,0,600,80);
		
		nameField.setBackground(new Color(255,255,204));
		//nameField.setForeground(new Color(25,255,0));
		nameField.setFont(new Font("Helvetica",Font.BOLD,60));
		nameField.setHorizontalAlignment(JLabel.CENTER);
		nameField.setText("Ski Resorts App");
		nameField.setOpaque(true); //przezroczystosc elementu
		
		titlePanel.add(nameField, BorderLayout.CENTER);
		mainFrame.add(titlePanel, BorderLayout.NORTH);
	}
	
	private void initListPanel() {
        listPanel.setLayout(new GridLayout(3,1));
		//listPanel.setBounds(0,80,600,520);
		listPanel.setBackground(new Color(255, 255, 204));
		
		//initialize buttons
		for(int i=0; i<resorts.length; i++) {
			buttons[i] = new JButton(resorts[i]);
			buttons[i].setFont(new Font("Helvetica",Font.BOLD,55));
			buttons[i].setForeground(new Color (25,255,200));
			buttons[i].setFocusable(false);
			buttons[i].addActionListener(this);
			buttons[i].setBackground(new Color(255, 255, 204));
			listPanel.add(buttons[i]);
		}
		
		mainFrame.add(listPanel, BorderLayout.CENTER);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		int index = 0;
		for (int i = 0; i < buttons.length; i++) {
            if (e.getSource() == buttons[i]) {
                index = i;
                break;
            }
        }
		switch(index) {
			case 0:
				logger.info("Opening Szczyrk Details.");
				new Szczyrk();
				break;
			case 1:
				new Kotelnica();
				logger.info("Opening Kotelnica Details.");
				break;
			case 2:
				break;
			default:
				break;
		}
	}
	
	void openResortDetails(String resortName){
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
