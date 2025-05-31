# Quest-Game-Cucumber

Quest-Game-Cucumber is an automated test suite designed to verify the end-to-end functionality of the Quest Game application using Cucumber. 

The suite covers critical user flows, simulates real player actions, and validates key features to ensure a reliable gaming experience.

## Key Features

* Automated UI Testing for Core Game Flows using Gherkin Scenarios
* Scenario-Based Acceptance Tests with Cucumber
* Integration with Continuous Integration Tools

## Skills Learned

### Iterative Development
* **Hotseat Approach**: Developed tests that simulate real multi-user scenarios, ensuring the game logic holds in shared or sequential play environments.
* **Web-Based Automation**: Transitioned from manual to fully automated testing, demonstrating adaptability and code maintainability.

### Behavior-Driven Development (BDD)
* **Responsibility Identification**: Broke down user stories into granular Gherkin feature files, ensuring full coverage of functional requirements.
* **Commit Structure**: Used a systematic approach to match every tested scenario with corresponding automation code.

### Acceptance Testing
* **Scenario Path Identification**: Mapped out and automated full user journeys through the Quest Game, emphasizing critical acceptance criteria.

### UI Automation and Design
* **Decoupling Test Logic and Data**: Architected tests to separate scenario data from automation logic, enabling easier updates and scenario expansion.
* **Robust Locator Strategies**: Implemented maintainable element locators and validation strategies to withstand UI changes.

### Refactoring
* **Code Improvement**: Regularly improved test structure, removed duplication, and enhanced readability.
* **Commit Management**: Used clear commit messages to indicate improvements and refactorings.

### Project Management
* **Repository Organization**: Structured the test repository with clear directory layouts, naming conventions, and descriptive commit messages.

## Setup and Run

1.  Clone the repository:  
    `git clone https://github.com/cupcakequeen77777/Quest-Game-Cucumber.git`
2.  Navigate to the project directory:  
    `cd Quest-Game-Cucumber`
3.  Install dependencies (ensure Java, Maven/Gradle, and browser drivers are installed).
4.  Run the test suite:  
    - If using Maven:  
      `mvn test`
    - If using Gradle:  
      `gradle test`

### Example/Demo

To see the automated tests in action, run the suite using your build tool (e.g., `mvn test`). The tests will launch the browser, execute user flows, and display results in the console or in the generated reports.

### Testing Instructions

1.  Ensure all dependencies are installed (Java, Maven/Gradle, browser drivers).
2.  Execute the test suite:  
    - Maven: `mvn test`  
    - Gradle: `gradle test`
3.  Review test results in the console output or in the generated HTML/XML reports (if configured).
