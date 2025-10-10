#!/bin/bash

# Grafana Dashboard Setup Script for Playwright Testing Framework
# This script sets up Grafana with Prometheus and InfluxDB for test metrics visualization

echo "🚀 Setting up Grafana Dashboard for Playwright Testing Framework..."

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed. Please install Docker first."
    exit 1
fi

# Check if Docker Compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose is not installed. Please install Docker Compose first."
    exit 1
fi

# Create necessary directories
echo "📁 Creating necessary directories..."
mkdir -p grafana-data
mkdir -p prometheus-data
mkdir -p influxdb-data

# Set proper permissions
echo "🔐 Setting permissions..."
chmod 755 grafana-data
chmod 755 prometheus-data
chmod 755 influxdb-data

# Build the metrics exporter
echo "🔨 Building metrics exporter..."
docker build -f Dockerfile.metrics -t playwright-metrics-exporter .

# Start the services
echo "🐳 Starting Grafana, Prometheus, and InfluxDB services..."
docker-compose -f docker-compose-grafana.yml up -d

# Wait for services to be ready
echo "⏳ Waiting for services to start..."
sleep 30

# Check if services are running
echo "🔍 Checking service status..."
docker-compose -f docker-compose-grafana.yml ps

# Import dashboard
echo "📊 Importing Grafana dashboard..."
curl -X POST \
  http://admin:admin@localhost:3000/api/dashboards/db \
  -H 'Content-Type: application/json' \
  -d @grafana-dashboard.json

echo ""
echo "✅ Setup complete!"
echo ""
echo "🌐 Access URLs:"
echo "   Grafana Dashboard: http://localhost:3000"
echo "   Prometheus: http://localhost:9090"
echo "   InfluxDB: http://localhost:8086"
echo ""
echo "🔑 Default credentials:"
echo "   Grafana: admin/admin"
echo "   InfluxDB: admin/admin"
echo ""
echo "📊 Dashboard features:"
echo "   - Test execution overview and timeline"
echo "   - Service health monitoring"
echo "   - Performance metrics and response times"
echo "   - Test coverage and error rates"
echo "   - Resource usage monitoring"
echo ""
echo "🛠️  To stop services: docker-compose -f docker-compose-grafana.yml down"
echo "🔄 To restart services: docker-compose -f docker-compose-grafana.yml restart"

