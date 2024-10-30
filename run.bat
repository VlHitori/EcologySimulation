@echo off

mvn clean install

java -cp "target\*.jar" com.kabachok.App

pause
