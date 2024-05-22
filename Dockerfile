FROM openjdk:21-jdk-slim-buster
EXPOSE 8080
WORKDIR /app

RUN apt-get update && apt-get install -y \
    fontconfig \
    libfreetype6 \
    ttf-dejavu \
    && rm -rf /var/lib/apt/lists/*

COPY /target/coursework-0.0.1-SNAPSHOT.jar /app/try-with-me.jar
ENTRYPOINT ["java", "-jar", "try-with-me.jar"]