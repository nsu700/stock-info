import pandas as pd
from sqlalchemy import create_engine
import numpy as np
import requests # Make sure requests is imported
import os

# --- 1. Fetch data from Wikipedia with a browser User-Agent ---
link = (
    "https://en.wikipedia.org/wiki/List_of_S%26P_500_companies#S&P_500_component_stocks"
)

# Set headers to mimic a real browser request
headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
}

# Use requests to get the HTML content
try:
    response = requests.get(link, headers=headers)
    response.raise_for_status() # This will raise an error for bad responses (4xx or 5xx)
    # This now reads the HTML content from the response text
    df = pd.read_html(response.text, header=0)[0]
except requests.exceptions.HTTPError as err:
    print(f"HTTP Error occurred: {err}")
    exit() # Exit the script if the page can't be fetched
except Exception as err:
    print(f"An other error occurred: {err}")
    exit()

# --- 2. Clean up column names for database compatibility ---
df.columns = [col.lower().replace(' ', '_').replace('.', '') for col in df.columns]
df.rename(columns={'security': 'name', 'gics_sector': 'sector'}, inplace=True)

# Append id as primary key
df['id'] = np.arange(1, len(df) + 1) # Start id from 1 for convention

# List all the column names you want to get rid of.
# Note: Cleaned names might be different, verify with a print statement if needed
columns_to_drop = ['gics_sub-industry', 'cik', 'founded', 'date_added', 'headquarters_location']
# Use a loop with a try-except block to safely drop columns that exist
for col in columns_to_drop:
    if col in df.columns:
        df.drop(columns=[col], inplace=True)

print("Available columns after cleaning:")
print(list(df.columns))
print("-" * 30)

# --- 3. Set up the database connection ---
db_user = os.getenv('DB_USER', 'your_username')
db_password = os.getenv('DB_PASSWORD', 'your_password')
db_host = os.getenv('DB_HOST', 'localhost')
db_port = os.getenv('DB_PORT', '5432')
db_name = os.getenv('DB_NAME', 'stock')

engine = create_engine(f'postgresql+psycopg2://{db_user}:{db_password}@{db_host}:{db_port}/{db_name}')

# --- 4. Write the DataFrame to the PostgreSQL database ---
table_name = 'stock_info'
df.to_sql(table_name, engine, if_exists='replace', index=False)

print(f"Successfully wrote {len(df)} records to the '{table_name}' table in the '{db_name}' database.")