#!/bin/bash
#1-Remove the old "test-output/allure-results" results folder to insure clean report generation
rm -rf test-output/allure-results

#2- Create the needed docker configuration from the (docker-compose.yml) including network, hub & nodes
docker-compose up -d
# Note: Ensure that the docker-compose.yml file is correctly configured to set up the Selenium Grid with the required nodes.

#3- Sleep for 5 seconds to ensure that the nodes got registered to the hub
Sleep 15

#4- Execute the tests
mvn clean test
#mvn clean test -Dbrowser=chrome
#mvn clean test -Dbrowser=firefox
#mvn clean test -Dbrowser=edge

#5-Copy the "history" folder of the latest generated "allure-report"/report folder to the results folder
cp -r test-output/allure-report/history test-output/allure-results/

#6-Generate a clean new report from the results folder including the "history" copied in the previous step
allure generate --clean ./test-output/allure-results -o ./test-output/allure-report

#7- Remove all the docker resources created for the test
docker-compose down

#8-Open the newly generated report
allure open ./test-output/allure-report