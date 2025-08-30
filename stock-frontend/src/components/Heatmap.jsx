import { useState, useEffect, useRef } from 'react';
import * as d3 from 'd3'; // Import the d3 library

function Heatmap() {
    const [heatmapData, setHeatmapData] = useState([]);
    const svgRef = useRef(); // Create a ref to hold the SVG element

    // Fetch data when the component first loads
    useEffect(() => {
        async function fetchData() {
            try {
                const data = await d3.json('http://localhost:8080/api/stocks/heatmap-data');
                setHeatmapData(data);
            } catch (error) {
                console.error("Failed to fetch heatmap data:", error);
            }
        }
        fetchData();
    }, []);

    // Draw the heatmap whenever heatmapData changes
    useEffect(() => {
        if (heatmapData.length === 0) return; // Don't draw until we have data

        const width = 1200;
        const height = 800;

        // --- D3 TREEMAP LOGIC ---
        
        // Select the SVG element using the ref
        const svg = d3.select(svgRef.current)
            .attr("width", width)
            .attr("height", height);

        // Clear previous rendering
        svg.selectAll("*").remove();

        // Prepare the data for the treemap layout
        const root = d3.hierarchy({ children: heatmapData })
            .sum(d => d.marketCapitalization)
            .sort((a, b) => b.value - a.value);

        // Create and run the treemap layout
        const treemapLayout = d3.treemap().size([width, height]).padding(1);
        treemapLayout(root);

        // Define a color scale
        const colorScale = d3.scaleLinear()
            .domain([-3, 0, 3])
            .range(["#d9534f", "#f0f0f0", "#5cb85c"]); // Red, Gray, Green

        // Create the cells (rectangles)
        const cell = svg.selectAll("g")
            .data(root.leaves())
            .enter().append("g")
            .attr("transform", d => `translate(${d.x0},${d.y0})`);

        cell.append("rect")
            .attr("width", d => d.x1 - d.x0)
            .attr("height", d => d.y1 - d.y0)
            .style("fill", d => colorScale(d.data.priceChangePercentage))
            .style("stroke", "#fff");

        // Add text labels
        cell.append("text")
            // Center the text horizontally in the middle of the rectangle
            .attr("x", d => (d.x1 - d.x0) / 2)
            .attr("y", d => (d.y1 - d.y0) / 2) // Center the text vertically
            .attr("text-anchor", "middle") // Horizontally align the text to its middle
            .attr("dominant-baseline", "middle") // Vertically align the text to its middle
            .attr("fill", "white")
            .attr("font-size", "15px")
            // Conditionally add an up or down arrow based on price change
            .text(d => 
                `${d.data.symbol} ${d.data.priceChangePercentage >= 0 ? '▲' : '▼'}`
            );

    }, [heatmapData]); // This useEffect runs whenever heatmapData changes

    return (
        <div className="heatmap-container">
            {/* The ref is attached to the SVG element */}
            <svg ref={svgRef}></svg>
        </div>
    );
}

export default Heatmap;