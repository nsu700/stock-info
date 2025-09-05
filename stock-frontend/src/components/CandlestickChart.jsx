import { useState, useEffect, useRef } from 'react';
import * as d3 from 'd3';

function CandlestickChart({ symbol }) {
    const [ohlcData, setOhlcData] = useState([]);
    const svgRef = useRef();

    // 1. Fetch the historical data for the given symbol
    useEffect(() => {
        async function fetchData() {
            try {
                const response = await fetch(`http://localhost:8080/api/stocks/${symbol}/price-history`);
                const data = await response.json();
                // Ensure this parsing step is correct
                const parsedData = data.map(d => ({
                    ...d,
                    date: new Date(d.timestamp) // This should convert the timestamp string
                }));
                // For debugging, check the first date
                if (parsedData.length > 0) {
                console.log("First parsed date:", parsedData[0].date);
                }
                setOhlcData(parsedData);
            } catch (error) {
                console.error("Failed to fetch OHLC data:", error);
            }
        }
        fetchData();
    }, [symbol]);

    // 2. Draw the chart whenever the data changes
    useEffect(() => {
        if (ohlcData.length === 0) return;

        const margin = { top: 20, right: 30, bottom: 30, left: 40 };
        const width = 800 - margin.left - margin.right;
        const height = 400 - margin.top - margin.bottom;

        const svg = d3.select(svgRef.current)
            .attr("width", width + margin.left + margin.right)
            .attr("height", height + margin.top + margin.bottom)
            .html("") // Clear previous chart
            .append("g")
            .attr("transform", `translate(${margin.left},${margin.top})`);

        // 3. Define the scales (mapping data to pixels)
        const xScale = d3.scaleBand()
            .domain(ohlcData.map(d => d.date))
            .range([0, width])
            .padding(0.2);

        const yScale = d3.scaleLinear()
            .domain([d3.min(ohlcData, d => d.lowPrice), d3.max(ohlcData, d => d.highPrice)])
            .range([height, 0]);

        // 4. Draw the axes
        svg.append("g")
            .attr("transform", `translate(0,${height})`)
            .call(d3.axisBottom(xScale).tickFormat(d3.timeFormat("%b %d")));

        svg.append("g")
            .call(d3.axisLeft(yScale));

        // 5. Draw the wicks (high-low lines)
        svg.selectAll(".wick")
            .data(ohlcData)
            .enter().append("line")
            .attr("class", "wick")
            .attr("x1", d => xScale(d.date) + xScale.bandwidth() / 2)
            .attr("x2", d => xScale(d.date) + xScale.bandwidth() / 2)
            .attr("y1", d => yScale(d.highPrice))
            .attr("y2", d => yScale(d.lowPrice))
            .attr("stroke", "gray");

        // 6. Draw the candle bodies (open-close rectangles)
        svg.selectAll(".candle")
            .data(ohlcData)
            .enter().append("rect")
            .attr("class", "candle")
            .attr("x", d => xScale(d.date))
            .attr("y", d => yScale(Math.max(d.openPrice, d.closePrice)))
            .attr("width", xScale.bandwidth())
            .attr("height", d => Math.abs(yScale(d.openPrice) - yScale(d.closePrice)))
            .attr("fill", d => d.closePrice >= d.openPrice ? "#5cb85c" : "#d9534f");

    }, [ohlcData]);

    return (
        <div>
            <h3>Price History</h3>
            <svg ref={svgRef}></svg>
        </div>
    );
}

export default CandlestickChart;