#!/bin/sh

## 1. java version
java -version
printf "\n"

## 2. gradle version
./gradlew -version
printf "\n"

## 3. gradle clean build
./gradlew clean build

## 4. deploy 发布正式版，关闭并行
./gradlew publish --no-parallel
