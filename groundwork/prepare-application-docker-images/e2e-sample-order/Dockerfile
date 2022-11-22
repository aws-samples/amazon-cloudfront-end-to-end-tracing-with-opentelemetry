FROM        gradle:7.4-jdk11-alpine AS build

WORKDIR     /home/gradle/src

# download dependencies
COPY        --chown=gradle:gradle build.gradle settings.gradle /home/gradle/src/
RUN         gradle clean build --no-daemon > /dev/null 2>&1 || true

# build source code
COPY        --chown=gradle:gradle . /home/gradle/src
RUN         gradle clean build -x test --no-daemon

FROM        openjdk:11-jre-slim

ARG         JVM_OPS
ENV         JVM_OPS=${JVM_OPS}

COPY        --from=build /home/gradle/src/interface/build/libs/*.jar app.jar

ADD         https://github.com/aws-observability/aws-otel-java-instrumentation/releases/latest/download/aws-opentelemetry-agent.jar /opt/aws-opentelemetry-agent.jar
ENV         JAVA_TOOL_OPTIONS=-javaagent:/opt/aws-opentelemetry-agent.jar

CMD         ["sh","-c","java $JVM_OPS -jar app.jar"]
