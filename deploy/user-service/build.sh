cd rm user-service.jar

cp /Users/kenny/IdeaProjects/example/user-service/target/user-service-*.jar user-service.jar

docker build -t user-service:0.1 .
