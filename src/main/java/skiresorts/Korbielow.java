package main.java.skiresorts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import  java.util.concurrent.ExecutionException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * The Korbielow class represents a GUI application for displaying ski resort information,
 * including weather data, ski runs, and ski lifts.
 */
public class Korbielow {
    private static Logger logger = LogManager.getLogger(Korbielow.class);
    final String skiRunsUrl = "https://korbielow.net/komunikat-narciarski/";
    final String weatherUrl = "http://pilsko.pogoda.cc/";
    private JFrame frame = new JFrame("Korbielów");
	private JPanel logoPanel  = new JPanel();
	private JPanel mainPanel  = new JPanel();
	
	private Color myColor = new Color(255, 255, 255);
	private HashMap<String, String> weatherDataMap = new LinkedHashMap<>();
	List<String[]> skiRunsArrayList = new ArrayList<>();
	List<String[]> skiLiftsArrayList = new ArrayList<>();
	/**
     * Constructor of the Korbielow class.
     * Initializes the GUI frame and components, fetches ski resort data, and displays the information.
     */
	Korbielow(){
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setBackground(myColor);
		frame.setLayout(new BorderLayout());
		
		scrapeAllData();
		//scrapeWeatherData();
		//skiWebScrapper();
		//skiLiftsWebScrapper();
		
		initLogoPanel();
		initMainPanel();
		
		frame.add(logoPanel, BorderLayout.NORTH);
		frame.add(mainPanel, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		logger.info("Korbielów initialized.");
	}
	
	/**
     * Initializes the logo panel, including the resort image or name and weather data.
     */
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
		JPanel weatherPanel = new JPanel();
		weatherPanel.setBackground(myColor);
		weatherPanel.setLayout(new GridLayout(weatherDataMap.size(),2));
		logger.info("Adding Labels to weatherPanel");
		for(String condition : weatherDataMap.keySet()) {
			logger.debug("Adding Labels to weatherPanel - Condition: {}, Value: {}", condition, weatherDataMap.get(condition));
			weatherPanel.add(new JLabel(condition));
			weatherPanel.add(new JLabel(weatherDataMap.get(condition)));
		}
	    logoPanel.add(weatherPanel, BorderLayout.CENTER);
	    
	}
	
