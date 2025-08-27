# Project To-Do List

---
## Project Description: Stock Market Heatmap Visualizer

This is a full-stack web application designed to provide a real-time visualization of the S&P 500 stock market through an interactive heatmap.

The backend is built with **Spring Boot** and is responsible for orchestrating data collection and delivery. A **Python** script initially populates a **PostgreSQL** database with all S&P 500 company information. The backend then uses scheduled tasks to periodically fetch real-time quote and profile data from the **Finnhub API**, persisting this information for historical analysis. This data is exposed through a clean **REST API**.

The frontend is a single-page application that consumes the backend API to render an interactive heatmap, where stocks are grouped by sector, colored by price change, and sized by market capitalization. The application is fully containerized using **Docker** and designed for deployment on **OpenShift**, demonstrating an end-to-end, cloud-native development lifecycle.

---
### Phase 1: Foundations and Initial Data 🏗️

* [ ] **Project Setup:**
    * [ ] Initialize a Git repository for version control.
    * [ ] Create the Spring Boot project with the necessary dependencies: `Spring Web`, `Spring Data JPA`, `PostgreSQL Driver`, and `Lombok`.
    * [ ] Set up the PostgreSQL database instance in OpenShift.
* [ ] **Initial Data Seeding (Step 1):**
    * [ ] Finalize the Python script to scrape the S&P 500 company list from Wikipedia.
    * [ ] Define the database schema and create the `companies` table in PostgreSQL.
    * [ ] Run the script to populate the `companies` table with the initial list of stocks.

---
### Phase 2: Backend API for the Heatmap (Step 2)

* [ ] **Data Persistence Layer (JPA):**
    * [ ] Create the `@Entity` classes that map to the database tables: `Company`, `StockPrice`, and `DailyStockSummary`.
    * [ ] Create the corresponding `JpaRepository` interfaces for each entity.
* [ ] **External API Integration:**
    * [ ] Create a `FinnhubClient` `@Service` to handle all communication with the Finnhub API.
    * [ ] Implement methods in `FinnhubClient` to call the `/quote` and `/profile` endpoints.
    * [ ] Create DTOs (`FinnhubQuoteDTO`, `FinnhubProfileDTO`) to map the JSON responses from Finnhub.
* [ ] **Business Logic and API Endpoint:**
    * [ ] Create the `HeatmapService` to orchestrate calls to the `FinnhubClient`.
    * [ ] Implement the logic in `HeatmapService` to fetch data from both Finnhub endpoints for each stock and combine it into a final `HeatmapDTO`.
    * [ ] Create the `StockController` with a single endpoint: `GET /api/stocks/heatmap-data`.
    * [ ] **Test the endpoint** 

---
### Phase 3: Frontend Heatmap Implementation (Step 3)

* [ ] **Frontend Setup:**
    * [ ] Create the basic `index.html`, `style.css`, and `script.js` files inside the Spring Boot project's `src/main/resources/static/` directory.
    * [ ] Choose and include a frontend charting/visualization library that supports heatmaps (**D3.js**).
* [ ] **Data Fetching and Rendering:**
    * [ ] In `script.js`, write a `fetch` call to the backend endpoint (`/api/stocks/heatmap-data`).
    * [ ] Write the JavaScript logic to process the JSON response and pass it to the chosen library to render the heatmap.
    * [ ] Configure the heatmap to use `sector` for grouping, `priceChangePercentage` for color, and `marketCap` for the area of each rectangle.

---
### Phase 4: Stock Detail Page (Steps 4 & 5)

* [ ] **Backend Enhancements:**
    * [ ] Create the `DataInitializationService` to fetch and save historical data for each stock into the `daily_stock_summary` table.
    * [ ] Implement the new API endpoints in the `StockController`:
        * `GET /api/stocks/{symbol}` to return company details.
        * `GET /api/stocks/{symbol}/price-history` to return the OHLC data.
    * [ ] Test these new endpoints thoroughly.
* [ ] **Frontend Enhancements:**
    * [ ] Create a new `stock-detail.html` page.
    * [ ] In `script.js`, add a click event listener to the heatmap rectangles.
    * [ ] When a stock is clicked, the event handler should redirect the user to `stock-detail.html`, passing the stock symbol as a URL parameter (e.g., `stock-detail.html?symbol=AAPL`).
    * [ ] On the detail page, write JavaScript to read the symbol from the URL, call the two new backend APIs, and display the company information and a historical price chart.

---
### Phase 5: Finalization and Deployment 🚀

* [ ] **Containerization & Deployment Prep:**
    * [ ] Finalize the `Dockerfile` for the Spring Boot backend.
    * [ ] Write the OpenShift deployment configuration (YAML files) for the frontend, backend, and database.
    * [ ] Configure the init container logic to run the data seeding process.
* [ ] **Deployment:**
    * [ ] Deploy the entire application to OpenShift.
    * [ ] Configure the PostgreSQL Persistent Volume (PV).
    * [ ] Verify that all components are running and communicating correctly.
* [ ] **Final Touches:**
    * [ ] Perform end-to-end testing of the full application.
    * [ ] Refine the UI and add any final styling.
    * [ ] Prepare the project documentation and presentation.