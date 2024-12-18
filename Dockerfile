FROM adopt openjdk:17-jre-hotspot
COPY ./target/demo-1.0.war /usr/app/
WORKDIR /usr/app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "demo-1.0.war"]