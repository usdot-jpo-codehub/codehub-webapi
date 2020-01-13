FROM maven:3.5.4-jdk-8-alpine as builder

WORKDIR /home

COPY . .

RUN mvn clean package

FROM openjdk:8u171-jre-alpine

RUN apk add curl

COPY --from=builder /home/src/main/resources/application.properties application.properties
COPY --from=builder /home/target/codehub-webapi-1.1.0.jar codehub-webapi-1.1.0.jar

ENTRYPOINT ["sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -jar /codehub-webapi-1.1.0.jar" ]
