<<<<<<< HEAD
FROM gradle:8.2.0-jdk17
WORKDIR /app
COPY . /app

RUN gradle --no-daemon --warning-mode all --console=plain getDeps

EXPOSE 8080
CMD ["sh", "start.sh"]


=======
# Cache stage
FROM gradle:latest AS cache
ENV APP_HOME=/usr/app/
RUN mkdir $APP_HOME
ENV GRADLE_USER_HOME /usr/gradle/cache

RUN mkdir -p /usr/gradle/cache
COPY build.gradle $APP_HOME
WORKDIR $APP_HOME
# COPY . .
RUN ./gradlew build || return 0
# ^ This build is expeected to fail (cause no java files)
# But it'll also pull dependencies which is what we want from the cache stage


# Build stage
FROM gradle:latest AS build
ARG WORKER_COUNT
ENV WORKER_COUNT=${WORKER_COUNT}
ENV APP_HOME=/usr/app/
COPY --from=cache /usr/gradle/cache /home/gradle/.gradle
WORKDIR $APP_HOME
COPY . .
RUN gradle bootJar


# Run stage
FROM openjdk:17-jdk-alpine
ENV APP_HOME=/usr/app
ENV ARTIFACT_NAME=talkychefserver-0.0.1-SNAPSHOT.jar
WORKDIR $APP_HOME
COPY --from=build $APP_HOME/build/libs/$ARTIFACT_NAME talkychef.jar
ENTRYPOINT ["java", "-jar", "talkychef.jar"]
>>>>>>> 924f959d32bb602377f6b7841804499c9d8cd5e5
