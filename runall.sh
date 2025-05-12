#!/bin/bash

./gradlew :fee-lib:build :fee-app:build  # Build backend JARs
cd fee-ui && npm install && cd ..        # Install frontend deps (once)

docker-compose build
docker-compose up
