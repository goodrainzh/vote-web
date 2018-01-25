FROM goodrainapps/maven:jdk7-alpine

COPY demo.vote.web.jar /vote-web.jar

ENTRYPOINT exec java -jar /vote-web.jar