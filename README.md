# Amazon Project - Cucumber BDD Automation Framework

## About the Project

**Amazon Project Cucumber** is a comprehensive **Behavior-Driven Development (BDD)** automation framework designed to test critical user workflows on the Amazon Egypt e-commerce platform. This framework automates end-to-end testing of product search, filtering, product details verification, and shopping cart management.

### Business Problem Solved
Manual testing of complex e-commerce workflows is time-consuming, error-prone, and difficult to scale. This framework enables:
- **Rapid test execution** across multiple browsers (Chrome, Firefox, Edge)
- **Readable test specifications** written in plain English (Gherkin language)
- **Easy maintenance** through modular Page Object design
- **Continuous integration** via Maven and TestNG
- **Detailed reporting** with Allure and HTML reports

### What Is Automated
1. **Product Search** — searching for products, validating results
2. **Search Results Management** — filtering by brand, price range, sorting by various criteria
3. **Product Details Verification** — validating product information, images, pricing, availability
4. **Shopping Cart Operations** — adding items, removing items, quantity updates, subtotal calculations, inventory limits

### Target Users
- **QC Automation Engineers** — responsible for maintaining and extending tests
- **QC Manual Testers** — transitioning to automation; can read Gherkin scenarios without programming knowledge
- **DevOps Engineers** — integrating tests into CI/CD pipelines
- **Development Teams** — understanding acceptance criteria through executable specifications

### Key Benefits
✅ **Readable Specifications** — non-technical stakeholders can understand test scenarios  
✅ **Maintainable Code** — Page Object Model reduces code duplication and brittleness  
✅ **Reusable Components** — utility classes, hooks, and helpers maximize productivity  
✅ **Multi-Browser Support** — run tests on Chrome, Firefox, and Edge  
✅ **Rich Reporting** — Allure and HTML reports provide insights into test execution  
✅ **Automatic Screenshots** — failures are captured for root-cause analysis

---

## What is BDD & Cucumber?

### Understanding BDD (Behavior-Driven Development)

**BDD** is a software development approach that bridges the gap between business stakeholders, QA, and developers by writing tests in plain English. Instead of writing cryptic test code, BDD focuses on **describing what the application should do** from the user's perspective.

#### BDD vs. Traditional Testing

| Aspect | Traditional Automation | BDD Approach |
|--------|------------------------|--------------|
| **Who writes tests** | Only QA engineers | Business analysts, QA, Developers |
| **Test readability** | Technical code (hard to read) | Plain English (easy to read) |
| **Documentation** | Separate test plans + code | Tests ARE the documentation |
| **Maintenance** | Requires code knowledge | Business users understand requirements |

#### Real-World Example from This Project

**Traditional Test Code:**
```java
// Hard to understand without technical knowledge
searchBox.sendKeys("Mouse");
searchButton.click();
List<WebElement> results = driver.findElements(By.xpath("//div[@data-component-type='s-search-result']"));
Assert.assertTrue(results.size() > 0);
```

**BDD Approach (Gherkin):**
```gherkin
Feature: Product Search Functionality
  Scenario: User searches for a valid product
    Given I am on the Amazon Egypt homepage
    When I search for a product "Mouse"
    Then the search results page title should contain "Mouse"
    And the system should display at least one search result
```

The Gherkin version is **self-documenting**—anyone can understand it.

---

### Understanding Cucumber

**Cucumber** is a tool that executes Gherkin specifications written in plain English. It acts as a **translator** between:
- **Feature files** (Gherkin — plain English test specifications)
- **Step definitions** (Java code — the actual test logic)
- **Test runners** (TestNG — executes the tests)

#### How Cucumber Works: The Three Layers

```
┌─────────────────────────────────────────┐
│  Feature File (Gherkin)                 │
│  "Given I am on Amazon homepage"        │
└─────────────────────────────────────────┘
                    ↓
            Cucumber parses and matches
                    ↓
┌─────────────────────────────────────────┐
│  Step Definition (Java)                 │
│  @Given("I am on Amazon homepage")      │
│  public void iAmOnHomepage() { ... }    │
└─────────────────────────────────────────┘
                    ↓
          Step definition executes code
                    ↓
┌─────────────────────────────────────────┐
│  Selenium interacts with browser        │
│  driver.get(url);                       │
└─────────────────────────────────────────┘
```

---

### Understanding Gherkin Syntax

**Gherkin** is a simple, human-readable language for writing BDD tests. Here are the key keywords:

#### Feature
Describes what functionality is being tested (the file starts with this).
```gherkin
Feature: Shopping Cart Management
```

#### Scenario
A single test case describing a specific user behavior.
```gherkin
Scenario: User adds a product to cart
```

#### Given / When / Then (GWT Pattern)
- **Given** — preconditions (what state the system is in)
- **When** — actions (what the user does)
- **Then** — expected outcomes (what should happen)

```gherkin
Given I am on the Amazon Egypt homepage                    # Precondition
When I search for a product "Mouse"                        # Action
Then the search results page title should contain "Mouse"  # Outcome
```

#### And / But
Used to add more steps to Given, When, or Then without repetition.
```gherkin
Then the search results page title should contain "Mouse"
And the system should display at least one search result
And the header navigation bar should remain displayed
```

#### Scenario Outline + Examples
Used when you want to run the **same scenario with multiple data sets**.

**Example from this project:**
```gherkin
Scenario Outline: Verify search functionality and result validation
  Given I am on the Amazon Egypt homepage
  When I search for a product "<product_name>"
  Then the search results page title should contain "<product_name>"
  And the system should display at least one search result

  Examples:
    | product_name        |
    | search.validKeyword |
```

Here, `<product_name>` is replaced with `search.validKeyword` (which resolves to "Mouse" from the JSON config).

---

### Understanding Selenium WebDriver

**Selenium** is a tool that **automates browser interactions**. It controls real browsers (Chrome, Firefox, Edge) to:
- Navigate to URLs
- Click buttons and links
- Fill in text fields
- Extract data from pages
- Wait for elements to appear

**Example from this project:**
```java
public SearchResultsPage searchForProduct(String productName){
    writeText(searchField, productName);      // Fill search field
    clickOnElement(searchButton);            // Click search button
    return new SearchResultsPage(driver);   // Wait for results page
}
```

---

### Understanding TestNG

**TestNG** is a **test execution framework** (similar to JUnit). It:
- Runs tests in a structured way
- Supports test groups and filtering
- Generates test reports
- Works seamlessly with Cucumber

In this project, TestNG is used via Cucumber's `AbstractTestNGCucumberTests` class, which allows Cucumber scenarios to be executed as TestNG tests.

---

### Understanding Maven

**Maven** is a **build and dependency management tool**. It:
- Downloads required libraries (Selenium, Cucumber, Log4j, etc.)
- Compiles Java code
- Runs tests
- Generates reports

**Key Maven commands** you'll use:
```bash
mvn clean install          # Download dependencies and compile code
mvn test                   # Run all tests
mvn test -Dtag="@smoke"    # Run only tests tagged @smoke
```

---

## Technologies Used

