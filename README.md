# JEEApp

The **JEEApp** is a Java Swing application designed to help JEE aspirants maintain a balanced approach to studying and health tracking. With dedicated pages for health data, subject progress, test scores, and an overview dashboard, this app aims to motivate students by providing an overall performance score and reward system.
This project was created as part of my college Java Swing project to demonstrate practical application of Java GUI programming.

## Features

### 1. Health Tracker
- Track daily health metrics including:
  - Sleep (in hours)
  - Nutrition (calories)
  - Exercise (minutes)
  - Meditation/Yoga (minutes)
  - Caffeine intake (mg)
- Health data is stored in a MySQL database and contributes to a **Health Score**.

### 2. Subject Tracking
- Monitor progress on key topics across Physics, Chemistry, and Mathematics.
- Mark topics as completed, and view progress in real-time.
- Subject data is saved to the database for future tracking.

### 3. Test Scores
- Input test scores for each subject (Physics, Chemistry, Mathematics).
- View an average score calculated from your test performance.

### 4. Dashboard
- Get an overview of:
  - Health Score (out of 100)
  - Topics Score (out of 100 based on topic completion)
  - Average Test Score (out of 100)
  - Overall Score combining health, topic, and test scores
  - Reward Status based on overall performance:
    - **Gold** (90+)
    - **Silver** (75+)
    - **Bronze** (50+)
    - **Motivation** for continuous improvement
- Reward system encourages consistency and motivates balanced habits.

## Technologies Used

- **Java Swing** for GUI development
- **MySQL** database to store health, subject, and test score data
- **JDBC** for database connectivity
- **MVC Architecture** for organized code structure

## Setup and Installation

### Prerequisites
- Java Development Kit (JDK) 11+
- MySQL Database
- JDBC Driver for MySQL

### Running the Application
1. Clone this repository and open the project in your preferred Java IDE (e.g., VSCode, IntelliJ).
2. Update `DatabaseConnection.java` with your MySQL connection details (URL, username, password).
3. Setup the database.
4. Run the `JeeStudyTrackerApp.java` file to launch the application.

