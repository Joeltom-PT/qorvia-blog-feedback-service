FROM openjdk:22-jdk
WORKDIR /app
COPY target/*.jar app.jar

ARG SPRING_DATASOURCE_URL
ARG SPRING_DATASOURCE_USERNAME
ARG SPRING_DATASOURCE_PASSWORD
ARG SPRING_RABBITMQ_HOST
ARG SPRING_RABBITMQ_PORT
ARG SPRING_RABBITMQ_USERNAME
ARG SPRING_RABBITMQ_PASSWORD
ARG SPRING_RABBITMQ_VIRTUAL_HOST

ENV SPRING_DATASOURCE_URL=${SPRING_DATASOURCE_URL}
ENV SPRING_DATASOURCE_USERNAME=${SPRING_DATASOURCE_USERNAME}
ENV SPRING_DATASOURCE_PASSWORD=${SPRING_DATASOURCE_PASSWORD}
ENV SPRING_RABBITMQ_HOST=${SPRING_RABBITMQ_HOST}
ENV SPRING_RABBITMQ_PORT=${SPRING_RABBITMQ_PORT}
ENV SPRING_RABBITMQ_USERNAME=${SPRING_RABBITMQ_USERNAME}
ENV SPRING_RABBITMQ_PASSWORD=${SPRING_RABBITMQ_PASSWORD}
ENV SPRING_RABBITMQ_VIRTUAL_HOST=${SPRING_RABBITMQ_VIRTUAL_HOST}

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]