| Technology | Version | Purpose | Used In Project |
|-----------|---------|---------|-----------------|
| **Java** | 25 (target 17) | Programming language for test logic | ✅ All test code |
| **Selenium** | 4.39.0 | Browser automation library | ✅ Interacts with Amazon website |
| **Cucumber** | 7.33.0 | BDD framework, executes Gherkin | ✅ Runs feature files as tests |
| **TestNG** | (via Cucumber) | Test execution framework | ✅ Organizes and runs test suites |
| **Log4j2** | 2.25.4 | Logging framework | ✅ Logs test execution details |
| **Gson** | 2.13.2 | JSON parser | ✅ Reads cartData.json test data |
| **Apache POI** | 5.4.1 | Excel/spreadsheet handling | ✅ Available for data-driven tests |
| **Allure** | 2.34.0 | Rich test reporting | ✅ Generates detailed test reports |
| **Maven** | 3.15.0 (compiler) | Build and dependency manager | ✅ Manages dependencies, runs tests |

---

## Framework Architecture

### Layered Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│  Feature Files (.feature)                                   │
│  • Search.feature, Cart.feature, etc.                       │
│  • Written in Gherkin (plain English)                       │
│  • Non-technical stakeholders understand these              │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│  Cucumber Framework                                         │
│  • Parses Gherkin and maps to step definitions              │
│  • Executes scenarios via TestNG runners                    │
│  • Dependency Injection via PicoContainer                   │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│  Step Definitions (Java Classes)                            │
│  • SearchStepDef, CartStepDef, etc.                         │
│  • Translate Gherkin to Selenium actions                    │
│  • Use Page Objects to interact with the UI                 │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│  Page Objects (Java Classes)                                │
│  • SearchPage, CartPage, ProductDetailsPage, etc.           │
│  • Encapsulate UI elements (XPath, CSS selectors)           │
│  • Provide reusable methods (searchForProduct(), addToCart) │
│  • Inherit from BasePage for common wait/click logic        │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│  Utilities & Helpers                                        │
│  • ConfigHandler (reads config.properties)                  │
│  • JSONFileManager (reads cartData.json)                    │
│  • ScreenShots (captures on failure)                        │
│  • RetryAnalyzer (retries failed tests)                     │
│  • WebDriverFactory (creates Chrome/Firefox/Edge drivers)   │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│  Selenium WebDriver                                         │
│  • Chrome, Firefox, Edge drivers                            │
│  • Communicates with browser via WebDriver protocol         │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│  Browser (Chrome, Firefox, Edge)                            │
│  • Actual browser executing test actions                    │
└─────────────────────────────────────────────────────────────┘
```

### How Each Layer Interacts

1. **Feature File** → Cucumber reads "When I search for a product 'Mouse'"
2. **Cucumber** → Finds matching step definition `@When("I search for a product {string}")`
3. **Step Definition** → Calls `searchPage.searchForProduct("Mouse")`
4. **Page Object** → Locates search field, enters text, clicks search button
5. **Selenium** → Sends commands to browser via WebDriver protocol
6. **Browser** → Performs actual search, displays results

---

## Folder Structure

```
Amazon Project_Cucumber/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/amazon/qa/
│   │   │       ├── base/
│   │   │       │   └── BasePage.java                 # Base class with common wait/click logic
│   │   │       ├── pages/
│   │   │       │   ├── SearchPage.java               # Page Object for search functionality
│   │   │       │   ├── SearchResultsPage.java        # Page Object for search results, filtering, sorting
│   │   │       │   ├── ProductDetailsPage.java       # Page Object for product details
│   │   │       │   └── CartPage.java                 # Page Object for shopping cart
│   │   │       └── utils/
│   │   │            ├── ConfigHandler.java        # Reads config.properties
│   │   │            ├── JSONFileManager.java      # Reads and parses cartData.json
│   │   │            ├── ScreenShots.java          # Captures screenshots on failure
│   │   │            └── PriceUtils.java        # Handles price-related utilities
│   │   └── resources/
│   │       ├── config.properties             # URL, browser configuration
│   │       └── cartData.json                 # Test data (search keywords, product info, filters)
│   │
│   └── test/
│       └── java
│           └── com/amazon/qa/tests
│               ├── base/
│               │   └── BaseTest.java             # Parent class holding shared objects (driver, wait, etc.)
│               │
│               ├── driver/
│               │   ├── WebDriverFactory.java     # Factory pattern: creates appropriate browser driver
│               │   ├── ChromeDriverClass.java    # Chrome driver setup
│               │   ├── FireFoxDriverClass.java   # Firefox driver setup
│               │   └── EdgeDriverClass.java      # Edge driver setup
│               │
│               ├── hooks/
│               │   └── HooksHandler.java         # Cucumber hooks: @Before, @AfterStep, @After
│               │
│               ├── Search/
│               │   ├── Search.feature            # Positive search scenarios
│               │   ├── Search_Nagative.feature   # Negative search scenarios
│               │   ├── SearchStepDef.java        # Step definitions for search
│               │   └── SearchRunner.java         # Test runner (TestNG) for search tests
│               │
│               ├── SearchResults/
│               │   ├── SearchResults.feature     # Filter and sort scenarios
│               │   ├── SearchResultsStepDef.java # Step definitions for filtering/sorting
│               │   └── SearchResultsRunner.java  # Test runner for search results
│               │
│               ├── ProductDetails/
│               │   ├── ProductDetails.feature    # Product details verification scenarios
│               │   ├── ProductDetailsStepDef.java# Step definitions for product details
│               │   └── ProductDetailsRunner.java # Test runner for product details
│               │
│               └── Cart/
│                   ├── Cart.feature              # Shopping cart scenarios
│                   ├── CartStepDef.java          # Step definitions for cart operations
│                   └── CartRunner.java           # Test runner for cart tests
│
├── pom.xml                                   # Maven configuration (dependencies, plugins)
├── Cucumber_Run.xml                          # TestNG suite configuration
├── logs/
│   └── application.log                       # Test execution logs
├── ScreenShots/                              # Screenshots captured on test failures
├── report/                                   # HTML reports generated by runners
└── allure-results/                           # Allure report data files
```

### Folder Purposes

| Folder | Purpose |
|--------|---------|
| `src/main/java/org/example/` | **Page Objects** — UI element locators and reusable methods |
| `src/main/resources/` | **Configuration** — config.properties and test data (JSON) |
| `src/test/java/tests/` | **Test code** — step definitions, runners, hooks, utilities |
| `logs/` | **Execution logs** — detailed test run information |
| `ScreenShots/` | **Failure evidence** — screenshots when tests fail |
| `report/` | **HTML reports** — test results in browser-readable format |
| `allure-results/` | **Allure data** — raw data for Allure reporting tool |

---

## Project Prerequisites

Before running tests, ensure you have the following installed and configured:

### 1. Java Development Kit (JDK)

**Required Version:** Java 25 or higher (Project source: 25, target: 25)

**Download:** https://www.oracle.com/java/technologies/downloads/

**Verify Installation:**
```bash
java -version
javac -version
```

**Expected Output:**
```
java version "25.x.x" ...
javac 25.x.x
```

### 2. Apache Maven

**Required Version:** Maven 3.6.0 or higher

**Download:** https://maven.apache.org/download.cgi

**Installation:**
1. Extract Maven to a folder (e.g., `C:\maven`)
2. Add Maven `bin` folder to system PATH
3. Restart terminal/IDE

**Verify Installation:**
```bash
mvn -version
```

**Expected Output:**
```
Apache Maven 3.x.x
Maven home: C:\path\to\maven
```

### 3. Git (Optional, for cloning repository)

**Download:** https://git-scm.com/download/win

**Verify Installation:**
```bash
git --version
```

### 4. IntelliJ IDEA (Recommended IDE)

**Download:** https://www.jetbrains.com/idea/

**Community Edition (FREE):** Sufficient for this project  
**Ultimate Edition:** Better Gherkin and Cucumber support

### 5. Web Browsers

Install at least one of the following:
- ✅ **Google Chrome** (default browser for tests) — https://www.google.com/chrome/
- ✅ **Mozilla Firefox** — https://www.mozilla.org/firefox/
- ✅ **Microsoft Edge** — https://www.microsoft.com/edge/

### 6. Internet Connection

Required for:
- Downloading Maven dependencies (Selenium, Cucumber, Log4j, etc.)
- Accessing Amazon Egypt website (https://www.amazon.eg/)
- Downloading browser drivers

### 7. System Requirements

- **RAM:** 4GB minimum (8GB recommended)
- **Disk Space:** 2GB free (for Maven dependencies and reports)
- **OS:** Windows, macOS, or Linux

---

## Downloading the Project

### Option 1: Clone via Git

```bash
git clone https://github.com/HGRBASHER/Amazon-Project-Cucumber.git
cd Amazon\ Project_Cucumber
```

### Option 2: Download as ZIP

1. Visit: https://github.com/HGRBASHER/Amazon-Project-Cucumber
2. Click **Code** → **Download ZIP**
3. Extract the ZIP file to your desired location
4. Open terminal and navigate to the extracted folder

```bash
cd path/to/Amazon\ Project_Cucumber
```

### Option 3: Open in IntelliJ IDEA

#### Method 1: From Existing Project Folder
1. Open IntelliJ IDEA
2. Click **File** → **Open**
3. Navigate to `Amazon Project_Cucumber` folder and select it
4. Click **Open**

#### Method 2: From Git URL (Inside IntelliJ)
1. Click **File** → **New** → **Project from Version Control**
2. Paste Git URL: `https://github.com/HGRBASHER/Amazon-Project-Cucumber.git`
3. Click **Clone**

