FROM java:openjdk-8
LABEL maintainer="shiweideyouxiang@sina.cn"

WORKDIR /app
ADD @project.artifactId@-@project.version@-app.tar.gz /app
EXPOSE 8080
ENTRYPOINT ["/app/@project.artifactId@-@project.version@-app/bin/run"]