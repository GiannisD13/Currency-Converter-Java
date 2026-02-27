# Currency Converter

A Java Swing desktop application for real-time cryptocurrency and currency conversion, built in 2025 as a university assignment.

## Description

Currency Converter is a desktop GUI application that allows users to look up live exchange rates between cryptocurrencies and fiat currencies using the [CoinAPI](https://www.coinapi.io/) service. Users can register an account, log in, perform conversions, save favourite currency pairs, and set up email price alerts via Gmail that notify them when a selected coin reaches a target price.

---

## Requirements

- **Java 24** (JDK 24 or later)
- **Apache Maven** (for building and dependency management)
- **A CoinAPI key** — required for exchange rate lookups. Copy `config.properties.example` to `config.properties` and fill in your key (see Setup below).
- **A Google Cloud project** with the Gmail API enabled, and an OAuth 2.0 `client_secret.json` credentials file (for the email notification feature).

---

## Setup

### 1. Clone the repository

```bash
git clone <repository-url>
cd CurrencyConverter-master-final
```

### 2. Add the CoinAPI key

Copy the example config file and fill in your CoinAPI key:

```bash
cp src/main/resources/config.properties.example src/main/resources/config.properties
```

Then edit `config.properties`:

```
coinapi.key=YOUR_COINAPI_KEY_HERE
```

You can get a free key at [coinapi.io](https://www.coinapi.io/).

> **Note:** `config.properties` is excluded from version control. Never commit it.

### 3. Add the Gmail credentials file

The email notification feature requires a Google OAuth2 credentials file.

1. Go to the [Google Cloud Console](https://console.cloud.google.com/)
2. Create a project and enable the **Gmail API**
3. Create **OAuth 2.0 Desktop App** credentials
4. Download the credentials and save the file as:

```
src/main/resources/client_secret.json
```

> **Note:** This file is excluded from version control for security reasons. Without it, the app will show an error on startup but currency conversion will still work.

### 4. Build the project

```bash
mvn clean package
```

### 5. Run the application

```bash
mvn exec:java -Dexec.mainClass="org.example.Main"
```

Or run the compiled JAR directly:

```bash
java -jar target/CurrencyConverter-1.0-SNAPSHOT.jar
```

---

## First Run

- On first launch, a **Login** window will appear.
- Use **Sign Up** to create a new account. Credentials are stored locally in `user.yaml`.
- After logging in, the main converter window opens.
- If `client_secret.json` is present, the first time the email notification feature is used, a **browser window will open** asking you to authorize the app to send emails via your Google account. After authorizing, the token is saved in the `tokens/` folder and reused for future sessions.

---

## Dependencies

Managed automatically by Maven:

| Library | Version | Purpose |
|---|---|---|
| OkHttp | 4.10.0 | HTTP requests to CoinAPI |
| Jackson Databind | 2.15.0 | JSON parsing |
| org.json | 20230227 | JSON handling |
| SnakeYAML | 2.2 | Reading/writing user data |
| Google API Client | 2.0.0 | Google OAuth2 integration |
| Google OAuth Client Jetty | 1.34.1 | Local OAuth2 authorization server |
| Google Gmail API | v1 | Sending email notifications |
| javax.mail | 1.4.7 | Building MIME email messages |

---

## Security Notes

The following files contain sensitive data and are excluded from version control via `.gitignore`:

- `src/main/resources/config.properties` — CoinAPI key (use `config.properties.example` as a template)
- `src/main/resources/client_secret.json` — Google OAuth2 client credentials
- `tokens/` — stored OAuth2 access tokens
- `user.yaml` — local user accounts and passwords