	/**
     * Initializes the main panel, including a table of ski runs and buttons for displaying ski lifts data and charts.
     */
	private void initMainPanel() {
		
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
	                String filePath = "resources/" + fileName;
	                File file = new File(filePath);
	                if(file.exists()) {
		                try {
		                    ImageIcon icon = new ImageIcon(filePath);
		                    label = new JLabel(icon);
		                } catch (Exception e) {
		                    logger.error("IOException while loading image: {}", e.getMessage());
		                    label = new JLabel("error");
		                }	
	                }else {
	                    logger.error("File doesn't exist: {}", filePath);
	                    index = fileName.lastIndexOf(".");
	                    fileName = fileName.substring(0, index);
	                    label = new JLabel(fileName);
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
	    mainPanel.add(scrollPane,BorderLayout.NORTH);
	    
        // Adding buttons to the main panel for displaying ski lifts data and charts
	    JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(myColor);
        buttonsPanel.setLayout(new GridLayout(1,2));

        JButton showLiftsButton = new JButton("Pokaż dane o wyciągach");
        showLiftsButton.addActionListener(e -> showLiftsDataDialog());
        
        JButton showChartButton = new JButton("Pokaż wykres");
        showChartButton.addActionListener(e -> showSkiRunsChart());
        
        buttonsPanel.add(showLiftsButton);
		buttonsPanel.add(showChartButton);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
	    
	}
	
	/**
     * Fetches all data including weather data, ski runs, and ski lifts using multiple threads.
     */
	private void scrapeAllData() {
        logger.info("Activate scrapeAllData() method.");

	    try(ExecutorService executorService = Executors.newFixedThreadPool(3)) {
	        // create a thread pool
	    	logger.info("Creating a new thread pool.");
	        Future<Void> weatherDataFuture = executorService.submit(() -> {
	            scrapeWeatherData();
	            return null;
	        });

	        Future<Void> skiRunsFuture = executorService.submit(() -> {
	            skiWebScrapper();
	            return null;
	        });

	        Future<Void> skiLiftsFuture = executorService.submit(() -> {
	            skiLiftsWebScrapper();
	            return null;
	        });

	        // wait for all threads to finish
	        weatherDataFuture.get();
	        skiRunsFuture.get();
	        skiLiftsFuture.get();
	        logger.info("Scrapping all data finished.");
	    } catch (InterruptedException | ExecutionException e) {
	        logger.error("Error while fetching data: " + e.getMessage());
	        ErrorHandler webScrapperError = new ErrorHandler();
	        webScrapperError.handleWebScrapperError(frame);
	        logger.error("Method handleWebScrapperError() called.");

	        e.printStackTrace();
	    } 
	}

	/**
	 * Scrapes weather data from the specified URL using Jsoup.
	 * Parses the HTML document to extract condition-value pairs,
	 * @throws IOException if an error occurs while fetching or parsing the data.
	 */
	private void scrapeWeatherData() throws IOException {
	    try {
	        // Connect to the weather URL and fetch the document
	        final Document weatherDoc = Jsoup.connect(weatherUrl).timeout(4000).get();
	        logger.info("Activate web scraper for weather data.");
	        
	        // Select the rows containing weather data from the document
	        Elements rows = weatherDoc.select("table.data tbody tr");
	        String condition;
	        String value;
	        // Iterate through the rows and extract condition-value pairs
            for (Element row : rows) {
                condition = row.select(".table_caption").text();
                value = row.select(".table_value").text();
                weatherDataMap.put(condition, value);
                logger.debug("Weather Data: {} - {}", condition, value);
            }
	        
	    } catch (IOException e) {
	        // Handle IOException by logging the error, printing the stack trace, and re-throwing the exception
	        logger.error("Error while fetching weather data: " + e.getMessage());
	        e.printStackTrace();
	        throw e;
	    }
	}

	/**
	 * Scrapes ski runs data from the specified URL using Jsoup.
	 * Parses the HTML document to extract information about each ski run,
	 * @throws IOException if an error occurs while fetching or parsing the data.
	 */
	private void skiWebScrapper() throws IOException {
        try {
            // Connect to the ski runs URL and fetch the document
            final Document skiRunsDoc = Jsoup.connect(skiRunsUrl).timeout(4000).get();
            System.out.println("Activate web scrapper for ski runs data.");
            // Select the rows containing ski runs data from the document
            Elements rows = skiRunsDoc.select("table.tg tbody tr");
            for (Element row : rows) {
                Elements cells = row.select("td");

                // Skip header row and consider only rows with sufficient cells
                if (cells.size() >=6 ) {
                    String status = cells.get(0).select("img").attr("src");
                    
                    // Proceed only if the status is not empty
                    if (!status.isEmpty()) {
                        String name = cells.get(1).text();
                        String conditions = cells.get(2).select("img").attr("src");
                        String snowRange = cells.get(3).text();
                        String length = cells.get(4).text();
                        String elevationDifference = cells.get(5).text();
                        
                        // Adjust ski run name if it contains parentheses
                        if (name.contains("(")) {
                            int index = name.indexOf("(");
                            name = name.substring(0, index);
                        }
                        
                        //add ski run informations to skiRunsArrayList
                        skiRunsArrayList.add(new String[] {
                        		status,
    							name,
    							conditions,
    							snowRange,
    							length,
    							elevationDifference
    					});
                        
                        // Log details of each ski run
                        logger.debug("Status: {}", status);
                        logger.debug("Name and Difficulty: {}", name);
                        logger.debug("Conditions: {}", conditions);
                        logger.debug("Snow Range: {}", snowRange);
                        logger.debug("Length: {}", length);
                        logger.debug("Elevation Difference: {}", elevationDifference);
                        logger.debug("-----------------------");
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Error while fetching ski runs data: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
	
	/**
	 * Scrapes ski lifts data from the specified URL using Jsoup.
	 * Parses the HTML document to extract information about each ski lift,
	 * @throws IOException if an error occurs while fetching or parsing the data.
	 */
	private void skiLiftsWebScrapper() throws IOException {
	    try {
	        // Connect to the URL and fetch the document
	        final Document skiLiftsDoc = Jsoup.connect(skiRunsUrl).timeout(4000).get();
	        System.out.println("Activate web scrapper for ski lifts data.");
	        
	        // Select the rows containing ski lifts data from the document
	        Elements rows = skiLiftsDoc.select("table.tg tbody tr");
	        for (Element row : rows) {
	            Elements cells = row.select("td");

	            if (cells.size() == 3) {
	                String status = cells.get(0).select("img").attr("src");
	                
	                // Proceed only if the status is not empty
	                if (!status.isEmpty()) {
	                    String liftName = cells.get(1).text();
	                    String liftType = cells.get(2).text();
	                    
	                    // add ski lift information to skiLiftsArrayList
	                    skiLiftsArrayList.add(new String[]{
	                            status,
	                            liftName,
	                            liftType
	                    });

	                    logger.debug("Status: {}", status);
                        logger.debug("Lift Name: {}", liftName);
                        logger.debug("Lift Type: {}", liftType);
                        logger.debug("-----------------------");
	                }
	            }
	        }
	    } catch (IOException e) {
	        // Handle IOException by logging the error, printing the stack trace, and re-throwing the exception
	        logger.error("Error while fetching ski lifts data: " + e.getMessage());
	        e.printStackTrace();
	        throw e;
	    }
	}
	
	/**
     * Displays a dialog with information about ski lifts, including status, name, and type.
     */
	private void showLiftsDataDialog() {
        logger.info("Activate showLiftsDataDialog() method.");

	    JPanel panel = new JPanel();
	    panel.setLayout(new GridLayout(0, 1));

	    for (String[] liftData : skiLiftsArrayList) {
	    	JLabel statusImageLabel;
	        int index = liftData[0].lastIndexOf("/");
	        String fileName = liftData[0].substring(index + 1);
            String filePath = "resources/" + fileName;
            File file = new File(filePath);
            //checking if img exist
            if(file.exists()) {
                try {
                    ImageIcon statusIcon = new ImageIcon(filePath);
                    statusImageLabel = new JLabel(statusIcon);
                } catch (Exception e) {
                    logger.error("IOException while loading image: {}", e.getMessage());
                    statusImageLabel = new JLabel(fileName);
                }	
            }else {
                logger.error("File doesn't exist: {}", filePath);
                index = fileName.lastIndexOf(".");
                fileName = fileName.substring(0, index);
                statusImageLabel = new JLabel(fileName);
                statusImageLabel.setHorizontalAlignment(JLabel.CENTER);
            }
	        panel.add(statusImageLabel);
	        
	        JLabel liftNameValueLabel = new JLabel(liftData[1]);
	        liftNameValueLabel.setHorizontalAlignment(JLabel.CENTER);
	        panel.add(liftNameValueLabel);

	        JLabel liftTypeValueLabel = new JLabel(liftData[2]);
	        liftTypeValueLabel.setHorizontalAlignment(JLabel.CENTER);

	        panel.add(liftTypeValueLabel);
	    }

	    JScrollPane scrollPane = new JScrollPane(panel);
	    
	    JOptionPane.showMessageDialog(null, scrollPane, "Dane o wyciągach narciarskich", JOptionPane.PLAIN_MESSAGE);
	}

	/**
     * Displays a bar chart showing the lengths of ski runs.
     */
	private void showSkiRunsChart() {
        logger.info("Activate showSkiRunsChart() method.");

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (String[] skiRun : skiRunsArrayList) {
            String name = skiRun[1];
            double length = Double.parseDouble(skiRun[4].replaceAll("[^\\d.]", "")); // delete non-numbers
            dataset.addValue(length, "Długość tras narciarskich", name);
        }
        // create
        JFreeChart chart = ChartFactory.createBarChart(
                "Długość tras narciarskich",
                "Trasy narciarskie",
                "Długość (m)",
                dataset
        );
        
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        CategoryAxis categoryAxis = plot.getDomainAxis();
        categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(900, 600));

        JOptionPane.showMessageDialog(null, chartPanel, "Wykres", JOptionPane.PLAIN_MESSAGE);
    }
	
}
