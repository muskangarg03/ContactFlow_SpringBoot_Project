FROM openjdk:17
ADD target/*.jar springmysql.jar 
ENTRYPOINT ["java", "-jar", "springmysql.jar"]