#!/bin/bash

echo "🚀 Starting optimized Parallel test execution..."


#1- Create the needed docker configuration from the (docker-compose.yml) including network, hub & nodes
docker-compose up -d
# Note: Ensure that the docker-compose.yml file is correctly configured to set up the Selenium Grid with the required nodes.

#2- Wait with healthcheck instead of sleep
while ! curl -sSL "http://localhost:4444/wd/hub/status" | jq -r ".value.ready" | grep "true"; do
    echo "Waiting for Selenium Hub..."
    sleep 15
done
echo "✅ Selenium Grid is ready!"

#3- Execute the tests with parallel execution for faster performance
# Using 'test' instead of 'clean test' to skip cleaning (faster), and -T 1C for parallel Maven builds
echo "🧪 Running tests with 3 parallel threads..."

#Uncomment the following lines to enable Edge tests in parallel
#docker-compose run --rm test-runner mvn test -T 1C -Dbrowser=edge \
#-DremoteExecution=true -Dheadless=true \
#-Dparallel=methods -Dthreadcount=3 \
#-Dtest="%regex[.*Tests.*],com.taf.tests.**.**,com.taf.tests.**" &
#EDGE_PID=$!

# Uncomment the following lines to enable Chrome tests in parallel as well
docker-compose run --rm test-runner mvn test -T 1C -Dbrowser=chrome \
-DremoteExecution=true -Dheadless=true \
-Dparallel=methods -Dthreadcount=2 \
-Dtest="%regex[.*Tests.*],com.taf.tests.**.**,com.taf.tests.**" &
CHROME_PID=$!
#mvn clean test -Dbrowser=chrome
#mvn clean test -Dbrowser=firefox
#mvn clean test -Dbrowser=edge

# Uncomment the following lines to enable Edge tests in parallel
# Wait for both processes and capture exit codes
#wait $EDGE_PID
#EDGE_EXIT=$?
#echo "✅ Edge tests exit code: $EDGE_EXIT"

# Uncomment the following lines to enable Chrome tests in parallel as well
wait $CHROME_PID
CHROME_EXIT=$?
echo "✅ Chrome tests exit code: $CHROME_EXIT"

#4- Remove all the docker resources created for the test
echo "🧹 Cleaning up Docker resources..."
docker-compose down
echo "✅ Test Execution completed successfully!"

#5-Open the newly generated report
open ./test-output/reports/AllureReport*.html

echo "📊 Allure Report opened successfully!"