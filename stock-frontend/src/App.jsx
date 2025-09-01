import { Routes, Route } from 'react-router-dom';
import Heatmap from './components/Heatmap';
import StockDetail from './components/StockDetail';
import './App.css';

function App() {
  return (
    <div className="App">
      <Routes>
        {/* Route for the main heatmap page */}
        <Route path="/" element={<Heatmap />} />
        
        {/* Route for the stock detail page with a dynamic symbol */}
        <Route path="/stock/:symbol" element={<StockDetail />} />
      </Routes>
    </div>
  );
}

export default App;