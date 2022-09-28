FROM tomcat:9.0.67
COPY target/EuCaptcha.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]