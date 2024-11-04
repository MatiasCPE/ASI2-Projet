FROM openjdk:17
RUN mkdir -p /app/SP
WORKDIR /app/SP
COPY ./target/asi2-backendmarket-monolithic-student-0.0.1-SNAPSHOT.jar backend.jar
CMD ["java", "-jar", "backend.jar"]
