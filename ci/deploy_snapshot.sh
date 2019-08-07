#!/bin/bash

set -e -u

function gradle_publish() {
    ./gradlew clean publish
}

if [ "$TRAVIS_REPO_SLUG" == "lets-mica/mica" ] && \
   [ "$TRAVIS_JDK_VERSION" == "openjdk8" ] && \
   [ "$TRAVIS_PULL_REQUEST" == "false" ] && \
   [ "$TRAVIS_BRANCH" == "master" ]; then
  echo "Publishing To Maven snapshot..."

  gradle_publish

  echo "Maven snapshot published."
fi
