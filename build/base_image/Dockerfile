FROM docker:18.09-git
LABEL maintainer="shiweideyouxiang@sina.cn"

ENV HTTP_PROXY http://192.168.0.201:1080
ENV HTTPS_PROXY http://192.168.0.201:1080
RUN apk update
# 安裝kubectl-v1.4.0
RUN apk add --update ca-certificates \
 && apk add --update -t deps curl \
 && apk add --update gettext \
 && curl -L https://storage.googleapis.com/kubernetes-release/release/v1.4.0/bin/linux/amd64/kubectl -o /usr/local/bin/kubectl \
 && chmod +x /usr/local/bin/kubectl

# 安裝openjdk-8
RUN apk add openjdk8-jre

# 安裝maven-3.2.5
RUN mkdir -p /usr/share/maven \
 && curl -fsSL http://apache.osuosl.org/maven/maven-3/3.2.5/binaries/apache-maven-3.2.5-bin.tar.gz \
    | tar -xzC /usr/share/maven --strip-components=1 \
 && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn
ENV MAVEN_HOME /usr/share/maven



# 清理环境
RUN apk del --purge deps \
 && rm /var/cache/apk/*