FROM openjdk:17
RUN mkdir -p /app/SP
WORKDIR /app/SP
COPY ./target/image-generation-service-0.0.1-SNAPSHOT.jar image-generation-service.jar
CMD ["java", "-jar", "image-generation-service.jar"]
