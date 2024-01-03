package main.java.skiresorts;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Szczyrk {
	private JFrame frame = new JFrame("Szczyrk Mountain Resort");
	private JPanel titlePanel  = new JPanel();
	private JPanel listPanel  = new JPanel();
	private JLabel nameField = new JLabel();
	
	Szczyrk(){
		initFrame();
		initLogoPanel();
		frame.setVisible(true);
	}
	
	void initFrame() {
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setBackground(new Color(255,255,204));
		frame.setLayout(new BorderLayout());
		frame.setLocationRelativeTo(null);
	}
	
	void  initLogoPanel() {
		
	}
}
