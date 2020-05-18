# Dockerfile for @project.artifactId@
# 1. Copy @project.artifactId@-@project.version@.zip to current directory
# 2. Build with: docker build -t @project.artifactId@ .
# 3. Run with: docker run -p 8090:8090 -d -v @logging.path@:@logging.path@ --name @project.artifactId@ @project.artifactId@
FROM openjdk:8-jdk-alpine

MAINTAINER roc.wong <roc.fly@qq.com>

# Setting the LD_LIBRARY_PATH environment variable so the systems dynamic linker can find the newly installed libraries.
ENV LD_LIBRARY_PATH /@project.artifactId@

# Updating PATH environment program so that utility binaries installed by the various libraries will be found.
ENV PATH /@project.artifactId@:$PATH

# 安装tzdata后，会在/usr/share/zoneinfo目录下生成各时区配置信息，Alpine目录并无timezone及locatime配置
RUN echo "https://mirrors.aliyun.com/alpine/v3.9/main/" > /etc/apk/repositories \
    && echo "https://mirrors.aliyun.com/alpine/v3.9/community/" >> /etc/apk/repositories \
    && apk update upgrade \
    && apk add --no-cache tini procps unzip curl bash tzdata busybox-extras \
    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone

ADD ./@project.artifactId@-@project.version@.zip /@project.artifactId@/@project.artifactId@-@project.version@.zip

# copy arthas, required busybox-extras
COPY --from=hengyunabc/arthas:latest /opt/arthas /opt/arthas

RUN unzip /@project.artifactId@/@project.artifactId@-@project.version@.zip -d /@project.artifactId@ \
    && rm -rf /@project.artifactId@/@project.artifactId@-@project.version@.zip

EXPOSE @server.port@

HEALTHCHECK CMD curl --fail http://localhost:@server.port@@server.servlet.context-path@actuator/health || exit 1

# Tini is now available at /sbin/tini
ENTRYPOINT ["/sbin/tini", "--"]

CMD ["/@project.artifactId@/scripts/docker-entrypoint.sh"]