rm api-gateway.jar

cp /Users/kenny/IdeaProjects/example/api-gateway/target/api-gateway-*.jar api-gateway.jar

docker build -t api-gateway:0.1 .