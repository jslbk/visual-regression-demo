# Visual Regression Demo — Automated UI Snapshot Testing & CI Integration

This project demonstrates a **Java-based visual regression testing framework** using Playwright, focused on **UI consistency validation**, **snapshot comparison**, and **CI execution with configurable parameters**.

<p align="center">
  <img src="media/Playwright-banner.png" alt="Project Banner" width="900"/>
</p>

---

## Table of Contents

* [Tools](#tools)
* [What This Project Demonstrates](#what-this-project-demonstrates)
* [Test Execution Flow](#test-execution-flow)
* [Running Tests Locally](#running-tests-locally)
* [Running in Jenkins (CI)](#running-in-jenkins-ci)
* [Visual Comparison Results](#visual-comparison-results)

---

## Tools

<p align="center">
  <img src="media/icons/Java.svg" width="40"/>
  <img src="media/icons/Gradle.svg" width="40"/>
  <img src="media/icons/Junit5.svg" width="40"/>
  <img src="media/icons/Playwright.svg" width="40"/>
  <img src="media/icons/Allure.svg" width="40"/>
  <img src="media/icons/Jenkins.svg" width="40"/>
</p>
---

## Test Execution Flow

<p align="center">
  <img src="media/flow.png" width="800"/>
</p>

1. Open target page using Playwright
2. Apply configured viewport (profile)
3. Capture screenshot (actual)
4. Load baseline image
5. Compare images pixel-by-pixel
6. Generate diff (if mismatch exists)
7. Attach results to Allure report
8. Fail test if threshold exceeded

---

## Running Tests Locally

Run tests with default configuration:

```bash
./gradlew clean test
```

Run with custom parameters:

```bash
./gradlew clean test \
  -Dvisual.browser=chromium \
  -Dvisual.profile=desktop
```

### Available parameters:

* `visual.browser`
    * chromium
    * firefox
  
* `visual.profile` (viewport presets)
    * desktop
    * tablet
    * mobile

---

## Running in Jenkins (CI)

This project supports **fully parameterized execution in Jenkins**.

### Pipeline parameters

* **BROWSER** → selects browser (chromium / firefox)
* **PROFILE** → selects viewport size

---

### Jenkins Job Configuration

<p align="center">
  <img src="media/jenkins-params.png" width="700"/>
</p>

* Parameters are defined in Jenkins UI
* User selects browser + viewport before execution

---

### Pipeline Execution

```bash
./gradlew clean test \
  -Dvisual.browser=${params.BROWSER} \
  -Dvisual.profile=${params.PROFILE}
```

---

### Jenkins Pipeline Run

<p align="center">
  <img src="media/jenkins-run.png" width="800"/>
</p>

* Build is triggered with selected parameters
* Tests run in выбранном браузере
* Screenshots are generated during execution

---

### Test Results (Allure Report)

<p align="center">
  <img src="media/allure-overview.png" width="800"/>
</p>

* Each test contains visual attachments
* Easy navigation between test results

---

### Visual Attachments in Allure

<p align="center">
  <img src="media/allure-attachments.png" width="800"/>
</p>

Each test includes:

* Baseline image
* Actual screenshot
* Diff image (highlighted differences)