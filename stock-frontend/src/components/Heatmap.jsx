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
                console.log("Heatmap data updated at: " + new Date().toLocaleTimeString());
            } catch (error) {
                console.error("Failed to fetch heatmap data:", error);
            }
        }
        fetchData();

        // 2. Set an interval to fetch data every 15 seconds
        const intervalId = setInterval(fetchData, 15000);

        // 3. Clean up the interval when the component is unmounted
        return () => clearInterval(intervalId);
    }, []);


    useEffect(() => {
      if (heatmapData.length === 0) return;

      const width = 1200;
      const height = 800;

      const svg = d3.select(svgRef.current)
          // Set the viewBox to match your coordinate system
          .attr("viewBox", `0 0 ${width} ${height}`)
          // Optional: Ensures the aspect ratio is maintained
          .attr("preserveAspectRatio", "xMidYMid meet");

      svg.selectAll("*").remove();

      // 1. --- GROUP DATA BY INDUSTRY ONLY ---
      // Use d3.group once to group by the 'industry' field.
      const groupedData = d3.group(heatmapData, d => d.industry);

      // 2. --- CREATE THE HIERARCHY ---
      const root = d3.hierarchy(groupedData)
          .sum(d => d.marketCapitalization)
          .sort((a, b) => b.value - a.value);

      // 3. --- CREATE THE TREEMAP LAYOUT ---
      const treemapLayout = d3.treemap()
          .size([width, height])
          .paddingTop(28)       // Space for the industry title
          .paddingInner(3)        // Space between individual stocks
          .round(true);

      treemapLayout(root);

      // 4. --- DEFINE THE CUSTOM COLOR SCALE ---
      const colorScale = d3.scaleLinear()
          // Define the input values (price change percentages)
          .domain([-2, -0.1, 0.1, 2])
          // Define the corresponding output colors
          .range(["#d9534f", "#cccccc", "#cccccc", "#5cb85c"]); // Red -> Gray -> Green

      // 5. --- DRAW THE NODES (GROUPS AND LEAVES) ---
      const node = svg.selectAll("g")
          .data(root.descendants())
          .join("g")
          .attr("transform", d => `translate(${d.x0},${d.y0})`);

      // Add a border rectangle for each industry group
      node.filter(d => d.depth === 1) // Filter for industry groups
          .append("rect")
          .attr("width", d => d.x1 - d.x0)
          .attr("height", d => d.y1 - d.y0)
          .attr("fill", d => d.data.industry)
          .attr("stroke", "#555")
          .attr("stroke-width", 2);

      // Draw the colored rectangles for the individual stocks
      node.filter(d => d.depth === 2) // Filter for stocks (leaf nodes)
          .append("rect")
          .attr("width", d => d.x1 - d.x0)
          .attr("height", d => d.y1 - d.y0)
          .attr("fill", d => colorScale(d.data.priceChangePercentage));

      // Add labels for the industry groups
      node.filter(d => d.depth === 1) // Filter for industry groups
          .append("text")
          .attr("x", 4)
          .attr("y", 18)
          .text(d => d.data[0]) // The industry name
          .attr("font-size", "16px")
          .attr("font-weight", "bold")
          .attr("fill", "#fff");

      // Add labels for the stocks (only if the rectangle is large enough)
      const leafNodes = node.filter(d => d.depth === 2);

      const labels = leafNodes.filter(d => (d.x1 - d.x0) > 50 && (d.y1 - d.y0) > 40); // Filter for larger cells

      // Create a <text> element centered in the rectangle
      const text = labels.append("text")
          .attr("x", d => (d.x1 - d.x0) / 2) // Horizontal center
          .attr("y", d => (d.y1 - d.y0) / 2) // Vertical center
          .attr("text-anchor", "middle") // Horizontally align
          .attr("dominant-baseline", "middle") // Vertically align
          .attr("fill", "white")
          .style("pointer-events", "none"); // Makes the text non-interactive

      // Add the first line (the symbol) using <tspan>
      text.append("tspan")
          .attr("font-size", "16px")
          .attr("font-weight", "bold")
          .text(d => d.data.symbol);

      // Add the second line (the price change) using <tspan>
      text.append("tspan")
          .attr("x", d => (d.x1 - d.x0) / 2) // Re-center horizontally
          .attr("dy", "1.2em") // 'dy' moves it down relative to the line above
          .attr("font-size", "12px")
          .text(d => `${d.data.priceChangePercentage.toFixed(2)}%`);

      // ✅ ADD THIS LINE TO DEBUG
      console.log("D3 Hierarchy Nodes:", root.descendants());
    }, [heatmapData]);

    return (
        <div className="heatmap-container">
            {/* The ref is attached to the SVG element */}
            <svg ref={svgRef}></svg>
        </div>
    );
}

export default Heatmap;