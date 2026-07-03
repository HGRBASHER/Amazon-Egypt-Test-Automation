# Amazon Project - TestNG Automation Framework

---

# 1. About the Project

## Purpose

This repository contains an automated UI test suite for the Amazon e-commerce website, specifically configured to test the Egyptian localization (`https://www.amazon.eg/`). The suite automates critical user flows to detect regressions and ensure core functionality works as expected.

## Business Problem Solved

Manual testing of e-commerce flows is time-consuming and prone to human error. This project provides:
- **Fast feedback** on changes through automated regression tests
- **Consistent test execution** with reproducible results
- **Detailed reporting** via Allure attachments (screenshots + logs on failure)
- **Maintainability** through well-structured Page Objects and reusable utilities

## Project Goals

1. Automate critical e-commerce workflows end-to-end
2. Provide a maintainable, scalable test framework for teams
3. Enable continuous integration testing
4. Capture failures with screenshots and logs for debugging

## What is Automated

- **Search functionality** — validating homepage loads and search returns correct results
- **Results filtering** — by brand and price ranges
- **Results sorting** — price (low-to-high, high-to-low), customer reviews, best sellers, newest arrivals
- **Product details page** — verifying title, price parsing, availability, UI elements
- **Add to cart** — single and multiple items
- **Cart operations** — update quantities, remove items, verify subtotals
- **Inventory limits** — prevent exceeding stock availability

## Key Benefits

- **Beginner-friendly**: Clear documentation and structured code for learning
- **Reusable patterns**: Page Object Model for easy test creation
- **Failure insights**: Automatic screenshot capture and log attachment on test failures
- **Flexible**: Supports Chrome, Firefox, and Edge browsers

## Intended Audience

- **Manual QA engineers** learning test automation
- **Junior automation engineers** developing skills in Selenium + TestNG
- **Java developers** joining the automation team
- **Experienced automation engineers** extending the suite

---

# 2. What is Selenium & TestNG?

If you're new to automation, here's what each piece does in plain English:

## Automation Testing

**What:** Using code/scripts to automatically run tests instead of a human clicking buttons and checking results.

**Why:** Humans are slow and make mistakes. Automated tests run the same steps thousands of times with zero fatigue and zero deviation.

**Analogy:** If manual testing is driving a car to inspect for defects, automation is hiring a robot to drive the same route every day and report exactly what changed.

## Selenium WebDriver

**What:** A library that remotely controls a real web browser (Chrome, Firefox, Edge) through code.

**How it works:**
1. You write code: `driver.get("https://amazon.eg")`
2. Selenium sends that command to the browser
3. The browser loads the page
4. You can then interact: click buttons, type text, read page content

**Why use it:** Tests run in real browsers, testing real user scenarios (not just APIs).

## TestNG

**What:** A testing framework that helps you organize, run, and report on tests. Think of it as a structured way to write and execute test methods.

**Key features:**
- **@Test** — marks methods as tests
- **@BeforeMethod / @AfterMethod** — setup/cleanup for each test
- **Grouping** — run only certain tests
- **Parallel execution** — run multiple tests at once
- **Reporting** — integrates with reporting tools

**Analogy:** TestNG is the coach organizing athletes' training sessions; Selenium is the athlete actually training.

## Maven

**What:** A build tool for Java projects. It manages dependencies (3rd-party libraries) and orchestrates the build process.

**How:**
1. You list dependencies in `pom.xml` (XML file)
2. Run `mvn install` — Maven downloads everything automatically
3. Run `mvn test` — Maven compiles code and runs tests

**Why:** Eliminates manual JAR file management; reproducible builds across machines.

## WebDriverManager

**What (Not in this project currently):** A utility that automatically downloads the correct browser driver version for your system.

**This project:** Currently does NOT include WebDriverManager. Browser drivers must be installed separately and available in system PATH.

## Page Object Model (POM)

**What:** A design pattern where each web page is represented as a Java class.

**Example from this project:**
```java
// Instead of scattered locators:
searchField = driver.findElement(By.id("twotabsearchtextbox"));
searchButton = driver.findElement(By.id("nav-search-submit-button"));

// You create a Page Object:
public class SearchPage extends BasePage {
    private By searchField = By.id("twotabsearchtextbox");
    private By searchButton = By.id("nav-search-submit-button");
    
    public SearchResultsPage searchForProduct(String productName) {
        writeText(searchField, productName);
        clickOnElement(searchButton);
        return new SearchResultsPage(driver);
    }
}

// Tests use it simply:
SearchResultsPage results = searchPage.searchForProduct("Mouse");
```

**Benefits:**
- Locators are in one place (easy to maintain when the app changes)
- Tests read like English (reusable business logic)
- Multiple tests share the same page interactions

---

# 3. Technologies Used

| Technology | Version | Purpose | Used in Project |
|---|---|---|---|
| Java (compiler) | 17 | Language for tests & framework | Yes |
| Maven | Latest Stable Version | Build tool & dependency manager | Yes |
| Selenium Java | 4.39.0 | Browser automation | Yes |
| TestNG | 7.10.2 | Test framework & reporting | Yes |
| Allure TestNG | 2.34.0 | Test reporting with attachments | Yes |
| Log4j2 | 2.25.4 | Logging framework | Yes |
| Gson | 2.13.2 | JSON parsing for test data | Yes |
| Apache Commons CSV | 1.9.0 | CSV utilities | Declared (not actively used) |
| Apache POI | 5.4.1 | MS Office file handling | Declared (not actively used) |
| Maven Surefire | 3.2.3 | Runs TestNG suite during `mvn test` | Yes |

