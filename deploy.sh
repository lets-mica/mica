#!/bin/sh

## 1. java version
export JAVA_HOME=`/usr/libexec/java_home -v 17`
java -version
printf "\n"

## 2. maven version
mvn -version
printf "\n"

## 3. 环境
if [ -z $1 ]; then
    profile="release"
else
    profile="$1"
fi

## 4. deploy 发布正式版，
mvn clean package deploy -P$profile -DskipTests

