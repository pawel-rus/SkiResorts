package main.java.skiresorts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Kotelnica {
    private static Logger logger = LogManager.getLogger(Kotelnica.class);
    
    final String url = "https://bialkatatrzanska.pl/pogoda/aktualne-warunki-pogodowe";
    
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
		webScrapper();
	}
	
	void initTitlePanel(){
		titlePanel.setLayout(new BorderLayout());
		
		nameField.setBackground(new Color(255,255,204));
		nameField.setFont(new Font("Helvetica",Font.BOLD,60));
		nameField.setHorizontalAlignment(JLabel.CENTER);
		nameField.setText("Kotelnica Białczańska");
		nameField.setOpaque(true); 
		
		titlePanel.add(nameField, BorderLayout.CENTER);
		/*
		JLabel imageLabel = new JLabel();
		//ClassLoader classLoader = getClass().getClassLoader();
		//ImageIcon icon = new ImageIcon(classLoader.getResource("/test.jpg"));
		//ImageIcon icon = new ImageIcon(getClass().getResource("/resources/test.jpg"));
		
		try {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("resources/test.jpg");
			if(inputStream != null) {
				BufferedImage iconImage = ImageIO.read(inputStream);
				ImageIcon icon = new ImageIcon(iconImage);
				imageLabel.setIcon(icon);
				titlePanel.add(imageLabel, BorderLayout.CENTER);
			}else {
				logger.error("Can't find image file.");
			}	
		}catch(IOException e) {
			logger.error(e.getMessage());
	        e.printStackTrace();
		}
		*/
		frame.add(titlePanel, BorderLayout.NORTH);
	}
	
	void webScrapper() {
		logger.info("activate web scrapper");
		try {
			final Document document = Jsoup.connect(url).get();
			
			for(Element row : document.select("table#weather-data-current tr")) {
				if(row.select("td.name").text().equals("")) {
					continue;
				} else {
					String condition = row.select("td.name").text();
					String value = row.select("td.value").text();
					System.out.println(condition + " : " + value);
				}
			}
		} catch (IOException e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
}