---

# 4. Framework Architecture

```
┌──────────────────────────────────────────────────────────────┐
│                      TEST CLASSES                            │
│         (SearchTest, CartTest, ProductDetailsTest)           │
└──────────────────────────┬───────────────────────────────────┘
                           │ executes scenarios using
                           ▼
┌──────────────────────────────────────────────────────────────┐
│                    PAGE OBJECTS                              │
│   (SearchPage, SearchResultsPage, ProductDetailsPage,        │
│    CartPage, BasePage)                                       │
│   ⮡ encapsulate locators & page interactions                 │
└──────────────────────────┬───────────────────────────────────┘
                           │ delegates to
                           ▼
┌──────────────────────────────────────────────────────────────┐
│                     UTILITIES / REUSE                        │
│  - WebDriverFactory (driver creation & lifecycle)            │
│  - BaseTest (setup, teardown, Allure attachments)            │
│  - ConfigHandler (loads config.properties)                   │
│  - JSONFileManager (loads cartData.json)                     │
│  - ScreenShots (captures images)                             │
│  - RetryAnalyzer (retry logic)                               │
└──────────────────────────┬───────────────────────────────────┘
                           │ manages
                           ▼
┌──────────────────────────────────────────────────────────────┐
│                    WEBDRIVER INSTANCES                       │
│    (ChromeDriverClass, FireFoxDriverClass,                   │
│     EdgeDriverClass)                                         │
└──────────────────────────┬───────────────────────────────────┘
                           │ controls
                           ▼
┌──────────────────────────────────────────────────────────────┐
│                    REAL BROWSER                              │
│              (Chrome / Firefox / Edge)                       │
└──────────────────────────────────────────────────────────────┘
```

## Layer Explanations

- **Test Classes**: Write human-readable test scenarios. Example: `testAddMultipleProductsToCart()` — search, open products, add to cart, verify.
- **Page Objects**: Encapsulate web page interactions. Locators and click/type/read methods belong here, not in tests.
- **Utilities**: Shared tools (driver factory, configuration loading, screenshot capture, retry logic).
- **WebDriver**: Browser-specific initialization (Chrome with incognito mode, Firefox with defaults, Edge with defaults).
- **Real Browser**: The actual application under test.

---

# 5. Folder Structure

```
Amazon Project_TestNG/
├─ pom.xml                                 (Maven build config & dependencies)
├─ TestNG.xml                              (TestNG suite definition)
├─ README.md                               (this file)
│
├─ allure-report/                          (Generated Allure HTML report)
│  └─ index.html
│
├─ allure-results/                         (Generated Allure JSON results)
│  └─ [multiple .json files]
│
├─ logs/
│  └─ application.log                      (Application execution log)
│
├─ ScreenShots/                            (Failure screenshots)
│  └─ [test failure images].png
│
├─ src/
│  ├─ main/
│  │  └─ resources/
│  │     ├─ config.properties              (URL, browser selection)
│  │     └─ cartData.json                  (Test data: keywords, products, filters)
│  │
│  ├─ main/java/org/example/
│  │  ├─ BasePage.java                     (Base for all page objects; common WebDriver methods)
│  │  ├─ SearchPage.java                   (Amazon homepage; search interaction)
│  │  ├─ SearchResultsPage.java            (Search results; filtering, sorting, product selection)
│  │  ├─ ProductDetailsPage.java           (Product detail page; title, price, add-to-cart)
│  │  └─ CartPage.java                     (Shopping cart; item mgmt, quantities, subtotal)
│  │
│  └─ test/java/
│     ├─ Reuse/
│     │  ├─ BaseTest.java                  (Test setup/teardown, Allure attachments)
│     │  ├─ WebDriverFactory.java          (Driver creation & quit logic)
│     │  ├─ ChromeDriverClass.java         (Chrome-specific driver initialization)
│     │  ├─ FireFoxDriverClass.java        (Firefox-specific driver initialization)
│     │  ├─ EdgeDriverClass.java           (Edge-specific driver initialization)
│     │  ├─ ConfigHandler.java             (Loads config.properties)
│     │  ├─ JSONFileManager.java           (Loads cartData.json with dotted-key access)
│     │  ├─ RetryAnalyzer.java             (TestNG IRetryAnalyzer; retries failed tests)
│     │  └─ ScreenShots.java               (Captures screenshot files)
│     │
│     ├─ SearchTest.java                   (Test: homepage + search validation)
│     ├─ SearchResultsTest.java            (Tests: filtering, sorting, invalid search)
│     ├─ ProductDetailsTest.java           (Tests: product details, add/remove from cart)
│     └─ CartTest.java                     (Tests: add multiple items, update qty, inventory limits)
│
└─ target/                                 (Compiled classes, built artifacts)
```

## Key File Purposes

| File | Purpose |
|---|---|
| `pom.xml` | Declares dependencies (Selenium, TestNG, Allure, etc.) and Maven plugins |
| `TestNG.xml` | Defines which test classes run and in what order (suite: MySuite) |
| `config.properties` | Application URL and browser choice (url, browser) |
| `cartData.json` | Test data (search keywords, product names, filter values) |
| `BasePage.java` | Common WebDriver helper methods (findElement, writeText, clickOnElement, getText) |
| `BaseTest.java` | Setup (initialize config, JSON, driver, page objects) and teardown (quit driver, attach logs) |
| `*Test.java` classes | Business logic tests (SearchTest, CartTest, etc.) |

