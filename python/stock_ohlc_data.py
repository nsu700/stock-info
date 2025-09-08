import os
import time
from datetime import datetime, timedelta
import pandas as pd
import requests
from sqlalchemy import create_engine, text

def get_db_engine():
    """Creates a SQLAlchemy engine from environment variables."""
    try:
        db_user = os.environ['DB_USER']
        db_password = os.environ['DB_PASSWORD']
        db_host = os.environ['DB_HOST']
        db_port = os.environ['DB_PORT']
        db_name = os.environ['DB_NAME']
        return create_engine(f'postgresql+psycopg2://{db_user}:{db_password}@{db_host}:{db_port}/{db_name}')
    except KeyError as e:
        print(f"Error: Environment variable {e} not set.")
        exit(1)

def get_stock_symbols(engine):
    """Fetches stock symbols from the stock table."""
    try:
        with engine.connect() as conn:
            result = conn.execute(text("SELECT symbol FROM stock"))
            symbols = [row[0] for row in result]
            return symbols
    except Exception as e:
        print(f"Error fetching symbols from database: {e}")
        return []

def fetch_yahoo_data(symbol):
    """Fetches the last 30 days of OHLC data for a given symbol."""
    now = datetime.now()
    thirty_days_ago = now - timedelta(days=30)
    period1 = int(time.mktime(thirty_days_ago.timetuple()))
    period2 = int(time.mktime(now.timetuple()))
    url_symbol = symbol.replace('.', '-')

    url = (
        f"https://query1.finance.yahoo.com/v8/finance/chart/{url_symbol}?"
        f"period1={period1}&period2={period2}&interval=1d&events=history"
    )
    
    headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
    }

    try:
        response = requests.get(url, headers=headers)
        response.raise_for_status()
        return response.json()
    except requests.exceptions.HTTPError as e:
        print(f"  -> HTTP Error for {symbol}: {e}")
    except Exception as e:
        print(f"  -> An error occurred for {symbol}: {e}")
    return None

def main():
    """Main execution function."""
    engine = get_db_engine()
    symbols = get_stock_symbols(engine)
    
    if not symbols:
        print("No symbols found in stock table. Exiting.")
        return

    print(f"Found {len(symbols)} symbols. Starting data fetch...")

    for symbol in symbols:
        print(f"Processing symbol: {symbol}")
        
        # --- PARSE AND SAVE DATA ---
        json_data = fetch_yahoo_data(symbol)
        
        if json_data and json_data.get('chart') and json_data['chart'].get('result'):
            result = json_data['chart']['result'][0]
            timestamps = result.get('timestamp', [])
            quote = result.get('indicators', {}).get('quote', [{}])[0]
            adj_close = result.get('indicators', {}).get('adjclose', [{}])[0].get('adjclose', [])

            if timestamps:
                df = pd.DataFrame({
                    'symbol': symbol,
                    'timestamp': pd.to_datetime(timestamps, unit='s', utc=True),
                    'open_price': quote.get('open', []),
                    'high_price': quote.get('high', []),
                    'low_price': quote.get('low', []),
                    'close_price': quote.get('close', []),
                    'adj_close_price': adj_close,
                    'volume': quote.get('volume', [])
                })
                
                # Drop rows with any missing data
                df.dropna(inplace=True)

                if not df.empty:
                    df.to_sql('stock_ohlc_data', engine, if_exists='append', index=False)
                    print(f"  -> Successfully saved {len(df)} records for {symbol}.")
                else:
                    print(f"  -> No valid data to save for {symbol}.")
            else:
                print(f"  -> No timestamps found for {symbol}.")
        
        # --- IMPORTANT: PAUSE TO AVOID RATE LIMITING ---
        time.sleep(1) # Wait for 1 second before the next symbol

    print("Finished processing all symbols.")

if __name__ == "__main__":
    main()