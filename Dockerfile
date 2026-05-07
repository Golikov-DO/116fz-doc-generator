FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM tomcat:10.1-jdk21
WORKDIR /usr/local/tomcat/webapps/

COPY --from=build /app/target/116fz-doc-generator-1.0-SNAPSHOT.war ./ROOT.war

ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseContainerSupport"
EXPOSE 8080
