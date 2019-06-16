FROM openjdk:8-jdk-alpine
LABEL maintainer="shiweideyouxiang@sina.cn"

WORKDIR /app
ADD @project.artifactId@-@project.version@-app.tar.gz /app
EXPOSE 8761
ENTRYPOINT ["/app/@project.artifactId@-@project.version@-app/bin/run"]