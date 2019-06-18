FROM openjdk:8-jdk
LABEL maintainer="shiweideyouxiang@sina.cn"

# Install Docker client
ARG DOCKER_VERSION=18.06.1-ce
RUN curl -fsSL https://download.docker.com/linux/static/stable/`uname -m`/docker-$DOCKER_VERSION.tgz | tar --strip-components=1 -xz -C /usr/local/bin docker/docker

# 安裝kubectl-v1.4.0
ADD kubectl /usr/local/bin/
RUN chmod +x /usr/local/bin/kubectl

# 安裝maven-3.2.5
RUN mkdir -p /usr/share/maven
ADD apache-maven-3.2.5-bin.tar.gz /usr/share/maven
ENV MAVEN_HOME /usr/share/maven/apache-maven-3.2.5
ENV PATH ${PATH}:${MAVEN_HOME}/bin/