### 4. Import Maven Dependencies

Once the project is open in IntelliJ:

1. Right-click on **pom.xml** in the project root
2. Select **Maven** → **Reload Project**
3. IntelliJ will download all dependencies (may take 2-5 minutes)

**Alternative via Terminal:**
```bash
mvn clean install
```

### 5. Verify Project Setup

After importing, verify:
- [ ] All Java files show no red error squiggles
- [ ] `target/` folder exists (Maven build output)
- [ ] `External Libraries` contains Selenium, Cucumber, Log4j, etc.

---

## Project Configuration

### config.properties

Location: `src/main/resources/config.properties`

This file controls global test settings:

```properties
url=https://www.amazon.eg/
browser=chrome
```

| Property | Value | Purpose | Valid Options |
|----------|-------|---------|---|
| `url` | `https://www.amazon.eg/` | Website to test | Any URL (e.g., https://www.amazon.com) |
| `browser` | `chrome` | Browser to use for tests | `chrome`, `firefox`, `edge` |

**How to Change Browser:**

Edit `config.properties`:
```properties
browser=firefox    # Runs tests in Firefox
```

Or pass as JVM parameter:
```bash
mvn test -Dbrowser=edge
```

### cartData.json

Location: `src/main/resources/cartData.json`

This file contains test data (search keywords, product names, filters) used across all scenarios:

```json
{
  "search": {
    "validKeyword": "Mouse",
    "invalidKeyword": "asdfghjkl123456789"
  },
  "productDetails": {
    "prod1Name": "Mixed Media",
    "prod2Name": "Posters & Prints",
    "firstProduct": "Wireless Gaming Mouse",
    "secondProduct": "Wireless Gaming Mouse - Black",
    "thirdProduct": {"productName": "Bags","targetPage" :2,"productBrand": "Mintra"}
  },
  "cart": {
    "product": "laptop",
    "prod1Index": 0,
    "prod2Index": 1,
    "quantities": {
      "increasedQty": 5,
      "decreasedQty": 2
    }
  },
  "filterAndSort": {
    "PriceStep": {
      "minPriceStep": "30",
      "maxPriceStep": "130"
    },
    "sortOption": "Price: Low to High",
    "brandFilter": "Redragon"
  }
}
```

**How to Use in Tests:**

Step definitions retrieve values using dot notation:
```java
@When("I search for a product {string}")
public void iSearchForAProduct(String productName) {
    // productName = "search.validKeyword"
    // jsonFileManager.getValue("search.validKeyword") returns "Mouse"
    searchResultsPage = searchPage.searchForProduct(
        jsonFileManager.getValue(productName).toString()
    );
}
```

**Modifying Test Data:**

1. Edit `src/main/resources/cartData.json`
2. Add/update values as needed
3. Rerun tests—they'll automatically use new data

**Example:** To change the brand filter from "Redragon" to "Sony":
```json
"brandFilter": "Sony"
```

### Cucumber_Run.xml

Location: `Cucumber_Run.xml` (project root)

This TestNG suite configuration file defines which test runners to execute:

```xml
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="MySuite">
    <test name="Search Test">
        <classes>
            <class name="search.com.amazon.qa.tests.SearchRunner"/>
        </classes>
    </test>
    <test name="Search Results Test">
        <classes>
            <class name="searchResults.com.amazon.qa.tests.SearchResultsRunner"/>
        </classes>
    </test>
    <test name="Product Details Test">
        <classes>
            <class name="product.com.amazon.qa.tests.ProductDetailsRunner"/>
        </classes>
    </test>
    <test name="Shopping Cart Test">
        <classes>
            <class name="cart.com.amazon.qa.tests.CartRunner"/>
        </classes>
    </test>
</suite>
```

**Purpose:** Defines the order and grouping of tests when running via TestNG.

**Add a New Test Suite:**
```xml
<test name="New Feature Test">
    <classes>
        <class name="tests.NewFeature.NewFeatureRunner"/>
    </classes>
</test>
```

### pom.xml

Location: `pom.xml` (project root)

Maven configuration file managing dependencies and build settings. Key sections:

**Project Identity:**
```xml
<artifactId>Amazon_Project_Cucumber</artifactId>
<version>1.0-SNAPSHOT</version>
```

**Java Compiler Settings:**
```xml
<maven.compiler.source>25</maven.compiler.source>
<maven.compiler.target>25</maven.compiler.target>
```

**Dependencies Include:**
- Selenium, Cucumber, TestNG, Log4j, Gson, Apache POI, Allure

See `pom.xml` for complete dependency list with versions.

---

## Understanding Cucumber

### What Are Feature Files?

Feature files (`.feature`) contain test scenarios written in **Gherkin**, a simple language that non-technical people can understand.

**Location:** `src/test/java/tests/[FeatureName]/[FeatureName].feature`

**Example Feature File: Search.feature**
```gherkin
Feature: Product Search Functionality
  Scenario Outline: Verify search functionality and result validation
    Given I am on the Amazon Egypt homepage
    When I search for a product "<product_name>"
    Then the search results page title should contain "<product_name>"
    And the system should display at least one search result

    Examples:
      | product_name        |
      | search.validKeyword |
```

### Understanding Scenarios

A **Scenario** is a single test case. It describes one user interaction flow with a clear beginning (Given), action (When), and expected outcome (Then).

**Real Example from Project:**
```gherkin
Scenario Outline: Verify search functionality with invalid search term
  Given I am on the Amazon Egypt homepage
  When I search for a product using an "invalid" search term
  Then the current URL should indicate a search page
  And the system should display zero search results
  And a "No Results Found" message should be visible to the user
  And the header navigation bar should remain displayed
  Examples:
    | invalid               |
    | search.invalidKeyword |
```

### Understanding Scenario Outline

A **Scenario Outline** runs the same scenario multiple times with different data (from the Examples table).

**Why Use Scenario Outline?**
Instead of writing 5 nearly-identical scenarios, write ONE scenario with placeholders (`<placeholder>`) and provide multiple data rows in Examples.

**Example:**
```gherkin
Scenario Outline: Verify adding multiple items to cart and subtotal calculation
  Given I am on the Amazon Egypt homepage
  When I search for and add two products "<product>" "<firstProd>" "<SecondProd>" to cart
  And I navigate to the cart page
  Then I verify the cart details and subtotal
  Examples:
    | product      | firstProd       | SecondProd      |
    | cart.product | cart.prod1Index | cart.prod2Index |
```

Here, placeholders `<product>`, `<firstProd>`, `<SecondProd>` are replaced with values from the Examples table.

### Understanding Given/When/Then

**Given** = Setup / Precondition  
**When** = Action / User behavior  
**Then** = Assertion / Expected outcome

**Example:**
```gherkin
Given I am on the Amazon Egypt homepage     # System state: User is on home page
When I search for a product "Mouse"         # User action: Searches for Mouse
Then the search results page title should contain "Mouse"  # Expected: Title shows "Mouse"
And the system should display at least one search result  # Expected: Results exist
```

### Feature Files in This Project

| Feature File | Purpose | Scenarios |
|--------------|---------|-----------|
| `Search.feature` | Valid product search | Scenario Outline: search with valid keyword |
| `Search_Nagative.feature` | Invalid search handling | Scenario Outline: search with invalid keyword |
| `SearchResults.feature` | Filtering & sorting | Scenario Outline: brand filter, price filter, sort by price |
| `ProductDetails.feature` | Product info verification | 2 Scenario Outlines: UI component validation |
| `Cart.feature` | Shopping cart operations | 4 Scenario Outlines: add items, remove items, quantity update, stock limit |

---

## Cucumber Hooks

### What Are Hooks?

Hooks are methods that run **before and after** each scenario. They're used for setup (opening browser) and teardown (closing browser, logging).

**Location:** `src/test/java/tests/hooks/HooksHandler.java`

### Implemented Hooks in This Project

#### 1. @Before Hook (Setup)

Runs **before each scenario** to initialize:
- JSON and config file managers
- WebDriver
- WebDriverWait
- SoftAssert

```java
@Before
public void setUP(){
    jsonFileManager = new JSONFileManager("src/main/resources/cartData.json");
    log.debug("json File Manager initialized");
    
    configHandler = new ConfigHandler("src/main/resources/config.properties");
    log.debug("configHandler initialized");
    
    driver = WebDriverFactory.getDriver(configHandler.getValue("browser"));
    driver.get(configHandler.getValue("url"));
    searchPage = new SearchPage(driver);
    log.debug("DriverSingleton initialized");
    
    explicitWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
    log.debug("Explicit wait initialized");
    
    softAssert = new SoftAssert();
    log.debug("SoftAssert initialized");
}
```

**What It Does:**
1. Loads test data from `cartData.json`
2. Loads configuration from `config.properties`
3. Creates a WebDriver instance (Chrome/Firefox/Edge based on config)
4. Navigates to Amazon Egypt website
5. Initializes wait times and soft assertions

#### 2. @AfterStep Hook (Failure Capture)

Runs **after each step** (Given/When/Then). If a step fails, it captures a screenshot.

```java
@AfterStep
public void checkFail(Scenario scenario) {
    try {
        if(scenario.isFailed()){
            File srcShoot = ScreenShots.getScreenShot(driver,"ScreenShots/"+scenario.getName()+".png");
            Allure.addAttachment("Screenshots", new FileInputStream(srcShoot));
            log.error("Test Failed: {} - Screenshot attached.", scenario.getName());
        }
    }catch (Exception e){
        log.error("Failed to take screenshot: {}", e.getMessage());
    }
}
```

**What It Does:**
1. Checks if the step failed
2. Captures a screenshot and saves it to `ScreenShots/[ScenarioName].png`
3. Attaches screenshot to Allure report
4. Logs the failure

#### 3. @After Hook (Teardown)

Runs **after each scenario** to clean up:
- Close WebDriver
- Attach log file to Allure report

```java
@After
public void tearDown() {
    if (driver != null) {
        WebDriverFactory.quitDriver();
    }
    log.info("Driver closed successfully");
    try {
        Allure.addAttachment("Log File", new FileInputStream("logs/application.log"));
    }catch (Exception e){
        log.error("Failed to attach log file to Allure: {}", e.getMessage());
    }
}
```

**What It Does:**
1. Closes the WebDriver and browser
2. Attaches application log file to Allure report for debugging

### Hook Execution Order

For a single scenario:
```
@Before Hook    ← Sets up driver, config, data
  Step 1 (Given)
  @AfterStep    ← Check if step failed, take screenshot if yes
  Step 2 (When)
  @AfterStep    ← Check if step failed, take screenshot if yes
  Step 3 (Then)
  @AfterStep    ← Check if step failed, take screenshot if yes
@After Hook     ← Close driver, attach logs
```

---

## Install Dependencies

### What Maven Does

Maven is a build automation tool that:
1. Downloads required libraries (Selenium, Cucumber, etc.) from Maven Central Repository
2. Compiles Java source code
3. Runs tests
4. Generates reports

### Step 1: Clean and Install

Run this command to download all dependencies and compile the project:

```bash
mvn clean install
```

**What This Does:**
- `clean` — Removes old build files (`target/` folder)
- `install` — Downloads dependencies and compiles code

**Expected Output:**
```
[INFO] Downloading selenium-java-4.39.0.jar
[INFO] Downloading cucumber-java-7.33.0.jar
...
[INFO] BUILD SUCCESS
```

**First Run:** May take 3-5 minutes (downloading ~100+ JAR files)

**Subsequent Runs:** Much faster (dependencies cached locally)

### Step 2: Verify Installation

Check that dependencies are installed:

```bash
mvn dependency:tree
```

This shows the full dependency tree with versions.

### Step 3: Resolve Issues

**Common Error: "Cannot find java"**
```
Solution: Add Java to system PATH or set JAVA_HOME environment variable
Windows: set JAVA_HOME=C:\Program Files\Java\jdk-17
Mac/Linux: export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home
```

**Common Error: "Cannot download dependencies"**
```
Solution: Check internet connection or run:
mvn clean install -U
(-U forces Maven to re-download dependencies)
```

---

## Run Tests

### Method 1: Run via Maven (Recommended)

#### Run All Tests
```bash
mvn test
```

Executes all test runners defined in `Cucumber_Run.xml` (Search, SearchResults, ProductDetails, Cart).

#### Run Specific Feature Module
```bash
mvn test -Dtest=SearchRunner
mvn test -Dtest=CartRunner
mvn test -Dtest=ProductDetailsRunner
mvn test -Dtest=SearchResultsRunner
```

#### Run with Specific Browser
```bash
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge
mvn test -Dbrowser=chrome    # (default)
```

#### Run with Tag Filtering

Tags allow you to run a subset of scenarios. For example, to run only "smoke" tests:

```bash
mvn test -Dtag="@smoke"
```

**Common Tags (add these to feature files):**
```gherkin
@smoke
Scenario: Quick smoke test

@regression
Scenario: Full regression test

@ui
Scenario: UI verification only

@critical
Scenario: Critical business flow
```

### Method 2: Run via TestNG (GUI)

1. Right-click on `Cucumber_Run.xml` in IntelliJ
2. Select **Run 'MySuite'**

Results display in a TestNG tree view within IntelliJ.

### Method 3: Run via IntelliJ IDE

#### Run a Single Feature File
1. Right-click on any `.feature` file
2. Select **Run [FeatureName]**

#### Run a Scenario
1. Right-click on a specific scenario in a `.feature` file
2. Select **Run**

### Method 4: Run via Command Line (No Maven)

If Maven is not available, you can run tests directly with Java:

```bash
java -cp "target/classes;target/test-classes;...jar-files" \
     org.testng.TestNG Cucumber_Run.xml
```

(Not recommended—use Maven instead.)

### Example Test Runs

**Run all tests:**
```bash
mvn clean test
```

**Run Search tests in Firefox with detailed logging:**
```bash
mvn test -Dtest=SearchRunner -Dbrowser=firefox
```

**Run Cart tests and generate Allure report:**
```bash
mvn test -Dtest=CartRunner
allure serve allure-results/
```

---

## Cucumber Reports

### Reporting Libraries Used

This project uses **two reporting tools**:

| Report Type | Library | Output Location | Format | How to View |
|-------------|---------|-----------------|--------|------------|
| **Allure** | allure-cucumber7-jvm 2.34.0 | `allure-results/` | JSON data + interactive HTML | `allure serve allure-results/` |
| **HTML** | Cucumber HTML plugin (built-in) | `report/loginRunner-reports.html` | Simple HTML | Open in browser |

### Allure Reports (Recommended)

**What is Allure?**  
Allure is a framework that generates **beautiful, interactive test reports** with:
- Test summary (passed, failed, skipped)
- Screenshots for failed tests
- Test duration
- Step-by-step execution flow
- Log files attached

#### Generate Allure Report

**Step 1: Run Tests**
```bash
mvn clean test
```

This generates JSON data in `allure-results/` folder.

**Step 2: Serve the Report Locally**
```bash
allure serve allure-results/
```

The report opens in your default browser at `http://localhost:4040`

**Alternative: Generate and Save as Static HTML**
```bash
allure generate allure-results/ -o allure-report/
# Then open: allure-report/index.html in a browser
```

#### Allure Report Features

✅ **Test Summary** — Pass/fail counts, execution time  
✅ **Screenshots** — Attached to failed steps automatically  
✅ **Logs** — application.log file attached for debugging  
✅ **Timeline** — See which tests ran when  
✅ **History** — Track test trends over multiple runs

### HTML Reports

**Location:** `report/loginRunner-reports.html`

Simple HTML report generated by Cucumber plugin. Less detailed than Allure but easier to share.

**To View:**
```bash
open report/loginRunner-reports.html    # Mac
start report/loginRunner-reports.html   # Windows
```

### Access Reports After Test Run

After running `mvn test`:

**Allure Report:**
```bash
allure serve allure-results/
```

**HTML Report:**
```bash
# Navigate to project folder and open in browser
report/loginRunner-reports.html
```

### Understanding Allure Report Sections

1. **Overview** — Pie chart showing pass/fail ratio
2. **Tests** — List of all scenarios with pass/fail status
3. **Behaviors** — Grouped by Feature
4. **Timeline** — When each test executed
5. **Graph** — Trends over multiple runs

---

## Tagging

### What Are Tags?

Tags are labels (starting with `@`) that you add to scenarios. They're used to:
- Group related scenarios
- Run subsets of tests (e.g., run only @smoke tests)
- Mark tests by priority or component

### Using Tags in Feature Files

```gherkin
@smoke @critical
Scenario: User searches for valid product
  Given I am on the Amazon Egypt homepage
  When I search for a product "Mouse"
  Then the search results should display

@regression @ui
Scenario Outline: Verify cart subtotal calculation
  ...
```

### Tags in This Project

Currently, **no tags are implemented** in the feature files. However, you can add them:

### How to Add Tags

Edit any `.feature` file and add `@tagname` before a scenario:

**Search.feature:**
```gherkin
@smoke @search
Scenario Outline: Verify search functionality and result validation
  Given I am on the Amazon Egypt homepage
  When I search for a product "<product_name>"
  Then the search results page title should contain "<product_name>"
  And the system should display at least one search result
  Examples:
    | product_name        |
    | search.validKeyword |
```

### Run Tests by Tag

**Run only @smoke tests:**
```bash
mvn test -Dtag="@smoke"
```

**Run @search OR @cart:**
```bash
mvn test -Dtag="@search or @cart"
```

**Run @critical AND @ui:**
```bash
mvn test -Dtag="@critical and @ui"
```

**Exclude @skip:**
```bash
mvn test -Dtag="not @skip"
```

### Recommended Tag Strategy

Adopt a standard tagging convention:

```gherkin
@smoke           # Quick sanity tests (< 5 min total)
@regression      # Full test coverage
@critical        # Business-critical functionality
@ui              # UI verification tests
@integration     # Multi-feature integration tests
@slow            # Long-running tests (e.g., stock checks)
@skip            # Temporarily disable a scenario
```

**Example Feature File with Tags:**
```gherkin
Feature: Product Search

@smoke @search @critical
Scenario: Valid product search
  ...

@regression @search
Scenario Outline: Multiple search queries
  ...
```

---

## Screenshots

### How Screenshots Work

Screenshots are automatically captured when a test **fails** during execution.

### When Screenshots Are Taken

**Location in Code:** `src/test/java/tests/hooks/HooksHandler.java`

```java
@AfterStep
public void checkFail(Scenario scenario) {
    if(scenario.isFailed()){
        File srcShoot = ScreenShots.getScreenShot(driver,"ScreenShots/"+scenario.getName()+".png");
        Allure.addAttachment("Screenshots", new FileInputStream(srcShoot));
        log.error("Test Failed: {} - Screenshot attached.", scenario.getName());
    }
}
```

**Trigger:** After each step (Given/When/Then), if step fails

### Where Screenshots Are Saved

**Location:** `ScreenShots/` folder (project root)

**File Naming:** `[ScenarioName].png`

**Example:**
```
ScreenShots/
├── Verify search functionality and result validation.png
├── Add two items and remove one to verify cart functionality.png
└── Verify updating cart quantity and subtotal calculation.png
```

### How Screenshot Logic Works

1. **Step fails** (e.g., assertion fails)
2. Scenario marked as "failed"
3. `@AfterStep` hook runs
4. Checks `scenario.isFailed()` — returns true
5. Calls `ScreenShots.getScreenShot(driver, path)` to capture browser window
6. Saves PNG file to `ScreenShots/[ScenarioName].png`
7. Attaches screenshot to Allure report

### View Screenshots

**In Allure Report:**
1. Run: `allure serve allure-results/`
2. Click on a failed test
3. Scroll to "Screenshots" attachment

**Manually in File System:**
```bash
open ScreenShots/              # Mac
explorer ScreenShots/          # Windows (opens folder)
```

### Screenshot Utility Class

**Location:** `src/test/java/tests/reuse/ScreenShots.java`

```java
public static File getScreenShot(WebDriver driver, String url) throws IOException {
    TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
    File srcShoot = takesScreenshot.getScreenshotAs(OutputType.FILE);
    File dest = new File(url);
    FileHandler.copy(srcShoot, dest);
    return dest;
}
```

**How to Take Manual Screenshots:**

In step definitions or page objects:
```java
ScreenShots.getScreenShot(driver, "ScreenShots/manual-screenshot.png");
```

---

## Logging

### Logging Framework

**Framework:** Apache Log4j2 (version 2.25.4)

Log4j2 is an industry-standard logging library that records detailed execution information.

### Log File Location

**File:** `logs/application.log`

**Automatically generated** during test execution.

### Log Levels

| Level | Severity | When Used |
|-------|----------|-----------|
| **DEBUG** | Low | Detailed execution flow (every step, initialization) |
| **INFO** | Medium | Important events (driver created, test started) |
| **WARN** | High | Unexpected but recoverable situations (element not found, retry) |
| **ERROR** | Critical | Test failures, exceptions |

### Log Configuration

**Location:** NOT explicitly configured in this project

Log4j2 uses default configuration (logs to console and file).

**To customize:** Create `log4j2.xml` in `src/main/resources/`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration>
    <Appenders>
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
        </Console>
        <File name="File" fileName="logs/application.log">
            <PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
        </File>
    </Appenders>
    <Loggers>
        <Root level="debug">
            <AppenderRef ref="Console"/>
            <AppenderRef ref="File"/>
        </Root>
    </Loggers>
</Configuration>
```

### Logging in Java Code

**Example from SearchResultsPage.java:**
```java
private static final Logger log = LogManager.getLogger(SearchResultsPage.class);

public void applyBrandFilter(String brandName) {
    log.info("Applying brand filter: {}", brandName);
    // ... code ...
    log.debug("Brand filter applied successfully");
}
```

### View Logs

**During test execution:**
Logs print to console in real-time.

**After test execution:**
View detailed logs in `logs/application.log`:

```bash
tail -f logs/application.log    # Stream latest logs
cat logs/application.log        # View entire log file
```

**In Allure Report:**
Logs automatically attached to failed tests for debugging.

### Log Examples from Project

From `HooksHandler.java`:
```java
log.debug("json File Manager initialized");
log.debug("configHandler initialized");
log.debug("DriverSingleton initialized");
log.error("Test Failed: {} - Screenshot attached.", scenario.getName());
log.info("Driver closed successfully");
```

From `SearchResultsPage.java`:
```java
log.info("Setting price slider [{}] value to: [{}] via JavaScript", isMin ? "MIN" : "MAX", targetValue);
log.warn("Product at index {} not available. Retrying...", currentIndex);
log.error("Failed to parse product price from all known selectors!");
```

---

## Test Scenarios

### Overview of Test Coverage

| Feature | File | Positive Cases | Negative Cases |
|---------|------|---|---|
| **Product Search** | Search.feature | ✅ Search for valid product, validate results | ❌ See Search_Negative.feature |
| **Search (Negative)** | Search_Nagative.feature | N/A | ✅ Search with invalid keyword, handle no results |
| **Filtering & Sorting** | SearchResults.feature | ✅ Apply brand filter, price range, sort options | N/A (implicit validation) |
| **Product Details** | ProductDetails.feature | ✅ Verify UI components, data consistency | N/A (implicit validation) |
| **Shopping Cart** | Cart.feature | ✅ Add items, remove items, update quantity | ✅ Inventory limits, out-of-stock handling |

### Detailed Scenario Breakdown

#### Feature 1: Product Search (Search.feature)

**Scenario Outline:** Verify search functionality and result validation

**Positive Cases:**
- ✅ Search for "Mouse" → Results display with "Mouse" in title
- ✅ At least 1 product shown
- ✅ Results page title updated

**Data:** `search.validKeyword = "Mouse"`

#### Feature 2: Search - Negative (Search_Nagative.feature)

**Scenario Outline:** Verify search functionality with invalid search term

**Negative Cases:**
- ❌ Search for "asdfghjkl123456789" → No results
- ❌ "No Results Found" message displayed
- ❌ Navigation bar remains visible despite no results
- ✅ URL contains search parameters

**Data:** `search.invalidKeyword = "asdfghjkl123456789"`

#### Feature 3: Filtering & Sorting (SearchResults.feature)

**Scenario Outline:** Verify filter application and sorting order

**Positive Cases:**
- ✅ Search for "Wireless Gaming Mouse"
- ✅ Apply brand filter "Redragon" → All results show "Redragon"
- ✅ Apply price range filter (₦30 - ₦130) → All prices within range
- ✅ Sort by "Price: Low to High" → Prices in ascending order
- ✅ Sort by "Price: High to Low" → Prices in descending order
- ✅ Sort by "Avg. Customer Review" → Ratings in descending order

**Data:**
```json
"productDetails.firstProduct": "Wireless Gaming Mouse",
"filterAndSort.brandFilter": "Redragon",
"filterAndSort.PriceStep": {"minPriceStep": "30", "maxPriceStep": "130"},
"filterAndSort.sortOption": "Price: Low to High"
```

#### Feature 4: Product Details (ProductDetails.feature)

**Scenario 1: Verify integrity of product details page UI components**

**Positive Cases:**
- ✅ Search for "Mixed Media"
- ✅ Open first product from results
- ✅ Product title not empty
- ✅ Product price > 0
- ✅ All UI elements visible:
    - Product Title
    - Product Description
    - Product Images
    - Add to Cart Button
    - Product Price
    - Buy Now Button

**Scenario 2: Verify product details match search results**

**Positive Cases:**
- ✅ Search for "Posters & Prints"
- ✅ Identify valid product and open details page
- ✅ Product title matches (substring comparison)
- ✅ Product price matches (±5 tolerance)
- ✅ Availability status visible
- ✅ Add to Cart button visible

#### Feature 5: Shopping Cart (Cart.feature)

**Scenario 1: Add two items and remove one**

**Positive Cases:**
- ✅ Search and add "Wireless Gaming Mouse"
- ✅ Search and add "Wireless Gaming Mouse - Black"
- ✅ Cart contains 2 items
- ✅ Remove first product → "was removed from Shopping Cart" message
- ✅ Second product still in cart

**Scenario 2: Add multiple items and verify subtotal**

**Positive Cases:**
- ✅ Search for "laptop"
- ✅ Add 2 products
- ✅ Navigate to cart
- ✅ Verify cart count = 2
- ✅ Verify product titles present
- ✅ Verify product prices match (±5 tolerance)
- ✅ Verify subtotal = price1 + price2

**Scenario 3: Update quantity and verify subtotal**

**Positive Cases:**
- ✅ Search and add 1 product
- ✅ Increase quantity to 5 → Subtotal increases
- ✅ Decrease quantity to 2 → Subtotal decreases
- ✅ Out-of-stock warning if quantity exceeds limit

**Negative Cases:**
- ❌ Quantity exceeds inventory → Error message displayed
- ❌ Quantity capped at inventory limit

**Scenario 4: Verify inventory limit**

**Negative Cases:**
- ❌ Search for product with limited stock
- ❌ Add to cart
- ❌ Attempt to set quantity > stock limit
- ❌ Quantity remains at limit (not increased)
- ❌ Error message explains inventory constraint

---

## Design Patterns & Best Practices

### 1. Page Object Model (POM)

**What It Is:** Organizational pattern where each page/component has a dedicated class encapsulating UI element locators and interaction methods.

**Why It's Used:** Reduces code duplication, improves maintainability, isolates UI changes from test logic.

**Implementation in This Project:**

```
src/main/java/org/example/
├── BasePage.java            # Common functionality
├── SearchPage.java          # Search UI + methods
├── SearchResultsPage.java   # Results UI + filtering/sorting
├── ProductDetailsPage.java  # Product UI + add-to-cart
└── CartPage.java            # Cart UI + quantity/removal
```

**Example: SearchPage.java**
```java
public class SearchPage extends BasePage {
    private final By searchField = By.id("twotabsearchtextbox");
    private final By searchButton = By.id("nav-search-submit-button");

    public SearchResultsPage searchForProduct(String productName) {
        writeText(searchField, productName);      // Reusable method from BasePage
        clickOnElement(searchButton);             // Reusable method from BasePage
        return new SearchResultsPage(driver);
    }
}
```

**Benefit:** If Amazon changes the search button ID tomorrow, you update only `SearchPage.java`, not 50 test files.

### 2. Factory Pattern

**What It Is:** Pattern for creating objects (WebDriver instances) based on a parameter.

**Why It's Used:** Centralizes object creation, makes code extensible.

**Implementation:**

```
src/test/java/tests/driver/
├── WebDriverFactory.java     # Factory class
├── ChromeDriverClass.java    # Chrome creation
├── FireFoxDriverClass.java   # Firefox creation
└── EdgeDriverClass.java      # Edge creation
```

**Example:**
```java
public class WebDriverFactory {
    public static WebDriver getDriver(String browser) {
        switch(browser.toLowerCase()) {
            case "chrome":
                return ChromeDriverClass.getChromeDriver();
            case "firefox":
                return FireFoxDriverClass.getFireFoxDriver();
            case "edge":
                return EdgeDriverClass.getEdgeDriver();
            default:
                throw new IllegalArgumentException("Invalid browser: " + browser);
        }
    }
}
```

**Usage in Hooks:**
```java
driver = WebDriverFactory.getDriver(configHandler.getValue("browser"));
// Automatically creates Chrome, Firefox, or Edge based on config
```

### 3. Singleton Pattern

**What It Is:** Ensuring only one instance of an object exists throughout the application.

**Why It's Used:** Prevents multiple WebDriver instances, which would waste resources.

**Implementation:**

Each driver class maintains a single static instance:
```java
public class ChromeDriverClass {
    private static WebDriver driver = null;

    public static WebDriver getChromeDriver() {
        if(driver == null) {  // Only create once
            driver = new ChromeDriver(options);
            driver.manage().window().maximize();
        }
        return driver;
    }
}
```

### 4. Dependency Injection

**What It Is:** Providing dependencies (like WebDriver) to classes rather than creating them internally.

**Why It's Used:** Makes code testable and flexible.

**Implementation:**

Cucumber uses **PicoContainer** for dependency injection. Each Page Object receives WebDriver via constructor:

```java
public class SearchPage extends BasePage {
    public SearchPage(WebDriver driver) {
        super(driver);  // Inject WebDriver
    }
}

public class BasePage {
    public WebDriver driver;
    public WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
}
```

### 5. Template Method Pattern

**What It Is:** BasePage defines common methods (findElement, clickOnElement, writeText) that all page objects inherit.

**Why It's Used:** Eliminates repetitive code, ensures consistent wait logic.

**Implementation:**

```java
public class BasePage {
    public WebElement findElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void writeText(By locator, String text) {
        WebElement element = findElement(locator);
        element.clear();
        element.sendKeys(text);
    }

    public void clickOnElement(By locator) {
        findElement(locator).click();
    }
}
```

All page objects use these methods, ensuring consistent waits and exception handling.

### 6. Configuration Management Pattern

**What It Is:** Centralizing configuration (URL, browser, test data) in files rather than hardcoding.

**Why It's Used:** Easy to switch environments/browsers without code changes.

**Implementation:**

```java
public class ConfigHandler {
    public String getValue(String key) {
        return properties.getProperty(key);
    }
}

public class JSONFileManager {
    public Object getValue(String keyPath) {
        // Retrieves nested JSON values using dot notation
    }
}
```

**Usage:**
```java
driver.get(configHandler.getValue("url"));           // From config.properties
searchForProduct(jsonFileManager.getValue("search.validKeyword"));  // From cartData.json
```

### 7. Data-Driven Testing

**What It Is:** Parameterizing tests with multiple data sets to test different scenarios.

**Why It's Used:** Tests same functionality with various inputs efficiently.

**Implementation (Scenario Outline):**

```gherkin
Scenario Outline: Verify search functionality
  When I search for a product "<product_name>"
  Examples:
    | product_name        |
    | search.validKeyword |
```

Each row in Examples = one test execution.

### Best Practices Applied

✅ **Explicit Waits** — Use WebDriverWait instead of Thread.sleep()  
✅ **Soft Assertions** — SoftAssert allows multiple assertions; test doesn't fail on first error  
✅ **Fail Artifacts** — Screenshots and logs attached to failed tests  
✅ **Retry Logic** — RetryAnalyzer retries failed tests (max 3 attempts)  
✅ **Page Objects** — UI elements encapsulated, not scattered in tests  
✅ **Separation of Concerns** — Steps, Pages, Drivers kept separate  
✅ **Clear Naming** — Classes, methods, variables have descriptive names  
✅ **Logging** — Detailed logs for debugging failures

---

## Troubleshooting

### Common Issues and Solutions

| Issue | Cause | Solution |
|-------|-------|----------|
| **"Cannot find symbol: SearchPage"** | Missing import or incorrect package | Ensure `import com.amazon.qa.pages.SearchPage;` in step def class |
| **"WebDriver timeout waiting for element"** | Element doesn't exist or takes > 10s to load | Increase wait time: `new WebDriverWait(driver, Duration.ofSeconds(20))` |
| **"Chrome driver not found"** | Chrome is not installed or path is wrong | Install Chrome from google.com/chrome or set `CHROME_DRIVER_PATH` |
| **"Failed to execute step"** | Step definition regex doesn't match feature file text | Ensure step text matches exactly, including placeholders `{string}`, `{int}` |
| **"Port 4040 is already in use"** | Previous Allure report server still running | Kill process: `lsof -i :4040` then `kill -9 [PID]` or `netstat -ano | findstr :4040` (Windows) |
| **"Reports folder is empty"** | Tests passed (no failures), no screenshots captured | Failures are required to generate screenshots; add assertion failure to test |
| **"NullPointerException on driver"** | Driver not initialized | Verify `@Before` hook is running; check logs |
| **"JSON parsing error"** | Malformed JSON in cartData.json | Validate JSON: use jsonlint.com or IDEs JSON validator |
| **"Scenario name has special characters"** | Filename with `/:*?"<>\|` invalid on Windows | Use alphanumeric and hyphens only in scenario names |
| **"Test passes locally but fails in CI"** | Environment differences (URLs, browser versions) | Check `config.properties` matches CI environment |
| **"Cannot connect to amazon.eg"** | Website unreachable or blocked | Check internet connection, verify URL in `config.properties` |
| **"Allure report not generating"** | Missing `allure-cucumber7-jvm` dependency | Verify pom.xml has: `allure-cucumber7-jvm` in dependencies |

---

## FAQ

### For Absolute Beginners

**Q1: What is Automation Testing?**

A: Instead of manually clicking buttons and typing, a computer program (test script) does it automatically. The script interacts with the website, verifies that things work correctly, and reports results. For example, instead of you manually searching for "Mouse" on Amazon 100 times, the script does it instantly.

**Q2: Do I need to be a programmer to use this framework?**

A: Not completely. You need to understand:
- Basic Java concepts (variables, if/else, loops)
- How to run commands in terminal
- Reading Gherkin scenarios (written in plain English)

If you're new to programming, start by reading feature files (they're in English!), then gradually learn step definitions.

