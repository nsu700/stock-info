import pandas as pd
from sqlalchemy import create_engine
import numpy as np
import os

# --- 1. Fetch data from Wikipedia ---
link = (
    "https://en.wikipedia.org/wiki/List_of_S%26P_500_companies#S&P_500_component_stocks"
)
# This reads the first table from the Wikipedia page
df = pd.read_html(link, header=0)[0]

# --- 2. Clean up column names for database compatibility ---
# The original column names have spaces and special characters.
# Let's make them more database-friendly (e.g., lowercase, no spaces).
df.columns = [col.lower().replace(' ', '_').replace('.', '') for col in df.columns]

# You can rename specific columns if you like
df.rename(columns={'security': 'name', 'gics_sector': 'sector'}, inplace=True)

# Append id as primary key
df['id'] = np.arange(0, len(df))

# List all the column names you want to get rid of.
columns_to_drop = ['gics_sub-industry', 'cik', 'founded', 'date_added', 'headquarters_location']
df.drop(columns=columns_to_drop, inplace=True)

print("Available columns after cleaning:")
print(list(df.columns))
print("-" * 30)

# --- 3. Set up the database connection ---
db_user = os.getenv('DB_USER', 'your_username')
db_password = os.getenv('DB_PASSWORD', 'your_password')
db_host = os.getenv('DB_HOST', 'localhost')
db_port = os.getenv('DB_PORT', '5432')
db_name = os.getenv('DB_NAME', 'stock')

# Create the database engine
engine = create_engine(f'postgresql+psycopg2://{db_user}:{db_password}@{db_host}:{db_port}/{db_name}')

# --- 4. Write the DataFrame to the PostgreSQL database ---
# 'stock' will be the name of your table
# if_exists='replace' will drop the table if it already exists and create a new one.
# Use 'append' if you want to add data to an existing table.
table_name = 'stock_info'
df.to_sql(table_name, engine, if_exists='replace', index=False)

print(f"Successfully wrote {len(df)} records to the '{table_name}' table in the '{db_name}' database.")
