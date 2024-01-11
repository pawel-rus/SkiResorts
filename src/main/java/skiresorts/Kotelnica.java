package main.java.skiresorts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.renderer.category.BarRenderer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.MediaTracker;
import java.awt.Paint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 * Class representing the details page for the "Kotelnica Białczańska" ski resort.
 */
public class Kotelnica {
    // Logger for logging events specific to Kotelnica class
    private static Logger logger = LogManager.getLogger(Kotelnica.class);
    // URL for fetching current weather conditions
    final String conditionsUrl = "https://bialkatatrzanska.pl/pogoda/aktualne-warunki-pogodowe";
    // URL for fetching ski runs information
    final String skiRunsUrl = "https://bialkatatrzanska.pl/warunki-narciarskie";
    // JFrame for the Kotelnica details window
	private JFrame frame = new JFrame("Kotelnica Białczańska");
    // JPanel for displaying the resort's logo
	private JPanel logoPanel  = new JPanel();
    // JPanel for the main content of the details window
	private JPanel mainPanel  = new JPanel();
    // JPanel for additional controls at the bottom of the details window
	private JPanel bottomPanel = new JPanel();
    // JTable for displaying ski runs information
	private JTable rightTable;
	private Color myColor = new Color(255, 255, 255);
	// HashMap to store weather conditions and their values
	private HashMap<String, String> weatherDataMap = new LinkedHashMap<>();
	List<String[]> skiRunsArrayList = new ArrayList<>();
	// Lists to store ski runs information
	List<String> skiRunsList = new ArrayList<>();
	List<String> difficultyLevel = new ArrayList<>();
	List<String> isActive = new ArrayList<>();
	List<String> iceSheet = new ArrayList<>();
	List<String> skiLength = new ArrayList<>();
	List<String> snow = new ArrayList<>();
	List<String> light = new ArrayList<>();
	/**
     * Constructor for the Kotelnica class. 
     * Initializes the frame and sets up the GUI components.
     */
	Kotelnica(){
		initFrame();
		initLogoPanel();
		initMainPanel();
		initBottomPanel();
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		logger.info("Kotelnica initialized.");
	}
	/**
     * Initializes the main frame of the Kotelnica details window.
     */
	void initFrame() {
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setBackground(myColor);
		frame.setLayout(new BorderLayout());
	}
	
