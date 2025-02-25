# 基础镜像
FROM openjdk:8-jre-slim

# 作者
MAINTAINER mbtt

# 时区
ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 添加编译后的 JAR 文件（注意文件名对应）
ARG JAR_PATH=target/dymall-mbtt-service.jar
COPY ${JAR_PATH} app.jar

# 创建日志目录（与 logging.file.path 对应）
RUN mkdir -p /app/logs
VOLUME /app/logs

# 入口
ENTRYPOINT ["java", "-jar", "/app.jar"]