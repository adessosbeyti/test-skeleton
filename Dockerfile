FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY .mvn ./.mvn
COPY mvnw ./
COPY pom.xml ./
RUN apt-get update && apt-get install dos2unix
RUN dos2unix mvnw
RUN ["chmod", "+x", "mvnw"]
RUN /bin/sh mvnw dependency:resolve
COPY src ./src
CMD ["./mvnw", "spring-boot:run"]
