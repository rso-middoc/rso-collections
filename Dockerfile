FROM openjdk:11.0.4-jre-slim

RUN mkdir /app

WORKDIR /app

ADD ./api/target/collections-api-1.0.0-SNAPSHOT.jar /app

EXPOSE 8081

CMD ["java", "-jar", "collections-api-1.0.0-SNAPSHOT.jar"]