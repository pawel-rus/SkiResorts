package main.java.skiresorts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Kotelnica {
    private static Logger logger = LogManager.getLogger(Kotelnica.class);
    
    final String conditionsUrl = "https://bialkatatrzanska.pl/pogoda/aktualne-warunki-pogodowe";
    final String skiRunsUrl = "https://bialkatatrzanska.pl/warunki-narciarskie";
    
	private JFrame frame = new JFrame("Kotelnica Białczańska");
	private JPanel logoPanel  = new JPanel();
	private JPanel mainPanel  = new JPanel();
	
	private HashMap<String, String> weatherDataMap = new HashMap<>();
	List<String[]> skiRunsArrayList = new ArrayList<>();
	//List<String> headings = new ArrayList<>();
	List<String> skiRunsList = new ArrayList<>();
	List<String> difficultyLevel = new ArrayList<>();
	List<String> isActive = new ArrayList<>();
	List<String> iceSheet = new ArrayList<>();
	List<String> skiLength = new ArrayList<>();
	List<String> snow = new ArrayList<>();
	List<String> light = new ArrayList<>();
	
	Kotelnica(){
		initFrame();
		initLogoPanel();
		initMainPanel();
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		logger.info("Kotelnica initialized.");
	}
	
	void initFrame() {
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//frame.getContentPane().setBackground(new Color(255,255,204));
		frame.getContentPane().setBackground(new Color(255,255,255));

		frame.setLayout(new BorderLayout());
		
	}
	
	private void initMainPanel() {
		webScrapper();
		
		mainPanel.setLayout(new BorderLayout());
		JPanel leftColumn = new JPanel(new GridLayout(weatherDataMap.size() + 1, 2));
		JPanel rightColumn = new JPanel(new GridLayout(skiRunsArrayList.size() + 1,skiRunsArrayList.get(0).length));
		
		leftColumn.add(new JLabel("WARUNKI POGODOWE"));
	    leftColumn.add(new JLabel(""));
		for(String condition : weatherDataMap.keySet()) {
			String value = weatherDataMap.get(condition);
			
			JLabel conditionLabel = new JLabel(condition);
			JLabel valueLabel = new JLabel(value);
			
			leftColumn.add(conditionLabel);
			leftColumn.add(valueLabel);
		}
		
		rightColumn.add(new JLabel("TRASA"));
	    rightColumn.add(new JLabel("TRUDNOŚĆ"));
	    rightColumn.add(new JLabel("CZYNNA"));
	    rightColumn.add(new JLabel("POKRYWA ŚNIEŻNA   "));
	    rightColumn.add(new JLabel("DŁUGOŚĆ"));
	    rightColumn.add(new JLabel("NAŚNIEŻANIE"));
	    rightColumn.add(new JLabel("OŚWIETLENIE"));
		/*
		for(String header : headings) {
			rightColumn.add(new JLabel(header));
		}
		*/
		for(String[] array : skiRunsArrayList) {
			for(String elem : array) {
				rightColumn.add(new JLabel(elem));
			}
		}
		
		
		
		mainPanel.add(leftColumn, BorderLayout.WEST);
		mainPanel.add(rightColumn, BorderLayout.EAST);
		frame.add(mainPanel, BorderLayout.CENTER);
	
	
	}
	
	void initLogoPanel(){
	
		logoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		try {
			JLabel imageLabel = new JLabel();
			ImageIcon icon = new ImageIcon("resources/kotelnica.png");
			imageLabel.setIcon(icon);
			logoPanel.add(imageLabel);
		}catch (Exception e) {
			logger.error(e.getMessage());
			
			JLabel nameField = new JLabel();
			nameField.setBackground(new Color(255,255,204));
			nameField.setFont(new Font("Helvetica",Font.BOLD,60));
			nameField.setHorizontalAlignment(JLabel.CENTER);
			nameField.setText("Kotelnica Białczańska");
			nameField.setOpaque(true); 
			
			logoPanel.add(nameField, BorderLayout.CENTER);
		}
		logoPanel.setOpaque(false);
		frame.add(logoPanel, BorderLayout.NORTH);
	}
	
	void webScrapper() {
		
		try {
			final Document document = Jsoup.connect(conditionsUrl).get();
			logger.info("Activate web scrapper for weather data.");
			for(Element row : document.select("table#weather-data-current tr")) {
				if(row.select("td.name").text().equals("")) {
					continue;
				} else {
					String condition = row.select("td.name").text();
					String value = row.select("td.value").text();
					weatherDataMap.put(condition, value);
					System.out.println(condition + " : " + value);
					logger.info("Weather Condition: {} - Value: {}", condition, value);
				}
			}
		} catch (IOException e){
			logger.error("Error while fetching weather data: {}", e.getMessage());
			//e.printStackTrace();
			System.exit(1);
		}
		
		
		try {
			final Document skiRunsDoc = Jsoup.connect(skiRunsUrl).get();
			logger.info("Activate web scrapper for ski runs data.");
			/*
			for(Element row : skiRunsDoc.select("thead tr")) {
				headings.add(row.select("th.ski-run").text());
				headings.add(row.select("th.center.is-active").text());
				headings.add(row.select("th.ice-sheet").text());
				headings.add(row.select("th.length").text());
				headings.add(row.select("th.center.snow").text());
				headings.add(row.select("th.center.light").text());
			}
			*/
			for(Element row : skiRunsDoc.select("table tr")) {
				//headings.add(row.select("th:is(.ski-run, .center.is-active, .ice-sheet, .length, .center.snow, .center.light").text());
				
				if(row.select("td.difficulty-level").text().equals("")) {
					continue;
				}else {
					
					skiRunsList.add(row.select("td.ski-run p span").text());
					difficultyLevel.add(row.select("td.difficulty-level").text());
					isActive.add(row.select("td.center.is-active span:is(.ico-tak, .ico-nie)").attr("title"));					
					iceSheet.add(row.select("td.ice-sheet").text());
					skiLength.add(row.select("td.length").text());
					snow.add(row.select("td.center.snow span:is(.ico-tak, .ico-nie)").attr("title"));
					light.add(row.select("td.center.light span:is(.ico-tak, .ico-nie)").attr("title"));
					
					skiRunsArrayList.add(new String[] {
							row.select("td.ski-run p span").text(),
							row.select("td.difficulty-level").text(),
							row.select("td.center.is-active span:is(.ico-tak, .ico-nie)").attr("title"),
							row.select("td.ice-sheet").text(),
							row.select("td.length").text(),
							row.select("td.center.snow span:is(.ico-tak, .ico-nie)").attr("title"),
							row.select("td.center.light span:is(.ico-tak, .ico-nie)").attr("title")
					});
				}
			}
			
		}catch(IOException e) {
			logger.error("Error while fetching ski runs data: {}", e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		logger.info("Ski Runs Data: ");
		for(String[] array : skiRunsArrayList) {
			for(String elem : array) {
				//System.out.print(elem + " ");
				logger.debug(elem + " ");
			}
			//System.out.println();
			//logger.debug("");
		}
	}
}
