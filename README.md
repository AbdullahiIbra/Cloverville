# ☘ Cloverville

A community management system for a sustainable co-living village, built as a first-semester group project for the Software Technology Engineering programme.

## About the Project

Cloverville is a desktop and web application designed to help manage a small eco-friendly community. Residents earn points by completing communal tasks and green actions, and can trade items or services with each other using those points. An admin dashboard allows the village coordinator to manage residents, assign tasks, and track community progress.

The project was developed by **Group 7** as part of our first-semester coursework.

## Features

- **Resident Management** — Add, edit, and remove village residents with personal details and point balances
- **Communal Tasks** — Create and assign shared tasks (cleaning, inventory checks, etc.) that reward residents with points upon completion
- **Green Actions** — Track eco-friendly activities like biking to school, taking short showers, or having meat-free days, contributing to a community-wide sustainability score
- **Trade Offers** — A marketplace where residents can post items or services for sale using points (buyer pays, seller receives)
- **Points System** — Personal points are earned through tasks and spent through trades; community points accumulate through green actions
- **Data Persistence** — All village data is saved to and loaded from a local JSON file
- **Community Website** — A responsive informational website showcasing the village, its residents, trade offers, communal tasks, and CO₂ savings

## Tech Stack

- **Language:** Java 21
- **UI Framework:** JavaFX 21 with FXML
- **Build Tool:** Apache Maven
- **Website:** HTML, CSS, JavaScript
- **Data Storage:** JSON (custom file-based read/write)
- **IDE:** IntelliJ IDEA

## Project Structure

Cloverville/
├── src/main/java/com/example/cloverville/
│   ├── Main.java
│   ├── controllers/
│   │   ├── AdminDesktopController.java
│   │   ├── ResidentController.java
│   │   ├── CommunalTaskController.java
│   │   ├── GreenActionController.java
│   │   ├── TradeOfferController.java
│   │   └── PointsController.java
│   └── models/
│       ├── Village.java
│       ├── Resident.java
│       ├── CommunalTask.java
│       ├── GreenAction.java
│       ├── TradeOffer.java
│       ├── PersonalPointTransition.java
│       ├── VillageFile.java
│       ├── MyDate.java
│       ├── ResidentList.java
│       ├── CommunalTaskList.java
│       ├── GreenActionList.java
│       └── TradeOfferList.java
├── src/main/resources/com/example/cloverville/
│   ├── AdminDesktop.fxml
│   ├── Resident.fxml
│   ├── CommunalTask.fxml
│   ├── GreenAction.fxml
│   ├── TradeOffer.fxml
│   └── Points.fxml
├── village.json
└── pom.xml

Cloverville Website/
├── index.html
├── about.html
├── communaltasks.html
├── tradeoffers.html
├── services.html
├── contact.html
├── script.js
├── styles/
└── images/

## How to Run

### Desktop Application

1. Make sure you have **Java 21** and **Maven** installed
2. Clone the repository:
   git clone https://github.com/AbdullahiIbra/Cloverville.git
3. Navigate to the project directory:
   cd Cloverville
4. Run the application:
   mvn clean javafx:run

### Website

Open `index.html` in any web browser — no server required.

## Design & Documentation

The project includes full software engineering documentation:

- Use case diagrams and descriptions
- Class diagrams and domain models
- Activity diagrams for all major operations
- Sequence diagrams
- Wireframes for phone, tablet, and notebook
- Test spreadsheet
- User manual
- Group contract and project schedule

## Group 7 Members

- Abdullahi
- Amir
- Dominika
- Jiashou
- Wenshi

## Course

Software Technology Engineering — 1st Semester (2025)
