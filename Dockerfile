FROM maven AS build

WORKDIR /app
ARG PORT
COPY . /app
RUN mvn clean install

FROM openjdk:22
COPY --from=build /app/target/*.jar app.jar
EXPOSE ${PORT}
CMD ["java", "-jar", "app.jar"]
