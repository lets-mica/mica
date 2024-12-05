#!/bin/sh

## 1. java version
export JAVA_HOME=`/usr/libexec/java_home -v 17`
java -version
printf "\n"

## 2. maven version
mvn -version
printf "\n"

## 3. deploy 发布正式版，
mvn clean package deploy -Prelease -DskipTests

