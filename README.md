# Ski Resorts Application

This Java application provides information about ski resorts, including weather data, ski runs, and ski lifts. It features a graphical user interface (GUI) built using Swing. The application utilizes web scraping with Jsoup to fetch data from specific URLs.

## Project Structure

The project is organized into several classes in one package main.java.skiresorts, each serving a specific purpose:

### 1. `ResortsList.java`

## Overview

- **Main Class:** Represents the Ski Resorts application.
- **Initialization:** Initializes the main frame, GUI components (title panel and list panel), and utilizes Swing components (JFrame, JLabel, JButton).
- **Event Handling:** Implements an ActionListener to manage button click events.
- **Internet Connectivity:** Utilizes the InternetConnectionChecker class for performing internet connection checks.
- **Logging:** Implements Log4j for logging events and errors.
- **Dynamic UI:** Displays ski resorts as buttons with dynamic appearance adjustments on mouse hover.
- **Detailed Information:** Opens detailed information about the selected ski resort using other classes (e.g., Kotelnica, Korbielow).
- **Error Handling:** Provides error handling for scenarios with no internet connection.

## Methods

### `ResortsList()`
Constructor for the ResortsList class. Initializes the main frame and sets up the GUI components.

### `initFrame()`
Initializes the main frame of the application.

### `initTitlePanel()`
Initializes the title panel of the application.

### `initListPanel()`
Initializes the list panel displaying ski resorts.

### `actionPerformed(ActionEvent e)`
Handles button click events and performs actions based on the selected resort.

### 2. `Korbielow.java`

- The Korbielow class represents a GUI application for displaying ski resort information, including weather data, ski runs, and ski lifts.
- Initializes the GUI frame and components, fetching ski resort data, and displaying the information.
- Uses Jsoup for web scraping to retrieve weather data, ski runs information, and ski lifts data from specific URLs.
- Utilizes Swing components, including JFrame, JPanel, JLabel, and JButton, to create an interactive user interface.
- Implements a multi-threaded approach using ExecutorService for concurrent fetching of weather data, ski runs, and ski lifts.
- Displays ski resort data, including ski runs with information such as status, name, conditions, snow range, length, and elevation difference.
- Presents ski lift data, including status, name, and type, through a dialog.
- Generates a bar chart using JFreeChart to visualize the lengths of ski runs and displays it in a separate dialog.
- Provides error handling for web scraping issues, logging errors using Log4j, and displaying appropriate error dialogs.

### 3. `Kotelnica.java`
- Represents a graphical user interface (GUI) application designed to present information about the ski resort.
- Initializes the main frame, logo panel, main content panel, and additional control buttons at the bottom of the window.
- Utilizes the Jsoup library for web scraping to gather essential data about weather conditions and ski runs from specific URLs.
- Fetches data such as wind speed, temperature, ski run details (name, difficulty, status, ice sheet, length, etc.).
- Implements Swing components, including JFrame, JPanel, JLabel, JButton, and JTable, to create an interactive and visually appealing user interface.
- Implements a multi-threaded approach using ExecutorService to concurrently fetch weather data, ski runs information, and ski lifts data, ensuring efficiency in data retrieval.
- Displays weather conditions on the left side of the GUI, including details like wind speed, temperature, and other relevant data.
- Presents ski runs information on the right side using a JTable, offering details such as ski run name, difficulty level, status, ice sheet, length, snow conditions, and lighting status.
- Generates a bar chart using JFreeChart to visually represent the lengths of ski runs based on their difficulty levels.
- Presents the bar chart in a separate dialog for enhanced visualization of ski run lengths.
- Incorporates error handling mechanisms to address potential issues during web scraping.
- Utilizes Log4j for logging events, capturing errors, and providing comprehensive logs for debugging purposes.
### 4. `ErrorHandler.java`

- Handles errors within the application, such as web scraping error and no internet connection error.
- Provides methods to handle errors and notify users about issues.
- Prompts the user to close the current window upon a web scraping error.

### 5. `InternetConnectionChecker.java`

- Utility class to check the availability of an internet connection.
- Uses `InetAddress` to attempt to reach www.google.com and determine internet connectivity.

## Usage

To run the application, create an instance of the `SkiResorts.java` class. Ensure that the required dependencies are available in your project.

## Dependencies

- Jsoup: Java HTML Parser library for web scraping.
- JFreeChart: Open-source library for creating charts.
- Log4j: Logging utility for Java.

## Project Configuration

### Resources Folder

The `resources` folder contains necessary images used in the application.

### Logs Folder

The `logs` folder stores log files generated by Log4j.

### Log4j Configuration

The `log4j2.xml` file in the `resources` folder configures Log4j appenders and log levels for different parts of the application.
