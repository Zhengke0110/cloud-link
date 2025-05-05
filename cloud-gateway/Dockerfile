FROM adoptopenjdk/openjdk11:jre11u-nightly
COPY target/cloud-gateway.jar cloud-gateway.jar
ENTRYPOINT ["java","-jar","/cloud-gateway.jar"]