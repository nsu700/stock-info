# S&P 500 Company Data Scraper

This Python script scrapes the list of S&P 500 component stocks from Wikipedia, cleans the data, and saves it into a PostgreSQL database table. The script is designed to be run as a standalone process or containerized with Docker for easy execution.

---

## Prerequisites

Before you begin, ensure you have the following installed:
- Docker
- Postgresql with database created

---

## Local Execution

### 1. Build the docker image
```
docker build -t stock-scraper .
```

### 2. How to run it
```
docker run --rm \
  -e DB_USER=your_username \
  -e DB_PASSWORD=your_password \
  -e DB_HOST=host.docker.internal \
  -e DB_PORT=5432 \
  -e DB_NAME=stock_db \
  stock-scraper
```

### Table structure

#### stock_ohlc_data

```
CREATE TABLE stock_ohlc_data (
    id SERIAL PRIMARY KEY,
    symbol VARCHAR(10) NOT NULL,
    timestamp_utc TIMESTAMP WITH TIME ZONE NOT NULL,
    open_price DECIMAL(19, 4),
    high_price DECIMAL(19, 4),
    low_price DECIMAL(19, 4),
    close_price DECIMAL(19, 4),
    adj_close_price DECIMAL(19, 4),
    volume BIGINT,
    CONSTRAINT unique_ohlc_record UNIQUE (symbol, timestamp_utc)
);
```