---

# 6. Project Prerequisites

## Required Software

| Prerequisite | Version | Why Needed |
|---|---|---|
| Java JDK | 17 | Compile & run Java tests. (pom.xml sets `<release>17`) |
| Maven | 3.6+ | Build tool; download dependencies & run `mvn` commands. Exact version not specified in repo; use recent 3.6 or 3.8+. Latest Stable Version for exact requirement |
| Git | Latest | Clone repository from version control |
| IntelliJ IDEA (or VS Code + extensions) | Latest Community | IDE for development & running tests. Not mandatory but recommended. |
| Chrome, Firefox, or Edge browser | Latest stable | The application under test runs in real browsers. Install at least one. |
| Browser driver executable | Matching browser version | chromedriver, geckodriver (Firefox), or msedgedriver. **Not included in repo.** Must be installed separately and added to system PATH or managed externally. |

## Browser Driver Setup

This project **does NOT include WebDriverManager**. You must manually ensure driver executables are available:

### Option A: Add to System PATH
1. Download the appropriate driver:
    - Chrome: [ChromeDriver](https://googlechromelabs.github.io/chrome-for-testing/)
    - Firefox: [GeckoDriver](https://github.com/mozilla/geckodriver/releases)
    - Edge: [MSEdgeDriver](https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/)
2. Extract to a folder (e.g., `C:\drivers` on Windows)
3. Add that folder to your system PATH environment variable

### Option B: Add WebDriverManager Dependency (Recommended)
See [Future Improvements](#22-future-improvements) for WebDriverManager integration.

---

# 7. Downloading the Project

## Via Git (Recommended)

```bash
git clone <REPOSITORY_URL> "Amazon Project_TestNG"
cd "Amazon Project_TestNG"
```

Replace `<REPOSITORY_URL>` with your actual repository URL.

## Via ZIP Download

1. Download ZIP from your Git host (GitHub, GitLab, etc.)
2. Extract to your desired folder
3. Open terminal/command prompt and navigate to the folder

## Opening in IntelliJ IDEA

1. Launch IntelliJ IDEA
2. **File** → **Open**
3. Select the `pom.xml` file in the project root
4. Click **Open**
5. When prompted, select **Open as Project**
6. IntelliJ detects Maven project and imports automatically

## Reloading Maven Dependencies (Command Line)

```bash
mvn -U clean install
```

- `-U` — force update of dependencies (useful if versions changed)
- `clean` — remove old builds
- `install` — download & compile

---

# 8. Project Configuration

## Configuration Files

### 1. `src/main/resources/config.properties`

**Actual content from repository:**

```properties
url=https://www.amazon.eg/
browser=chrome
```

| Property | Purpose | Accepted Values | Default | Notes |
|---|---|---|---|---|
| `url` | Base URL of application under test | Any valid URL | `https://www.amazon.eg/` | Change to test staging/production environments |
| `browser` | Browser to use for tests | `chrome`, `firefox`, `edge` (case-insensitive) | `chrome` | Must match an available browser on your system |

### 2. `src/main/resources/cartData.json`

**Actual content from repository:**

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
    "brandFilter": "Logitech"
  }
}
```

| Key Path | Purpose | Value |
|---|---|---|
| `search.validKeyword` | Search term used in tests | "Mouse" |
| `search.invalidKeyword` | Invalid search to test "no results" scenario | "asdfghjkl123456789" |
| `productDetails.prod1Name`, `prod2Name`, etc. | Search keywords for product detail tests | "Mixed Media", "Posters & Prints", etc. |
| `cart.product` | Search keyword for cart tests | "laptop" |
| `cart.prod1Index`, `prod2Index` | Product indices to select from search results | 0, 1 |
| `cart.quantities.increasedQty` | Target quantity for increase test | 5 |
| `cart.quantities.decreasedQty` | Target quantity for decrease test | 2 |
| `filterAndSort.PriceStep.minPriceStep` | Minimum price for filtering | "30" |
| `filterAndSort.PriceStep.maxPriceStep` | Maximum price for filtering | "130" |
| `filterAndSort.sortOption` | Sort option to test | "Price: Low to High" |
| `filterAndSort.brandFilter` | Brand name for filtering | "Logitech" |

## How Configuration is Loaded

- **ConfigHandler** (Reuse package): loads `config.properties` at test startup (`BaseTest.setUP()`)
- **JSONFileManager** (Reuse package): loads `cartData.json` using dotted-key notation

Example usage in tests:
```java
String url = configHandler.getValue("url");
String browser = configHandler.getValue("browser");
String keyword = jsonFileManager.getValue("search.validKeyword").toString();
```

## Missing Configuration

- **Log4j2.xml / Logback.xml**: No explicit logging configuration file found. Log4j2 is a declared dependency, but configuration is implicit. If you need specific log formatting or output location, add `log4j2.xml` to `src/main/resources`.

---

# 9. Understanding TestNG

## What is TestNG?

TestNG is a **testing framework for Java** designed to overcome limitations of JUnit. It provides:
- Flexible annotations for lifecycle hooks
- Test grouping and prioritization
- Parallel execution
- Advanced reporting
- Dependency management between tests

**Think of it as:** A framework that organizes and executes test methods in a structured, repeatable way with detailed reporting.

## Annotations Found in This Project

### @BeforeMethod

**When:** Runs **before each** `@Test` method

**Example from `BaseTest.java`:**

```java
@BeforeMethod
public void setUP(){
    jsonFileManager = new JSONFileManager("src/main/resources/cartData.json");
    log.debug("json File Manager initialized");
    configHandler = new ConfigHandler("src/main/resources/config.properties");
    log.debug("configHandler initialized");
    driver=WebDriverFactory.getDriver(configHandler.getValue("browser"));
    driver.get(configHandler.getValue("url"));
    searchPage = new SearchPage(driver);
    log.debug("DriverSingleton initialized");
    explicitWait = new WebDriverWait(WebDriverFactory.driver, java.time.Duration.ofSeconds(15));
    log.debug("Explicit wait initialized");
    softAssert = new SoftAssert();
    log.debug("SoftAssert initialized");
}
```

**Purpose:** Initialize WebDriver, load config/test data, create page objects.

### @AfterMethod

**When:** Runs **after each** `@Test` method (regardless of pass/fail)

**Example from `BaseTest.java` — two methods:**

```java
@AfterMethod
public void checkFail(ITestResult result) {
    try {
        if(result.getStatus() == ITestResult.FAILURE){
            File srcShoot = ScreenShots.getScreenShot(driver,"ScreenShots/"+result.getName()+".png");
            Allure.addAttachment("Screenshots",new FileInputStream(srcShoot));
            log.error("Test Failed: {} - Screenshot attached.", result.getName());
        }
    }catch (Exception e){
        log.error("Failed to take screenshot: {}", e.getMessage());
    }
}

@AfterMethod
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

**Purpose:**
- First method: capture screenshot if test failed
- Second method: close browser and attach logs to Allure report

### @Test

**When:** Marks a method as a test case to execute

**Example from `SearchTest.java`:**

```java
@Test(retryAnalyzer = Reuse.RetryAnalyzer.class)
public void testHomeAndSearch(){
    log.info("Testing Home and Search functionality");
    log.debug("Home Page opened successfully");
    softAssert.assertTrue(searchPage.isHomePageDisplayed(), "Search input is not displayed");
    String product = jsonFileManager.getValue("search.validKeyword").toString();
    log.debug("Performing search for product: {}", product);
    searchResultsPage = searchPage.searchForProduct(product);
    String actualSearchTitle = searchResultsPage.getSearchTitleText();
    softAssert.assertTrue(actualSearchTitle.contains(product), "Expected search title to contain '" + product + "' but found: " + actualSearchTitle);
    int productsCount = searchResultsPage.getProductsCount();
    softAssert.assertTrue(productsCount > 0, "Error: No products displayed in search results!");
    softAssert.assertAll();
}
```

**Key feature used here:** `retryAnalyzer = Reuse.RetryAnalyzer.class` — if test fails, retry up to 3 times.

## TestNG Annotations NOT Used in This Project

- `@BeforeSuite` — would run once before all tests
- `@AfterSuite` — would run once after all tests
- `@BeforeClass` — would run before a test class
- `@AfterClass` — would run after a test class
- `groups` attribute — for test grouping (all tests run regardless)
- `dependsOnMethods` — for test dependencies
- `enabled = false` — to disable tests
- `priority` — for test execution order

---

# 10. TestNG Lifecycle

## Execution Order (Per Test Method)

For each test method in `TestNG.xml`:

```
TestNG Suite Starts
    │
    ├─→ @BeforeMethod (BaseTest.setUP)
    │   ├─ Initialize JSONFileManager
    │   ├─ Initialize ConfigHandler
    │   ├─ Get WebDriver from WebDriverFactory
    │   ├─ Navigate to base URL
    │   ├─ Initialize all Page Objects
    │   └─ Initialize SoftAssert for assertions
    │
    ├─→ @Test (e.g., testHomeAndSearch)
    │   ├─ Execute test business logic
    │   ├─ Make assertions using softAssert
    │   └─ Return (PASS or FAIL)
    │
    ├─→ @AfterMethod #1 (BaseTest.checkFail)
    │   ├─ IF test FAILED:
    │   │  ├─ Capture screenshot
    │   │  └─ Attach to Allure
    │   └─ IF test PASSED:
    │      └─ Do nothing
    │
    ├─→ @AfterMethod #2 (BaseTest.tearDown)
    │   ├─ Quit WebDriver
    │   ├─ Attach logs/application.log to Allure
    │   └─ Close all browser windows
    │
    └─→ Move to next @Test method (repeat)

TestNG Suite Ends
```

## Lifecycle Diagram

```
┌────────────────────────────────────────┐
│     TestNG Suite Execution Start       │
└────────────────┬───────────────────────┘
                 │
        ┌────────▼────────┐
        │  Read TestNG.xml│
        │  Load test list │
        └────────┬────────┘
                 │
    ┌────────────▼────────────────┐
    │  For each @Test method:     │
    └────────┬────────────────────┘
             │
    ┌────────▼────────────┐
    │   @BeforeMethod     │
    │   (setup driver)    │
    └────────┬────────────┘
             │
    ┌────────▼────────────┐
    │   Execute @Test     │
    │   (assertions)      │
    └────────┬────────────┘
             │
    ┌────────▼────────────────────┐
    │   @AfterMethod #1           │
    │   (capture screenshot)      │
    └────────┬────────────────────┘
             │
    ┌────────▼─────────────────────┐
    │   @AfterMethod #2            │
    │   (quit driver, attach logs) │
    └────────┬─────────────────────┘
             │
    ┌────────▼────────────────────┐
    │  Any more tests? → YES:     │
    │  Loop back to @BeforeMethod │
    │              → NO: End      │
    └─────────────────────────────┘
```

---

# 11. Install Dependencies

## What Maven Does

Maven (from `pom.xml`) will:
1. **Download** all declared dependencies (Selenium, TestNG, Allure, Log4j2, etc.) into `~/.m2/repository`
2. **Compile** your Java source code
3. **Run** tests (if you run `mvn test`)
4. **Package** artifacts into JAR/WAR files

## Installation Commands

### Full Install (Download Dependencies + Compile)

```bash
mvn clean install
```

- `clean` — removes old build artifacts
- `install` — downloads dependencies and compiles code

### Force Update (If Dependencies Changed)

```bash
mvn -U clean install
```

- `-U` — updates dependencies to latest versions in repositories

### Compile Only (No Tests)

```bash
mvn clean compile
```

## Expected Output

First run will download ~200MB+ of dependencies. Subsequent runs will be faster (cached locally).

```
[INFO] Downloading from central: https://repo.maven.apache.org/maven2/org/seleniumhq/selenium/selenium-java/4.39.0/selenium-java-4.39.0.pom
[INFO] Downloaded from central: https://repo.maven.apache.org/maven2/org/seleniumhq/selenium/selenium-java/4.39.0/selenium-java-4.39.0.pom
...
[INFO] BUILD SUCCESS
```

---

# 12. Run Tests

## Method 1: Maven (Full Suite via TestNG.xml)

**Command:**

```bash
mvn test
```

**What happens:**
1. Surefire plugin reads `TestNG.xml`
2. Runs all tests in suite order: SearchTest → SearchResultsTest → ProductDetailsTest → CartTest
3. Generates reports in `target/surefire-reports/`

## Method 2: Maven (Specific Test Class)

```bash
mvn -Dtest=SearchTest test
```

Replace `SearchTest` with any test class name.

## Method 3: Maven (Specific Test Method)

```bash
mvn -Dtest=SearchTest#testHomeAndSearch test
```

Format: `ClassName#methodName`

## Method 4: IntelliJ IDE

### Run All Tests

1. Right-click `TestNG.xml` in project root
2. Select **Run 'TestNG.xml'**

### Run Single Test Class

1. Open test file (e.g., `SearchTest.java`)
2. Right-click on class name
3. Select **Run 'SearchTest'**

### Run Single Test Method

1. Open test file
2. Click the green ▶ icon next to the test method
3. Select **Run**

## Method 5: Command Line with Groups (Not Implemented Here)

```bash
mvn -Dgroups=smoke test
```

**Note:** This project does not define TestNG groups. All tests run regardless of this parameter.

---

# 13. Test Reports

## Reporting Frameworks Used

### 1. Allure TestNG

**What it generates:**
- JSON result files with test metadata and attachments

**Where output is stored:**
- `allure-results/` — raw JSON files (overwritten on each test run)
- `allure-report/` — HTML report (if you generate it)

**How attachments work:**
- `BaseTest.java` attaches screenshots on failure: `Allure.addAttachment("Screenshots", ...)`
- `BaseTest.java` attaches application log: `Allure.addAttachment("Log File", ...)`

**How to view Allure report:**

Install Allure CLI first (if not already installed):
- **macOS:** `brew install allure`
- **Windows (Chocolatey):** `choco install allure`
- **Windows (Scoop):** `scoop install allure`

Then generate and serve report:

```bash
allure serve allure-results
```

Or generate static HTML:

```bash
allure generate allure-results -o allure-report --clean
```

Then open `allure-report/index.html` in a browser.

### 2. Maven Surefire

**What it generates:**
- XML and HTML test execution summary

**Where output is stored:**
- `target/surefire-reports/` — XML test results
- `target/site/surefire-report.html` — HTML summary

**How to view:**

```bash
mvn surefire-report:report
```

Then open `target/site/surefire-report.html` in a browser.

### 3. TestNG HTML Report

**Where output is stored:**
- `test-output/` folder (auto-generated by TestNG if run via IDE)

---

# 14. Test Groups

**This project currently does NOT implement TestNG groups.**

All tests in `TestNG.xml` run every time. There are no `groups` attributes in `@Test` annotations.

If you want to add groups in the future:

```java
@Test(groups = "smoke")
public void testHomeAndSearch() { ... }

@Test(groups = "regression")
public void testFilterAndSorting_1() { ... }
```

Then run:

```bash
mvn -Dgroups=smoke test        # Only smoke tests
mvn -Dgroups=regression test   # Only regression tests
```

---

# 15. Screenshots

## When Screenshots are Captured

**Only on test failure.**

Triggered in `BaseTest.checkFail()`:

```java
if(result.getStatus() == ITestResult.FAILURE){
    File srcShoot = ScreenShots.getScreenShot(driver,"ScreenShots/"+result.getName()+".png");
    Allure.addAttachment("Screenshots",new FileInputStream(srcShoot));
}
```

- If test PASSES: no screenshot
- If test FAILS: screenshot captured and attached to Allure

## Where Screenshots are Stored

**File path:** `ScreenShots/` folder in project root

**Example:** `ScreenShots/testHomeAndSearch.png`

## File Naming Convention

```
ScreenShots/<TEST_METHOD_NAME>.png
```

- `<TEST_METHOD_NAME>` comes from `result.getName()` (TestNG test method name)
- Example: if test method is `testAddMultipleProductsToCart_2`, filename will be `testAddMultipleProductsToCart_2.png`

## Implementation

Screenshot capture is done by `Reuse.ScreenShots` class:

```java
public static File getScreenShot(WebDriver driver, String filePath) throws IOException {
    TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
    File srcShoot = takesScreenshot.getScreenshotAs(OutputType.FILE);
    File destFile = new File(filePath);
    FileHandler.copy(srcShoot, destFile);
    return destFile;
}
```

---

# 16. Logging

## Logging Framework

The project uses **Apache Log4j2** (declared in `pom.xml`):

```xml
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-api</artifactId>
    <version>2.25.4</version>
</dependency>
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <version>2.25.4</version>
</dependency>
```

## Log Levels Used in Code

In various classes (SearchResultsPage, ProductDetailsPage, CartPage, test classes), you'll see:

```java
log.debug("Debug message");      // Detailed info for developers
log.info("Info message");        // General informational messages
log.warn("Warning message");     // Non-critical issues
log.error("Error message");      // Errors during execution
```

## Configuration File

**Status:** No explicit `log4j2.xml` or `logback.xml` found in the repository.

Log4j2 is using implicit/default configuration. If you need specific log output:
- Add `log4j2.xml` to `src/main/resources/`
- See [Future Improvements](#22-future-improvements)

## Where Logs are Stored

Log file exists in workspace: `logs/application.log`

This file is also attached to Allure reports on test teardown:

```java
Allure.addAttachment("Log File", new FileInputStream("logs/application.log"));
```

---

# 17. Test Coverage

This table reflects all test classes and methods found in the project. Do not assume coverage beyond what is listed.

| Module | Test Class | Positive Tests | Negative Tests |
|---|---|---|---|
| **Search** | `SearchTest` | `testHomeAndSearch()` — verifies homepage loads & search returns results | None explicit |
| **Search Results** | `SearchResultsTest` | `testFilterAndSorting_1()` — verifies brand filter, price range, and sorting | `testSearchWithInvalidInputScenario_4()` — tests invalid keyword, verifies "no results" message & page stability |
| **Product Details** | `ProductDetailsTest` | `testProductDetailsVerificationScenario_5()` — verifies title, price, availability, UI elements; `testVerifyAvailableProductDetails()` — validates product data & add-to-cart button; `testRemoveProductFromCartScenario()` — adds 2 products, removes 1, verifies remaining | None explicit |
| **Cart** | `CartTest` | `testAddMultipleProductsToCart_2()` — adds 2 products, verifies titles/prices/subtotal; `testUpdateCartQuantityScenario_3()` — increases & decreases quantity; `testInventoryLimitScenario()` — verifies quantity cap | `testInventoryLimitScenario()` includes edge case: exceeding inventory limit triggers warning |

---

# 18. Design Patterns & Best Practices

## 1. Page Object Model (POM)

**What it is:**
Each web page is a Java class. Locators and page-specific methods live in that class. Tests call page methods, never touch locators directly.

**Why it matters:**
- Locators change? Update one class, not 20 tests.
- Tests read like business logic, not technical gibberish.
- Easy to reuse across multiple tests.

**Where it appears in this project:**

```
src/main/java/org/example/
├─ BasePage.java          (shared methods: findElement, writeText, clickOnElement, getText)
├─ SearchPage.java        (homepage actions)
├─ SearchResultsPage.java (results page with filtering, sorting, product selection)
├─ ProductDetailsPage.java (product details actions)
└─ CartPage.java          (cart operations)
```

**Example:**

```java
// Without POM (scattered locators):
WebElement search = driver.findElement(By.id("twotabsearchtextbox"));
search.sendKeys("Mouse");
driver.findElement(By.id("nav-search-submit-button")).click();

// With POM (clean, reusable):
SearchResultsPage results = searchPage.searchForProduct("Mouse");
```

## 2. Driver Factory Pattern (Singleton-style)

**What it is:**
Centralized driver creation and lifecycle management. Only one driver instance per test.

**Why it matters:**
- Browser startup/shutdown logic in one place.
- Easy to switch browsers (config file).
- Thread-safe for parallel execution.

**Where it appears:**

```
src/test/java/Reuse/
├─ WebDriverFactory.java      (getDriver, quitDriver)
├─ ChromeDriverClass.java     (Chrome with incognito mode)
├─ FireFoxDriverClass.java    (Firefox)
└─ EdgeDriverClass.java       (Edge)
```

**Usage:**

```java
WebDriver driver = WebDriverFactory.getDriver("chrome");
// ... tests ...
WebDriverFactory.quitDriver();
```

## 3. Retry Analyzer Pattern

**What it is:**
Automatically retry flaky tests (up to 3 times) before marking as failed.

**Why it matters:**
- Reduces false negatives (network glitches, timing issues)
- Improves test reliability

**Where it appears:**

```java
// Reuse/RetryAnalyzer.java
public class RetryAnalyzer implements IRetryAnalyzer {
    private int retryCount = 0;
    private static final int maxCount = 3;

    @Override
    public boolean retry(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE && retryCount < maxCount) {
            retryCount++;
            return true;  // retry
        }
        return false;
    }
}
```

**Used in:**

```java
// SearchTest.java
@Test(retryAnalyzer = Reuse.RetryAnalyzer.class)
public void testHomeAndSearch() { ... }
```

## 4. Base Test Class Pattern

**What it is:**
Shared setup/teardown for all tests (driver init, config loading, Allure attachments).

**Why it matters:**
- Avoid code duplication in every test class
- Centralized lifecycle management
- Consistent test execution

**Where it appears:**

```java
// Reuse/BaseTest.java
public class BaseTest {
    @BeforeMethod
    public void setUP() { 
        // Initialize driver, config, page objects, etc.
    }
    
    @AfterMethod
    public void checkFail(ITestResult result) { 
        // Capture screenshot on failure
    }
    
    @AfterMethod
    public void tearDown() { 
        // Quit driver, attach logs
    }
}
```

**Used by:**

```java
// SearchTest.java, CartTest.java, etc.
public class SearchTest extends BaseTest { ... }
```

---

# 19. Troubleshooting

| Issue | Likely Cause | Solution |
|---|---|---|
| **WebDriverException: unknown error: Chrome driver could not start** | Browser driver (chromedriver) not found or not in PATH | Install chromedriver matching your Chrome version; add to system PATH or use WebDriverManager (see Future Improvements) |
| **Chrome version mismatch (driver incompatible)** | Browser was updated but driver binary is old | Download matching driver version or add WebDriverManager dependency |
| **Tests hang or timeout** | Explicit wait time too short or element never appears | Increase wait duration in `BasePage` or `WebDriverWait`; check if selector is correct |
| **Allure report fails to generate** | Allure CLI not installed; or `allure-results` folder empty | Install Allure CLI (`brew install allure` / `choco install allure`); run tests first |
| **Build fails: Java compilation error** | JDK version mismatch (pom.xml expects Java 17) | Install JDK 17; set `JAVA_HOME` to JDK 17 folder |
| **`logs/application.log` not created** | No Log4j2 configuration file; default behavior not writing | Add `log4j2.xml` to `src/main/resources` to define appenders and log file location (see Future Improvements) |
| **Screenshot folder not writable** | Permission issue on `ScreenShots/` directory | Ensure `ScreenShots/` folder exists and current user has write permissions; create manually if needed: `mkdir ScreenShots` |
| **Tests run but TestNG.xml not detected** | Surefire plugin misconfigured in pom.xml | Verify `pom.xml` contains correct `<suiteXmlFile>TestNG.xml</suiteXmlFile>` path; ensure `TestNG.xml` is in project root |
| **Maven dependency download fails** | Network issue or invalid repository URL | Check internet connection; try `mvn -U clean install` to force re-download |
| **Page object elements not found (NoSuchElementException)** | Locators outdated due to app UI changes | Update XPath/CSS selectors in page object class; verify with browser inspector |
| **Test passes locally but fails in CI/CD** | Browser configuration or environment variable difference | Ensure CI/CD pipeline has browser drivers installed; check `config.properties` URL is accessible from CI environment |

---

# 20. FAQ

**Q1: How do I change which browser the tests use?**

A: Edit `src/main/resources/config.properties` and change the `browser` value:
- `browser=chrome` (default)
- `browser=firefox`
- `browser=edge`

Ensure the corresponding driver executable is installed and in your system PATH.

---

**Q2: I want to run only one test. How?**

A: In IntelliJ:
1. Open the test file (e.g., `SearchTest.java`)
2. Click the green ▶ icon next to the test method name
3. Select **Run**

From command line:
```bash
mvn -Dtest=SearchTest#testHomeAndSearch test
```

---

**Q3: Where are test reports saved?**

A: Multiple locations:
- **Allure** → `allure-results/` (JSON) and `allure-report/` (HTML)
- **Surefire** → `target/surefire-reports/` (XML)
- **TestNG** → `test-output/` (if run from IDE)

To view Allure report:
```bash
allure serve allure-results
```

---

**Q4: A test failed. Where's the screenshot?**

A: Check `ScreenShots/` folder in project root. Screenshot name matches test method name:
- Test method: `testHomeAndSearch()` → Screenshot: `ScreenShots/testHomeAndSearch.png`

Screenshot is also attached to Allure report.

---

**Q5: How do I add a new test?**

A:
1. Create a new Java class in `src/test/java` extending `BaseTest`
2. Add `@Test` methods with your test logic
3. Use Page Objects (SearchPage, CartPage, etc.) for interactions
4. Add your test class to `TestNG.xml` if you want it in the main suite

Example:
```java
public class NewFeatureTest extends BaseTest {
    @Test
    public void testNewFeature() {
        // Use page objects
        searchResultsPage = searchPage.searchForProduct("Test");
        // Make assertions
        softAssert.assertTrue(searchResultsPage.getProductsCount() > 0);
        softAssert.assertAll();
    }
}
```

---

**Q6: What if the element I'm looking for doesn't exist on the page?**

A: You'll get a `NoSuchElementException` after the wait timeout. To debug:
1. Open the app in a browser manually
2. Right-click the element and **Inspect** to get the correct XPath/CSS
3. Update the locator in the corresponding Page Object class
4. Re-run the test

---

**Q7: How do I run tests in parallel?**

A: This project does NOT have parallel execution enabled. To enable:
1. Add `parallel="methods"` to `<suite>` tag in `TestNG.xml`:
   ```xml
   <suite name="MySuite" parallel="methods" thread-count="4">
   ```
2. Ensure tests are thread-safe (no shared state between tests)

---

**Q8: Can I run tests on different environments (staging/production)?**

A: Yes. Edit `src/main/resources/config.properties` and change the `url`:
```properties
url=https://staging.amazon.eg/
# or
url=https://www.amazon.eg/
```

Or parameterize via Maven:
```bash
mvn -Durl=https://staging.amazon.eg/ test
```

Then update `ConfigHandler` to accept system property override.

---

**Q9: Why do I need WebDriver executables if Selenium is installed?**

A: Selenium is a Java library that controls browsers programmatically. It communicates with browser driver executables (chromedriver, geckodriver, etc.) which must be installed separately. This project does NOT include WebDriverManager to auto-download drivers.

---

**Q10: How do I debug a failing test?**

A:
1. Take a screenshot (automatically saved on failure)
2. Check logs in `logs/application.log`
3. View Allure report with attachments: `allure serve allure-results`
4. Re-run the single failing test with debug mode in IDE

---

# 21. Future Improvements

These improvements are **NOT currently implemented** in the project. Adding them would enhance maintainability and functionality:

## 1. Add WebDriverManager Dependency

**What it does:** Automatically downloads the correct browser driver version for your system at runtime.

**Why valuable:**
- Eliminates manual driver management
- Works across different team members' machines without PATH setup
- Auto-updates drivers when browsers update

**Implementation:**
- Add to `pom.xml`:
  ```xml
  <dependency>
      <groupId>io.github.bonigarcia</groupId>
      <artifactId>webdrivermanager</artifactId>
      <version>5.7.0</version>
  </dependency>
  ```
- Update driver classes to use it:
  ```java
  public class ChromeDriverClass {
      public static WebDriver getChromeDriver() {
          WebDriverManager.chromedriver().setup();
          ChromeOptions options = new ChromeOptions();
          options.addArguments("--incognito");
          driver = new ChromeDriver(options);
          driver.manage().window().maximize();
          return driver;
      }
  }
  ```

---

## 2. Add Log4j2 Configuration File

**What it does:** Provides explicit control over logging output format, level, and file location.

**Why valuable:**
- Deterministic log file location
- Custom formatting for readability
- Control over which classes log at which levels

**Implementation:**
- Add `src/main/resources/log4j2.xml`:
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
          <Logger name="org.example" level="DEBUG"/>
          <Root level="INFO">
              <AppenderRef ref="Console"/>
              <AppenderRef ref="File"/>
          </Root>
      </Loggers>
  </Configuration>
  ```

---

## 3. Move Test Classes to Named Package

**What it does:** Organize test classes in a Java package (e.g., `com.amazon.tests`) instead of default package.

**Why valuable:**
- Better organization and IDE support
- Follows Java best practices
- Easier namespace management with multiple projects

**Implementation:**
- Create folder: `src/test/java/com/amazon/tests/`
- Move test files there
- Update `TestNG.xml` class references:
  ```xml
  <class name="com.amazon.tests.SearchTest"/>
  ```

---

## 4. Add TestNG Listeners for Centralized Logging

**What it does:** Implement `ITestListener` to handle onTestStart, onTestFailure, onTestSuccess events globally.

**Why valuable:**
- Centralized test event handling
- Avoid multiple `@AfterMethod` hooks
- Consistent logging across all tests

**Implementation:**
```java
public class TestListener implements ITestListener {
    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("Test started: " + result.getMethod().getMethodName());
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        // Capture screenshot and attach logs
    }
}
```

Register in `TestNG.xml`:
```xml
<listeners>
    <listener class-name="com.amazon.tests.TestListener"/>
</listeners>
```

---

## 5. Add CI/CD Workflow (GitHub Actions)

**What it does:** Automatically run tests on pull requests and pushes.

**Why valuable:**
- Catch bugs before merging code
- Build confidence in releases
- Automated Allure report publishing

**Implementation:**
- Create `.github/workflows/tests.yml`:
  ```yaml
  name: Run Tests
  on: [push, pull_request]
  jobs:
    test:
      runs-on: ubuntu-latest
      steps:
        - uses: actions/checkout@v2
        - uses: actions/setup-java@v2
          with:
            java-version: '17'
        - run: mvn clean test
        - name: Publish Allure Report
          uses: simple-elf/allure-report-action@master
  ```

---

## 6. Add TestNG Groups for Selective Execution

**What it does:** Organize tests into groups (e.g., "smoke", "regression", "sanity") for selective runs.

**Why valuable:**
- Run quick smoke tests before full regression
- Separate concerns (unit-level vs. full flows)
- Faster feedback in CI/CD

**Implementation:**
```java
@Test(groups = "smoke")
public void testHomeAndSearch() { ... }

@Test(groups = "regression")
public void testFilterAndSorting_1() { ... }
```

Then run:
```bash
mvn -Dgroups=smoke test   # Only smoke tests
```

---

## 7. Parameterize Tests for Multiple Environments & Browsers

**What it does:** Run the same test against different environments (staging/prod) and browsers.

**Why valuable:**
- Catch environment-specific bugs
- Cross-browser compatibility testing
- Reuse test code across multiple scenarios

**Implementation:** Use TestNG `@Parameters` or Maven profiles.

---

# 22. Author

# Hagar Basheer

---

# 23. Contact

- **LinkedIn:** http://linkedin.com/in/hagarbasheer
- **GitHub:** https://github.com/HGRBASHER
- **Email:** hagareid307@gmail.com

---

# 24. License

No license file detected in the repository. Defaulting to **MIT License**. Replace with your project's actual license if different.

```
MIT License

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

**Last updated:** July 2, 2026

For questions, issues, or contributions, please refer to the Contact section above or open an issue in the repository.