**Q3: How do I run a single test?**

A: Run a specific scenario via IntelliJ:
1. Open the feature file
2. Click the ▶ (Play) icon next to the scenario
3. Results appear in IntelliJ console

Or via terminal:
```bash
mvn test -Dtest=SearchRunner
```

**Q4: Where do I find test results?**

A:
- **Allure Report:** Run `allure serve allure-results/` to open interactive report
- **HTML Report:** Open `report/loginRunner-reports.html` in browser
- **Screenshots:** Check `ScreenShots/` folder for failure captures
- **Logs:** View `logs/application.log` for detailed execution trace

**Q5: How do I add a new test scenario?**

A:
1. Open a feature file (e.g., `Search.feature`)
2. Add a new scenario:
   ```gherkin
   @smoke
   Scenario: My new test
     Given I am on the Amazon Egypt homepage
     When I search for "laptop"
     Then results should display
   ```
3. Create a step definition in the corresponding class:
   ```java
   @Then("results should display")
   public void resultsDisplay() {
       softAssert.assertTrue(searchResultsPage.getProductsCount() > 0);
   }
   ```
4. Run test: `mvn test -Dtest=SearchRunner`

### For Junior QA Engineers (Manual Test Background)

**Q6: How does this differ from manual testing?**

A:
| Manual Testing | Automation |
|---|---|
| Click, type, verify manually | Script clicks, types, verifies automatically |
| Takes 30 minutes per test run | Takes 2 minutes per test run |
| Prone to human error | Consistent, repeatable |
| Good for exploratory testing | Great for regression testing |
| Labor-intensive | Scalable to 100s of tests |

