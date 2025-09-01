import { useParams, Link } from 'react-router-dom'; // 1. Import Link

function StockDetail() {
  const { symbol } = useParams();

  return (
    <div>
      {/* 2. Add the Link component */}
      <Link to="/" className="back-button">
        &larr; Back to Heatmap
      </Link>
      
      <h1>Details for {symbol}</h1>
      {/* You will fetch and display the stock history data here */}
    </div>
  );
}

export default StockDetail;