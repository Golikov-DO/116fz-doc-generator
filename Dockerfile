FROM tomcat:10.1-jdk21
COPY ./target/pmllpa-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
ENV JAVA_OPTS="-Xms256m -Xmx450m -XX:+UseContainerSupport"
EXPOSE 8080