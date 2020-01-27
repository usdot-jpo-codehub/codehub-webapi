
#Build Docker images
docker build -t codehub-webapi:latest .

#Run Docker container
docker run -p 3000:3000 --rm -e "server.port=3000" -e "codehub.webapi.es.host=localhost" -e "codehub.webapi.es.port=9200" -e "codehub.webapi.es.scheme=http" -e "JAVA_OPTS=-Xmx512M -Xms512M" -t -i codehub-webapi:latest