This framework lets you run 50 tests simultaneously, freeing you for exploratory testing.

**Q7: What is a Page Object, and why should I care?**

A: A Page Object is a Java class representing a web page (SearchPage, CartPage, etc.). Instead of hardcoding element locators in tests, you centralize them in page objects:

**Bad (without Page Objects):**
```java
driver.findElement(By.id("twotabsearchtextbox")).sendKeys("Mouse");
driver.findElement(By.id("nav-search-submit-button")).click();
// ... 50 more test lines hardcoding selectors
```

**Good (with Page Objects):**
```java
searchPage.searchForProduct("Mouse");
// Clean, reusable, maintainable
```

If Amazon changes the search button selector, you update only `SearchPage.java`, not 50 test files.

**Q8: What does "soft assert" mean?**

A: Normally, when an assertion fails, the test stops immediately (hard assert). A **soft assert** allows multiple assertions to run even if one fails:

```java
softAssert.assertTrue(titleContainsMouse, "Title missing Mouse");
softAssert.assertTrue(resultsCount > 0, "No results found");
softAssert.assertTrue(navBarVisible, "Nav bar missing");
softAssert.assertAll();  // Reports all failures, not just the first
```

Benefit: See all failures in one test run, not just the first error.

### For Developers Joining QA

**Q9: How does Cucumber integrate with Java and Selenium?**

