FROM openjdk:8-jdk
LABEL maintainer="shiweideyouxiang@sina.cn"

#工具安裝
RUN apt-get update
RUN apt-get install sshpass

# Install Docker client
ARG DOCKER_VERSION=18.06.1-ce
RUN curl -fsSL https://download.docker.com/linux/static/stable/`uname -m`/docker-$DOCKER_VERSION.tgz | tar --strip-components=1 -xz -C /usr/local/bin docker/docker

# 安裝kubectl-v1.4.0
ADD kubectl /usr/local/bin/
RUN chmod +x /usr/local/bin/kubectl
RUN mkdir -p /home/jenkins/.kube

# 安裝maven-3.2.5
RUN mkdir -p /usr/share/maven
ADD https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.2.5/apache-maven-3.2.5-bin.tar.gz /usr/share/maven
RUN tar -zxvf /usr/share/maven/apache-maven-3.2.5-bin.tar.gz -C /usr/share/maven
ENV MAVEN_HOME /usr/share/maven/apache-maven-3.2.5
ENV PATH ${PATH}:${MAVEN_HOME}/bin/
RUN mkdir -p /home/jenkins/.m2