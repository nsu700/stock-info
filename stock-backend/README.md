# Architecture Design

Target to deploy into OCP

# Deployment Design

- Database use postgresql in OCP with PV
- Frontend is a deployment with two main page template
    - The main page is a heatmap to show all stocks price
    - The sub page would be history of each single stock
- Backend is another deployment with two container
    - Init container to verify if there is any data in the postgresql, if no, call the python script to save basic data(Stock info and history data) into database
    - App container
        - Regularly call api to get current data and update into the database
        - Expose APIs for calling from frontend
            - To query for stock info
            - To query for stock price

## Database Design

The database name is `stock_db` with two tables:

- `stock_info` : this will store static information about a company with fields:
    - company name
    - symbol
    - sector
    - id
- `stock_price` : This store time-sensitive data, include fields:
    - id ⇒ Primary key
    - current price
    - timestamp
    - trading volume
    - price change percentage
    - market_cap
    - stock_id ⇒ Foreign key referenced to `stock` table
- `daily_stock_summary` : Just for OHCL info, probably would update once per day, include below fields:
    - id ⇒ Primary key
    - stock_id ⇒ Foreign key referenced to `stock` table
    - date
    - open_price
    - high_price
    - low_price
    - close_price
    - volume

## Backend Design

### One-off task:
DataInitializationService
  **Responsibility:** Handles the one-off task of populating the database with foundational data if it's empty. This is the logic for **init container**.
  **Actions:**
      1.  Fetch the list of S&P 500 companies.
      2.  Save this list to the `stock_info` table.
      3.  Fetch the last 30 days of historical data for each company and populate the `daily_stock_summary` table.


### Controller

The backend will expose the following RESTful, data-only endpoints. The API is the clear contract between the frontend and backend.

* `GET /api/stocks/heatmap-data`
    * **Description:** Provides the essential data needed to render the main heatmap page.
    * **Response Body:** A JSON array of stock data.
        ```json
        [
          {
            "symbol": "AAPL",
            "currentPrice": 12.32,
            "priceChangePercentage": 2.15,
            "volume": 95000000
          }
        ]
        ```

* `GET /api/stocks/{symbol}`
    * **Description:** Retrieves static company information for a single stock.
    * **Response Body:** A JSON object with details for the requested symbol (e.g., AAPL).
        ```json
        {
          "symbol": "AAPL",
          "name": "Apple Inc.",
          "sector": "Technology"
        }
        ```

* `GET /api/stocks/{symbol}/price-history`
    * **Description:** Provides the daily open, high, low, and close (OHLC) data for the stock's sub-page chart.
    * **Response Body:** A JSON array of historical daily summaries.
        ```json
        [
          {
            "date": "2025-08-24",
            "open": 145.50,
            "high": 148.00,
            "low": 145.00,
            "close": 147.80
          }
        ]
        ```

### Service

These services contain the main logic of the application.

* **`PriceUpdateService`**
    * **Responsibility:** This is a crucial **backend-only** service for keeping the price data current. The frontend should **never** be responsible for updating the database.
    * **Actions:**
        1.  **Scheduled Price Fetching:** Using Spring Boot's `@Scheduled` annotation, this service will run a task at a fixed interval (e.g., every 5 minutes).
        2.  For each stock in the `stock_info` table, it will call the Finnhub API to get the latest quote.
        3.  It will save this new quote as a new entry in the `stock_price` table.
        4.  **Daily Summary Job:** A separate scheduled task will run once per day (e.g., at midnight) to calculate and save the OHLC data to the `daily_stock_summary` table.

* **`StockQueryService`**
    * **Responsibility:** Provides the read-only logic to fetch data from the database and prepare it for the API controllers.
    * **Actions:** Implements the methods needed to serve the three API endpoints defined above.


### Data Persistence (Repository Layer)

* **Entities:** Create JPA `@Entity` classes (`StockInfo`, `StockPrice`, `DailyStockSummary`) that map directly to your database tables.
* **Repositories:** Define `JpaRepository` interfaces for each entity to handle all database interactions (saving, fetching, etc.).

---

## Data Transfer Objects (DTOs)

* **Purpose:** Use DTOs to shape the data specifically for your API responses. This decouples your API from your internal database structure.
* **Examples:**
    * `HeatmapDTO`: A class with fields `symbol`, `currentPrice`, `priceChangePercentage`, and `volume`.
    * `DailySummaryDTO`: A class with fields `date`, `open`, `high`, `low`, and `close`.

# Frontend Design

### What data need provide:

- stock symbol
- stock price change
- stock trade volume
- current price

Maybe like below:

```bash
[
  {
    "symbol": "AAPL",
    "priceChangePercentage": 2.15,
    "volume": 95000000,
    "currentPrice": 12.32
  },
  {
    "symbol": "GOOGL",
    "priceChangePercentage": -0.58,
    "volume": 34000000,
    "currentPrice": 12.32
  }
]
```