A: Cucumber **bridges plain English (Gherkin) and Java code (Selenium)**:

1. Feature file: `When I search for a product "Mouse"`
2. Cucumber matches: `@When("I search for a product {string}")`
3. Java executes: `searchPage.searchForProduct("Mouse")`
4. Selenium clicks/types: Interacts with browser

Think of Cucumber as a **translator**—it converts English sentences into Java method calls.

**Q10: How does Dependency Injection work in this framework?**

A: Cucumber uses **PicoContainer** to inject dependencies:

```java
// HooksHandler creates driver
@Before
public void setup() {
    driver = WebDriverFactory.getDriver(browser);
}

// Any step definition automatically receives the same driver instance
public class SearchStepDef extends BaseTest {
    // "driver" field inherited from BaseTest, injected by PicoContainer
    // All step definitions share the SAME driver for a scenario
}
```

Benefit: No manual passing of driver between classes; Cucumber handles it.

---

## Future Improvements

### Currently NOT Implemented (Planned for Future Versions)

| Feature | Description | Benefit |
|---------|-------------|---------|
| **Parallel Execution** | Run multiple scenarios simultaneously across cores | Reduce total test time from 30 min to 5 min |
| **Docker Support** | Run tests in containerized environments | Standardize test environment across machines |
| **Selenium Grid** | Distribute tests across multiple machines/browsers | Increased throughput and browser coverage |
| **Cloud Execution** | Run tests on cloud services (BrowserStack, Sauce Labs) | Test on real devices, no local setup needed |
| **API Testing** | Automate backend API calls alongside UI tests | End-to-end integration testing |
| **Performance Testing** | Measure page load times, response times | Identify bottlenecks |
| **Mobile Testing** | Automate tests on mobile devices (Appium) | Test mobile shopping experience |
| **Database Validation** | Directly query database to validate data changes | Complete end-to-end validation |
| **Visual Testing** | Compare screenshots to baseline images | Catch UI regressions automatically |
| **Retry with Backoff** | Retry failed tests with increasing delays | Handle flaky tests better |
| **Test Metrics Dashboard** | Real-time test execution metrics | Monitor test health trends |
| **Slack/Email Notifications** | Notify team of test results automatically | No manual status checking |
| **GitHub Actions CI/CD** | Auto-run tests on every Git push | Continuous quality gate |
| **Advanced Reporting** | Trend analysis, historical data, comparisons | Executive reporting |

