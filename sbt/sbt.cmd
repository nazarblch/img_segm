@echo off


set L_HOME=%~dp0..

java -Xmx1200M -XX:MaxPermSize=200m -XX:ReservedCodeCacheSize=256m -jar %L_HOME%\sbt\sbt-launch.jar "%*"
