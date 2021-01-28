FROM openjdk:8-alpine as builder

RUN mkdir /app
WORKDIR /app
COPY . .
RUN ./gradlew build

FROM openjdk:8-jre-alpine
COPY --from=builder /app/build/libs/superchat-backend-challenge.jar /usr/bin/superchat-backend-challenge.jar
EXPOSE 8080 8080
CMD java -jar /usr/bin/superchat-backend-challenge.jar
