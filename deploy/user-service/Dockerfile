    # 使用 JDK 运行时镜像
    FROM eclipse-temurin:8-jdk

    # 设置工作目录
    WORKDIR /app

    # 复制构建好的 jar 文件
    COPY user-service.jar app.jar

    # 开放服务端口（根据你的应用端口设置）
    EXPOSE 8081

    # 启动应用
    ENTRYPOINT ["java", "-jar", "app.jar"]