### How to Contribute Improvements

1. Create a branch: `git checkout -b feature/parallel-execution`
2. Implement the feature
3. Test thoroughly
4. Submit a pull request

---

## Author

**Hagar Basheer**

QA Automation Engineer | BDD Framework Specialist | Cucumber & Selenium Expert

---

## Contact

📧 **Email:** hagareid307@gmail.com  
🔗 **LinkedIn:** http://linkedin.com/in/hagarbasheer  
💻 **GitHub:** https://github.com/HGRBASHER

Feel free to reach out for questions, suggestions, or collaboration opportunities!

---

## License

This project is licensed under the **MIT License** — see the LICENSE file for details.

You are free to:
- ✅ Use this project for personal and commercial purposes
- ✅ Modify and distribute the code
- ✅ Use it as a basis for your own projects

With the condition that you include the original license notice.

---

## Quick Start Checklist

- [ ] Download and install Java 17+
- [ ] Download and install Maven
- [ ] Download and install at least one browser (Chrome, Firefox, or Edge)
- [ ] Clone/download the project
- [ ] Run: `mvn clean install`
- [ ] Run: `mvn test`
- [ ] View results: `allure serve -h localhost`
- [ ] Read a feature file to understand test scenarios
- [ ] Try modifying a test and running it

---

## Additional Resources

### Learning Resources

- **Cucumber Official:** https://cucumber.io/
- **Selenium Documentation:** https://www.selenium.dev/documentation/
- **Gherkin Syntax:** https://cucumber.io/docs/gherkin/
- **Maven Guide:** https://maven.apache.org/guides/

### Tools & Extensions

- **IntelliJ Cucumber Plugin:** Built-in (File → Settings → Plugins → search "Cucumber")
- **Allure Report:** https://docs.qameta.io/allure/
- **Log4j2 Documentation:** https://logging.apache.org/log4j/2.x/

---

**Last Updated:** July 2, 2026
**Framework Version:** 1.0-SNAPSHOT  
**Java Compatibility:** Java 25+  
**Maven Compatibility:** Maven 3.6.0+

