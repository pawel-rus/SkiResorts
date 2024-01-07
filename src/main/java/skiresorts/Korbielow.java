package main.java.skiresorts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Korbielow {
    private static Logger logger = LogManager.getLogger(Korbielow.class);
    final String skiRunsUrl = "https://korbielow.net/komunikat-narciarski/";
    final String weatherUrl = "http://pilsko.pogoda.cc/";
    private JFrame frame = new JFrame("Korbielów");
	private JPanel logoPanel  = new JPanel();
	private JPanel mainPanel  = new JPanel();
	private JPanel bottomPanel = new JPanel();
	
	private Color myColor = new Color(255, 255, 255);
	private HashMap<String, String> weatherDataMap = new LinkedHashMap<>();
	List<String[]> skiRunsArrayList = new ArrayList<>();

	Korbielow(){
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setBackground(myColor);
		frame.setLayout(new BorderLayout());
		initLogoPanel();
		initMainPanel();
		//skiWebScrapper();
		
		
		frame.add(logoPanel, BorderLayout.NORTH);
		frame.add(mainPanel, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		logger.info("Korbielów initialized.");
	}
	private void initLogoPanel() {
		logoPanel.setLayout(new BorderLayout());
		logoPanel.setBackground(myColor);
		//adding image to logoPanel
		try {
			JLabel imageLabel = new JLabel();
			ImageIcon icon = new ImageIcon("resources/korbielow.jpg");
			if (icon.getImageLoadStatus() == MediaTracker.ERRORED) {
		        throw new IOException("Can't find this file.");
		    }
			imageLabel.setIcon(icon);
			logoPanel.add(imageLabel, BorderLayout.WEST);
		}catch (IOException e) {
			logger.error(e.getMessage());
			
			JLabel nameField = new JLabel();
			nameField.setBackground(myColor);
			nameField.setFont(new Font("Helvetica",Font.BOLD,35));
			nameField.setHorizontalAlignment(JLabel.CENTER);
			nameField.setText("Korbielów");
			nameField.setOpaque(true); 
			logoPanel.add(nameField, BorderLayout.WEST);
		}
		//adding weather data
		scrapeWeatherData();
		JPanel weatherPanel = new JPanel();
		weatherPanel.setBackground(myColor);
		weatherPanel.setLayout(new GridLayout(weatherDataMap.size(),2));
		for(String condition : weatherDataMap.keySet()) {
			logger.info("Adding Labels to weatherPanel");
			weatherPanel.add(new JLabel(condition));
			weatherPanel.add(new JLabel(weatherDataMap.get(condition)));
		}
	    logoPanel.add(weatherPanel, BorderLayout.CENTER);
	    
	    //JPanel rightPanel = new JPanel();
	    //logoPanel.add(rightPanel, BorderLayout.EAST);
	}
	
	private void initMainPanel() {
		skiWebScrapper();
		mainPanel.setBackground(myColor);
		mainPanel.setLayout(new BorderLayout());
		JPanel skiPanel = new JPanel();
		skiPanel.setBackground(myColor);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
	    skiPanel.setLayout(gridBagLayout);
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(0, 5, 0, 5);
	    
	    String[] tHeader = {"Czynne", "Nazwa", "Warunki", "Śnieg", "Długość", "Przewyższenie"};
	    for (int i = 0; i < tHeader.length; i++) {
	        JLabel headerLabel = new JLabel(tHeader[i]);
	        headerLabel.setHorizontalAlignment(JLabel.CENTER);
	        gbc.gridx = i;
	        gbc.gridy = 0;
	        gbc.fill = GridBagConstraints.HORIZONTAL;
	        skiPanel.add(headerLabel, gbc);
	    }
	    
	    for (int row = 0; row < skiRunsArrayList.size(); row++) {
	        String[] array = skiRunsArrayList.get(row);
	        
	        for (int col = 0; col < array.length; col++) {
	            JLabel label;
	            if (col == 0 || col == 2) {
	                int index = array[col].lastIndexOf("/");
	                String fileName = array[col].substring(index + 1);
	                try {
	                    ImageIcon icon = new ImageIcon("resources/" + fileName);
	                    label = new JLabel(icon);
	                } catch (Exception e) {
	                    logger.error(e.getMessage());
	                    label = new JLabel("error");
	                }
	            } else {
	                label = new JLabel(array[col]);
	                if(col != 1) label.setHorizontalAlignment(JLabel.CENTER);
	            }
	            
	            gbc.gridx = col;
	            gbc.gridy = row + 1;
	            gbc.fill = GridBagConstraints.HORIZONTAL;
	            skiPanel.add(label, gbc);
	        }
	    }
	    
	    JScrollPane scrollPane = new JScrollPane(skiPanel);
	    mainPanel.add(scrollPane);
		//mainPanel.add(skiPanel);
		
	}
	

	private void scrapeWeatherData() {
	    try {
	        final Document weatherDoc = Jsoup.connect(weatherUrl).get();
	        logger.info("Activate web scraper for weather data.");
	        Elements rows = weatherDoc.select("table.data tbody tr");
	        String condition;
	        String value;
            for (Element row : rows) {
                condition = row.select(".table_caption").text();
                value = row.select(".table_value").text();
                weatherDataMap.put(condition, value);
                System.out.println(condition + " " + value);
            }
	        
	    } catch (IOException e) {
	        System.err.println("Error while fetching weather data: " + e.getMessage());
	        e.printStackTrace();
	        System.exit(1);
	    }
	}




	
	private void skiWebScrapper() {
        try {
            final Document skiRunsDoc = Jsoup.connect(skiRunsUrl).get();
            System.out.println("Activate web scrapper for ski runs data.");

            Elements rows = skiRunsDoc.select("table.tg tbody tr");
            for (Element row : rows) {
                Elements cells = row.select("td");

                // Skip header row
                if (cells.size() >=6 ) {
                    String status = cells.get(0).select("img").attr("src");

                    if (!status.isEmpty()) {
                        String name = cells.get(1).text();
                        String conditions = cells.get(2).select("img").attr("src");
                        String snowRange = cells.get(3).text();
                        String length = cells.get(4).text();
                        String elevationDifference = cells.get(5).text();
                        
                        if (name.contains("(")) {
                            int index = name.indexOf("(");
                            name = name.substring(0, index);
                        }

                        
                        skiRunsArrayList.add(new String[] {
                        		status,
    							name,
    							conditions,
    							snowRange,
    							length,
    							elevationDifference
    					});
                        
                        // Process the extracted data as needed
                        System.out.println("Status: " + status);
                        System.out.println("Name and Difficulty: " + name);
                        System.out.println("Conditions: " + conditions);
                        System.out.println("Snow Range: " + snowRange);
                        System.out.println("Length: " + length);
                        System.out.println("Elevation Difference: " + elevationDifference);
                        System.out.println("-----------------------");
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error while fetching ski runs data: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
	
	
	
}

