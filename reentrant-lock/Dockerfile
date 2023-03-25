# using multistage docker build
# ref: https://docs.docker.com/build/building/multi-stage/

#### Do gradle build inside a container ####
FROM gradle:jdk11 AS BUILD_IMAGE
ENV APP_HOME=/usr/gradle/
WORKDIR $APP_HOME
COPY build.gradle settings.gradle $APP_HOME
COPY gradle $APP_HOME/gradle

# --chown=<user>:<group>, ensures that the new files and directories that are
# created have the specified user and group ownership applied.
COPY --chown=gradle:gradle . /home/gradle/src
USER root
RUN chown -R gradle /home/gradle/src
COPY . .
RUN gradle clean build

#### Use the following lines only if you need to debug the gradle build running inside docker ####
# CMD sleep infinity
# To debug, docker build . --tag gradle-build/barrier:1 and then run docker run gradle-build/barrier:1
# and then ssh into the gradle-build/barrier:1 container
#### end ####

FROM openjdk:11
ENV ARTIFACT_NAME=reentrant-lock.jar
ENV APP_HOME=/home/demo/build/libs/
WORKDIR $APP_HOME
COPY --from=BUILD_IMAGE /usr/gradle/build/libs/$ARTIFACT_NAME .

#### debugging ####
#CMD sleep infinity
CMD /bin/bash

