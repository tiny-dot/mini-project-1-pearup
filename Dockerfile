# need .mvn, mvmw, pom.xml
FROM eclipse-temurin:22-jdk AS builder

LABEL maintainer="minidott"

WORKDIR /src


COPY mvnw . 
COPY mvnw.cmd .

COPY pom.xml .
COPY .mvn .mvn
COPY src src

RUN chmod a+x mvnw && ./mvnw package -Dmaven.test.skip=true 

FROM eclipse-temurin:22-jdk 


WORKDIR /app

COPY --from=builder /src/target/mini_project_1-0.0.1-SNAPSHOT.jar pearup.jar

ENV PORT=8080

EXPOSE ${PORT}


ENTRYPOINT java -jar pearup.jar
