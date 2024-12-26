# SkyWalking Agent 配置
export SW_AGENT_NAME=demo-example # 配置 Agent 名字。一般来说，我们直接使用 Spring Boot 项目的 `spring.application.name` 。
export SW_AGENT_COLLECTOR_BACKEND_SERVICES=172.16.118.101:11800 # 配置 Collector 地址。
export JAVA_AGENT=-javaagent:/Users/kenny/skywalking/skywalking-agent/skywalking-agent.jar # SkyWalking Agent jar 地址。

# Jar 启动
java -jar $JAVA_AGENT -jar /Users/kenny/IdeaProjects/example/target/example-0.0.1-SNAPSHOT.jar
