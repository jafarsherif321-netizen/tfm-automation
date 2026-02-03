# TFM Automation Framework

## Overview

This is a **Java + Gradle automation framework** for testing the TFM web application.
It supports **UI testing** using Selenium WebDriver, **API testing** using RestAssured, and **reporting** with ExtentReports.

The framework follows a **Hybrid Automation Framework** approach and includes:

- **Page Object Model (POM)** design pattern
- **Singleton WebDriver** implementation for centralized driver management and POM initialization
- **TestNG** as the test execution and test management framework
- **Extent Reports** for rich and interactive test reporting
- **Data-driven testing** support using **Apache POI**

---

## Initial Author

Initial framework design, base setup, and core automation implementation
**Jafar Sherif**

---

## Folder Structure

```
tfm-automation/
├── build.gradle                  # Gradle build file
├── settings.gradle               # Gradle settings
├── src/
│   ├── main/
│   │   ├── java/com/tfm/automation/
│   │   │   ├── config/          # ConfigReader, EnvironmentManager
│   │   │   ├── core/            # BaseTest, BasePage
│   │   │   ├── driver/          # DriverManager, WebDriver setup
│   │   │   ├── reporting/       # ExtentReports setup
│   │   │   └── utils/           # FileUtils, WaitUtils, JsonUtils
│   │   └── resources/
│   │       ├── config.properties
│   └── test/
│       ├── java/com/tfm/automation/
│       │   ├── pages/           # Page Objects (LoginPage, DashboardPage, etc.)
│       │   ├── tests/
│       │   │   ├── ui/          # UI test classes
│       │   │   ├── api/         # API test classes (future)
│       │   │   └── mobile/      # Mobile test classes (future)
│       │   ├── dataprovider/    # TestNG DataProviders
│       └── resources/
│           ├── locators/        # External locator files (optional)
│           ├── testdata/        # JSON/CSV/Excel test data
│           └── testng.xml       # TestNG suite configuration
└── .gitignore
```

---

## Prerequisites

- **Java 21** (or compatible version)
- **Gradle** (wrapped or installed)
- IDE: **IntelliJ IDEA** or **VS Code**
- ChromeDriver (or driver for your browser) in PATH

---

## Setup

1. Clone the repository:

```bash
git clone https://github.com/jafarsherif321-netizen/tfm-automation.git
cd tfm-automation
```

2. Build the project:

```bash
./gradlew build
```

3. Ensure `config.properties` contains your environment info:

```properties
baseUrl=https://dev.trulyfree.com/
browser=chrome
timeout=10
```

---

## Running Tests

### Run all tests via Gradle

```bash
./gradlew test
```

### Run specific TestNG XML suite

```bash
./gradlew test --tests "com.tfm.automation.tests.ui.LoginTest"
```

## Adding Test Data

- Place JSON, CSV, or Excel files in:

```
src/test/resources/testdata/
```

- Use `DataProvider` classes to read and feed tests dynamically.

---

## Reporting

- ExtentReports generates **HTML reports** in `target/reports/extent/`
- Configure report path in `reporting/ExtentManager.java`

---

## Code Conventions

- **Packages/folders** are lowercase
- **Page classes** under `pages/`
- **Test classes** under `tests/ui` or `tests/api`
- **Data providers** under `dataprovider/`
- **Reusable utilities** under `utils/`

---

## Components integrated so far

- **Created framework and pushed to GitHub**
- **Added dependencies(Selenium, TestNG, Apache POI, ExtentReports )**
- **Added xml file and verified if it is working fine for parallel and cross browser testing**
- **Added singleton driver factory and verified that it is working fine**
- **Integrated Apache POI & Listeners**
- **Added Utility class for code reuseablity**
- **Integrated POM**
- **Integrated ExtentReports and verified working fine**

---

## TODO - Pending

- **Improve Utility class**
- **Increase Test scope**
- **Generate list of test cases to automate**

---

## Longterm future goals

- **Script refactor**
- **API automation using RestAssured**
- **Mobile automation using Appium**

---
