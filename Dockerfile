FROM gradle:6.8.3 AS build
COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src
RUN gradle build --no-daemon
EXPOSE 8080
RUN mkdir /app

ENTRYPOINT ["gradle", "bootRun"]
