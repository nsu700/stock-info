# ---- Stage 1: The Builder ----
# Use a full Python image to install dependencies
FROM python:3.11-slim as builder

# Set the working directory
WORKDIR /app

# Install system dependencies that might be needed by some packages
RUN apt-get update && apt-get install -y --no-install-recommends gcc

# Copy the requirements file
COPY requirements.txt .

# Create a virtual environment and install the packages
RUN python -m venv /opt/venv
ENV PATH="/opt/venv/bin:$PATH"
RUN pip install --no-cache-dir -r requirements.txt

# ---- Stage 2: The Final Image ----
# Use a slim image for a smaller final size
FROM python:3.11-slim

# Set the working directory
WORKDIR /app

# Copy the virtual environment from the builder stage
COPY --from=builder /opt/venv /opt/venv

# Copy your Python script into the image
COPY stock_init.py .

# Activate the virtual environment
ENV PATH="/opt/venv/bin:$PATH"

# Set the command to run your script
CMD ["python", "stock_init.py"]
