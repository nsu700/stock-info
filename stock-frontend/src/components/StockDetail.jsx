import { useParams, Link } from 'react-router-dom'; // 1. Import Link
import CandlestickChart from './CandlestickChart';

function StockDetail() {
  const { symbol } = useParams();

  return (
    <div>
      {/* 2. Add the Link component */}
      <Link to="/" className="back-button">
        &larr; Back to Heatmap
      </Link>
      
      <h1>Details for {symbol}</h1>
      <CandlestickChart symbol={symbol} />
    </div>
  );
}

export default StockDetail;