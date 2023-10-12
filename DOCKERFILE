FROM eclipse-temurin:11
RUN mkdir /opt/app
COPY Tuffel-Bot.jar /opt/app 
CMD ["java", "-jar", "/opt/app/Tuffel-Bot.jar"]
