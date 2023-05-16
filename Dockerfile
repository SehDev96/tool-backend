FROM maven:3.8.4-openjdk-17 AS MAVEN_BUILD
WORKDIR /build/
RUN echo ls
COPY pom.xml /build/
COPY mvnw /build/
COPY .mvn /build/.mvn
COPY src /build/src/
RUN mvn clean install -Dmaven.test.skip=true

FROM openjdk:17-oracle
WORKDIR /app
COPY --from=MAVEN_BUILD /build/target/tool-backend-0.0.1-SNAPSHOT.jar /app/application.jar
EXPOSE 5000
ENTRYPOINT ["java","-jar","/app/application.jar"]