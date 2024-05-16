FROM openjdk:22

WORKDIR /app
COPY . .
RUN mvn clean install

CMD mvn spring-boot:run