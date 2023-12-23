FROM openjdk:17-jdk

WORKDIR /app

COPY target/keepfit-0.0.1-SNAPSHOT.jar /app/keepfit.jar

EXPOSE 8080

CMD ["java", "-jar", "keepfit.jar"]