	/**
     * Initializes the main content panel of the Kotelnica details window.
     */
	private void initMainPanel() {
		webScrapper();
		
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(myColor);
		JPanel leftColumn = new JPanel(new GridLayout(weatherDataMap.size() + 1, 2));
		leftColumn.setBackground(myColor);
		//JPanel rightColumn = new JPanel(new GridLayout(skiRunsArrayList.size() + 1,skiRunsArrayList.get(0).length));
		
		leftColumn.add(new JLabel("WARUNKI POGODOWE"));
	    leftColumn.add(new JLabel(""));
	    
		for(String condition : weatherDataMap.keySet()) {
			String value = weatherDataMap.get(condition);
			
			JLabel conditionLabel = new JLabel(condition);
			JLabel valueLabel = new JLabel(value);
			conditionLabel.setBackground(myColor);
			valueLabel.setBackground(myColor);
			leftColumn.add(conditionLabel);
			leftColumn.add(valueLabel);
		}
		
		String[][] rightTableData = skiRunsArrayList.toArray(new String[skiRunsArrayList.size()][]);
		String[] columnNames = {"Trasa", "Trudność", "Czynna", "Pokrywa śnieżna", "Długość", "Naśnieżanie", "Oświetlenie"};
		
		rightTable = new JTable(rightTableData, columnNames);
		
        // Code for centering text in JTable cells
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		for (int i = 0; i < rightTable.getColumnCount(); i++) {
		    rightTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		// Code for adjusting column widths in JTable
		TableColumnModel columnModel = rightTable.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(60);
		columnModel.getColumn(1).setPreferredWidth(120);
		columnModel.getColumn(2).setPreferredWidth(60);
		columnModel.getColumn(3).setPreferredWidth(100);
		columnModel.getColumn(4).setPreferredWidth(80);
		columnModel.getColumn(5).setPreferredWidth(100);
		columnModel.getColumn(6).setPreferredWidth(100);
		rightTable.setPreferredScrollableViewportSize(new Dimension(620, 310)); 
		
		//rightTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JScrollPane scrollPane = new JScrollPane(rightTable);
		scrollPane.setBackground(myColor);
	    /*
		for(String[] array : skiRunsArrayList) {
			for(String elem : array) {
				rightColumn.add(new JLabel(elem));
			}
		}
		*/
		
		mainPanel.add(leftColumn, BorderLayout.WEST);
		mainPanel.add(scrollPane, BorderLayout.EAST);
		
		frame.add(mainPanel, BorderLayout.CENTER);
	}
	
	/**
     * Initializes the panel for displaying the resort logo.
     */
	void initLogoPanel(){
	
		logoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		try {
			JLabel imageLabel = new JLabel();
			ImageIcon icon = new ImageIcon("resources/kotelnica.png");
			if (icon.getImageLoadStatus() == MediaTracker.ERRORED) {
		        throw new IOException("Can't find this file.");
		    }
			imageLabel.setIcon(icon);
			logoPanel.add(imageLabel);
		}catch (Exception e) {
			logger.error("Error while loading logo: {} ", e.getMessage());
			
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
	
	/**
     * Initializes the panel for additional buttons at the bottom of the details window.
     */
	private void initBottomPanel(){
		JButton pieChart = new JButton("Wykres kołowy");
		pieChart.addActionListener(e -> showPieChart());
		JButton barChart = new JButton("Wykres słupkowy");
		barChart.addActionListener(e -> showBarChart());
		
		bottomPanel.setLayout(new GridLayout(1,2));
        bottomPanel.setBackground(myColor);
	    bottomPanel.add(pieChart);
		bottomPanel.add(barChart);
		
		frame.add(bottomPanel, BorderLayout.SOUTH);
	}
	
	
	/**
	 * Displays a pie chart showing the distribution of ski runs by difficulty level.
	 */
	private void showPieChart() {
		logger.info("Activate showPieChart() method.");

        int hard = 0, easy = 0, veryEasy = 0, other = 0;
        for (String difficulty : difficultyLevel) {
            if (difficulty.equals("trudna")) {
                hard++;
            } else if (difficulty.equals("łatwa")) {
                easy++;
            } else if (difficulty.equals("bardzo łatwa")) {
                veryEasy++;
            } else {
                other++;
            }
        }
        // Create a dataset for the pie chart
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Trudna", hard);
        dataset.setValue("Łatwa", easy);
        dataset.setValue("Bardzo łatwa", veryEasy);
        dataset.setValue("Inne", other);
        
        // Create a pie chart
        JFreeChart pieChart = ChartFactory.createPieChart(
                "Rozkład Tras Narciarskich",
                dataset,
                true,
                true,
                false
        );
        // Customize pie chart plot
        PiePlot plot = (PiePlot) pieChart.getPlot();
        plot.setSectionPaint("Łatwe", Color.GREEN);
        plot.setSectionPaint("Trudne", Color.RED);
        plot.setSectionPaint("Inne", Color.BLUE);
        
        // Create a chart panel for displaying the pie chart
        ChartPanel chartPanel = new ChartPanel(pieChart);
        chartPanel.setBackground(myColor);
        // Display the pie chart in a dialog box
        JOptionPane.showMessageDialog(null, chartPanel, "Wykres kołowy", JOptionPane.PLAIN_MESSAGE);
    }
	
	/**
	 * Displays a bar chart showing the lengths of ski runs based on difficulty level.
	 */
	private void showBarChart() {
		logger.info("Activate showBarChart() method.");
        JFreeChart barChart = ChartFactory.createBarChart(
                "Długość tras narciarskich",
                "Trasa",
                "Długość (m)",
                new DefaultCategoryDataset(),
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );
        
        // Create a chart panel for displaying the bar chart
        ChartPanel barChartPanel = new ChartPanel(barChart);
        barChartPanel.setBackground(myColor);

        // Access the category plot for customizing
        CategoryPlot categoryPlot = barChart.getCategoryPlot();
        
        // Create a dataset for the bar chart
        DefaultCategoryDataset barDataset = (DefaultCategoryDataset) categoryPlot.getDataset();

        // Iterate through ski runs and add data to the dataset
        for (int i = 0; i < skiRunsArrayList.size(); i++) {
            String skiRunName = skiRunsArrayList.get(i)[0];
            int skiRunLength;

            try {
                skiRunLength = Integer.parseInt(skiRunsArrayList.get(i)[4].replaceAll("[^\\d.]", ""));
            } catch (NumberFormatException e) {
                logger.error("Error while parsing ski-run length", skiRunName, e.getMessage());

                skiRunLength = 0;
            }
            // Get difficulty level and set corresponding color
            String difficulty = difficultyLevel.get(i);
            Paint color;
            switch (difficulty.toLowerCase()) {
                case "trudna":
                    color = Color.RED;
                    break;
                case "łatwa":
                    color = Color.BLUE;
                    break;
                case "bardzo łatwa":
                    color = Color.GREEN;
                    break;
                    
                default:
                    color = Color.YELLOW;
            }

            // Unique series key for each ski run
            String seriesKey = skiRunName + " - " + difficulty;
            // Add data to the bar dataset and set bar color
            barDataset.addValue(skiRunLength, seriesKey, skiRunName);
            categoryPlot.getRenderer().setSeriesPaint(i, color);
        }
        // Customize category axis and renderer for the bar chart
        CategoryAxis categoryAxis = categoryPlot.getDomainAxis();
        categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
        //set width of the bars
        BarRenderer renderer = (BarRenderer) barChart.getCategoryPlot().getRenderer();
        renderer.setItemMargin(-20);
        // Display the bar chart in a dialog box
        JOptionPane.showMessageDialog(null, barChartPanel, "Wykres słupkowy", JOptionPane.PLAIN_MESSAGE);
    }
	
	/**
     * Scrapes data from the web pages to populate weather and ski runs information.
     */
	void webScrapper() {
        // Code for scraping weather data
		try {
			final Document document = Jsoup.connect(conditionsUrl).timeout(4000).get();
			logger.info("Activate web scrapper for weather data.");
			String condition;
			String value;
			for(Element row : document.select("table#weather-data-current tr")) {
				if(row.select("td.name").text().equals("")) {
					continue;
				} else {
					condition = row.select("td.name").text();
					value = row.select("td.value").text();
					if(condition.equals("Wiatr")) {
						value = value.replaceAll("\\s.*", "");
					}

					weatherDataMap.put(condition, value);
					System.out.println(condition + " : " + value);
					//logger.debug("Weather Condition: {} - Value: {}", condition, value);
				}
			}
		} catch (IOException e){
			logger.error("Error while fetching weather data: {}", e.getMessage());
			e.printStackTrace();
			ErrorHandler webScrapperError = new ErrorHandler();
	        webScrapperError.handleWebScrapperError(frame);
	        logger.error("Method handleWebScrapperError() called.");
		}
		
        // Code for scraping ski runs data
		try {
			final Document skiRunsDoc = Jsoup.connect(skiRunsUrl).timeout(4000).get();
			logger.info("Activate web scrapper for ski runs data.");
			
			for(Element row : skiRunsDoc.select("table tr")) {
				
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
			ErrorHandler webScrapperError = new ErrorHandler();
	        webScrapperError.handleWebScrapperError(frame);
	        logger.error("Method handleWebScrapperError() called.");
		}
		logger.info("Ski Runs Data Scrapped succesfully.");
		for(String[] array : skiRunsArrayList) {
			for(String elem : array) {
				System.out.print(elem + " ");
				//logger.debug(elem + " ");
			}
			System.out.println();
			//logger.debug("");
		}
	}
}
