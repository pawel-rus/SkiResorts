package main.java.skiresorts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.MediaTracker;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Szczyrk {
    private static Logger logger = LogManager.getLogger(Szczyrk.class);

	final String skiRunsUrl = "https://www.szczyrkowski.pl/resort/mapa-trasy-i-koleje";
	final String weatherUrl = "https://www.szczyrkowski.pl/resort/pogoda";
	private JFrame frame = new JFrame("Szczyrk Mountain Resort");
	private JPanel logoPanel  = new JPanel();
	private JPanel mainPanel  = new JPanel();
	private JLabel bottomPanel = new JLabel();
	
	private Color myColor = new Color(255, 255, 204);
	private HashMap<String, String> weatherDataMap = new LinkedHashMap<>();

	
	Szczyrk(){
		initFrame();
		initLogoPanel();
		initMainPanel();
		initBottomPanel();
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		logger.info("Szczyrk initialized.");
	}
	
	void initFrame() {
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setBackground(myColor);
		frame.setLayout(new BorderLayout());
		frame.setLocationRelativeTo(null);
	}
	
	void  initLogoPanel() {
		logoPanel.setLayout(new BorderLayout());
		try {
			JLabel imageLabel = new JLabel();
			ImageIcon icon = new ImageIcon("resources/szczyrk.png");
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
			nameField.setText("Szczyrk Mountain Resort");
			nameField.setOpaque(true); 
			
			logoPanel.add(nameField, BorderLayout.WEST);
		}
		
		//dodac co bedzie po prawej stronie logoPanel
		weatherWebScrapper();
		
		logoPanel.setOpaque(false);
		frame.add(logoPanel, BorderLayout.NORTH);
	}
	
	private void weatherWebScrapper() {
		JPanel weatherPanel = new JPanel();
		try {
			final Document weatherDoc = Jsoup.connect(weatherUrl).get();
			logger.info("Activate web scrapper for weather data.");
			String condition;
			String value;
			
			/*
			Elements conditionDivs = weatherDoc.getElementsByClass("rwdg-title");
			//Elements conditionDivs = weatherDoc.
			
			for(Element div : conditionDivs) {
				condition = div.text();
				System.out.print(condition);
			}
			for(Element div : weatherDoc.select("div#c185335")) {
				condition = div.text();
				System.out.println(condition);
			}
			*/
			
			/*
			for(Element div : weatherDoc.select("div.rwdg-general-container div")) {
				if(div.select("div.rwdg-title-container p.rwdg-title").text().equals("")) {
					continue;
				} else {
					condition = div.select("div.rwdg-title-container p.rwdg-title").text();
					value = div.select("div.rwdg-pie-container div.rwdg-pie-text-container p rwdg-chart-pie-text").text();
					if(value.equals("") || condition.equals(""))throw new IOException("Empty field.");
					//if(condition.equals("Wiatr")) {
						//value = value.replaceAll("\\s.*", "");
					//}

					weatherDataMap.put(condition, value);
					System.out.println(condition + " : " + value);
					logger.debug("Weather Condition: {} - Value: {}", condition, value);
				}
			}
			*/
		} catch (IOException e){
			logger.error("Error while fetching weather data: {}", e.getMessage());
			//e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	private void initMainPanel() {
		JPanel leftPanel = new JPanel();
		JPanel rightPanel = new JPanel();
		leftWebScrapper();
		
		
	}
	
	private void leftWebScrapper() {
	    try {
	        final Document skiRunsDoc = Jsoup.connect("https://korbielow.net/komunikat-narciarski/").get();
	        System.out.println("Activate web scrapper for ski runs data.");

	        Elements rows = skiRunsDoc.select("table.contenttable tbody tr");
	        for (Element row : rows) {
	            try {
	                Element statusElement = row.select("td.stav img").first();
	                String status = statusElement != null ? statusElement.attr("alt") : "";

	                Element difficultyElement = row.select("td.typ img").first();
	                String difficulty = difficultyElement != null ? difficultyElement.attr("alt") : "";

	                String number = row.select("td.cislo span").text();
	                String name = row.select("td:eq(3)").text();
	                String conditions = row.select("td:eq(4) img").attr("title");
	                String snowDepth = row.select("td:eq(5) img").attr("title");
	                String length = row.select("td:eq(7)").text();
	                String elevationDifference = row.select("td:eq(8)").text();

	                // Process the extracted data as needed
	                System.out.println("Status: " + status);
	                System.out.println("Difficulty: " + difficulty);
	                System.out.println("Number: " + number);
	                System.out.println("Name: " + name);
	                System.out.println("Conditions: " + conditions);
	                System.out.println("Snow Depth: " + snowDepth);
	                System.out.println("Length: " + length);
	                System.out.println("Elevation Difference: " + elevationDifference);
	                System.out.println("-----------------------");
	            } catch (Exception e) {
	                System.err.println("Error processing row: " + e.getMessage());
	            }
	        }
	    } catch (IOException e) {
	        System.err.println("Error while fetching ski runs data: " + e.getMessage());
	        e.printStackTrace();
	        System.exit(1);
	    }
	}
	
	
	
	/*
	private void leftWebScrapper() {
		try {
			final Document skiRunsDoc = Jsoup.connect("https://korbielow.net/komunikat-narciarski/").get();
			logger.info("Activate web scrapper for ski runs data.");
			//String cond;
			Elements rows = skiRunsDoc.select("table.tg tbody tr");
			for(Element row : rows) {
				String cond = row.select("td img ").get(1).text();
			}
			/*
			for(Element row : skiRunsDoc.select("table.table-d.r-all.r-4 tr")) {
				//if(row.select("td").attr("class") == "7") continue;
				System.out.println("LOL");
				//cond = row.select("td").get(0).text();
				//System.out.println(cond);
				//System.out.println(" dd");
				
			}
			
		}catch(IOException e) {
			logger.error("Error while fetching ski runs data: {}", e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}
	*/
	
	private void initBottomPanel() {
		
	}
}
