#!/bin/bash
#1- Create the needed docker configuration from the (docker-compose.yml) including network, hub & nodes
docker-compose up -d
# Note: Ensure that the docker-compose.yml file is correctly configured to set up the Selenium Grid with the required nodes.

#2- Wait with healthcheck instead of sleep
while ! curl -sSL "http://localhost:4444/wd/hub/status" | jq -r ".value.ready" | grep "true"; do
    echo "Waiting for Selenium Hub..."
    sleep 15
done

#3- Execute the tests
docker-compose run --rm test-runner mvn clean test
#mvn clean test -Dbrowser=chrome
#mvn clean test -Dbrowser=firefox
#mvn clean test -Dbrowser=edge

#4- Remove all the docker resources created for the test
docker-compose down

#5-Open the newly generated report
open ./test-output/reports/AllureReport*.html