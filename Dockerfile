FROM openjdk:8u212-jre-alpine
MAINTAINER SCCBA.IUS
RUN mkdir -p /app/logs
WORKDIR /
WORKDIR /app
COPY ./VX-API-Gateway ./VX-API-Gateway
RUN mv VX-API-Gateway gateway
ENTRYPOINT /app/gateway/bin/start.sh