# ☕ The Art of code

This project contains independent Java examples demonstrating beautiful code and modern language features.  
Each example class can be **executed individually** using **Java 25** (with preview features enabled) and **Apache Maven**.

---

## ⚙️ Requirements

| Tool | Minimum Version | Notes |
|------|------------------|--------|
| **Java** | 25 | Uses `--enable-preview` for modern language features |
| **Maven** | 3.9.x | For build automation and dependency management |

Make sure your `JAVA_HOME` points to a JDK 25 installation.

---

## 🧱 Project Overview

The source code is organized by **chapters** and **listings**, matching the book’s structure:

- Each top-level package `chapterX` corresponds to a **chapter** in the book.
- Each subpackage or class `listingYY` corresponds to a **specific code listing** from that chapter.

For example:
chapter3.listing33.MarketingService

→ This means **Chapter 3**, **Listing 3.3** in the book.

Every listing is a **self-contained Java example** with its own `main()` method, so you can compile and run any listing independently.

### Supporting Packages

In addition to the `chapterX.listingYY` examples, the project includes several shared packages:

| Package | Purpose                                                                                                                                                                                                                                                                                                                                                      |
|----------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **`chapterX.domain`** | Contains simple *domain model* classes (e.g., `Customer`, `Product`, `Promotion`) used by the listings.                                                                                                                                                                                                                                                      |
| **`chapterX.exception`** | Defines custom exception types used in examples (e.g., `EmailException`, `MissingPriceException`).                                                                                                                                                                                                                                                           |
| **`chapterX.mock`** | Provides *mock or stub implementations* (e.g., `EmailService`, `PromotionService`, `UserService`) used to simulate dependencies without real infrastructure.                                                                                                                                                                                                 |
| **`chapterX.snippet`** | Contains *independent illustrative examples* used throughout each chapter. These snippets are **not tied to a specific listing number** but demonstrate supporting concepts. |

These packages keep the code examples lightweight and focused on logic rather than external systems.

## ▶️ Run an Example

Some classes include their own main() entry point and can be executed independently.

For example, to run chapter3.listing33.MarketingService:

### Using Maven
mvn exec:java \
-Dexec.mainClass="chapter3.listing33.MarketingService" \
-Dexec.args="--enable-preview"

### using the JAR directly
java --enable-preview \
-cp target/artOfCode-1.0.0.jar \
chapter3.listing33.MarketingService

### Launch from Your IDE

You can also run each example directly from your IDE, for instance in IntelliJ IDEA:

Navigate to the class you want to run (for example MarketingService).

Click the green start icon next to the main() method, or right-click → Run.
