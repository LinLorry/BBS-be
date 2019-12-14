FROM java:8

COPY target/BBS-be-1.0-SNAPSHOT.jar /BBS-be.jar

EXPOSE 8080

CMD ["java", "-jar" , "/BBS-be.jar"]