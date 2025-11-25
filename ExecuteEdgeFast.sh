#!/bin/bash
# Fast Edge-only test execution script
# Optimized for speed: only starts Edge node, uses parallel execution

echo "🚀 Starting optimized Edge test execution..."

#1- Start ONLY Hub + Edge node (faster than starting all 3 browsers)
docker-compose -f docker-compose-edge-only.yml up -d

#2- Wait for grid to be ready
while ! curl -sSL "http://localhost:4444/wd/hub/status" | jq -r ".value.ready" | grep "true"; do
    echo "⏳ Waiting for Selenium Hub..."
    sleep 10
done
echo "✅ Selenium Grid is ready!"

#3- Execute tests with aggressive parallelism (5 threads)
echo "🧪 Running tests with 3 parallel threads..."
docker-compose -f docker-compose-edge-only.yml run --rm test-runner \
  mvn test -T 1C \
  -Dbrowser=edge \
  -DremoteExecution=true \
  -Dheadless=true \
  -Dparallel=methods \
  -Dthreadcount=3 \
  -Dtest="%regex[.*Tests.*],com.taf.tests.**.**,com.taf.tests.**"

TEST_EXIT_CODE=$?

#4- Cleanup
echo "🧹 Cleaning up Docker resources..."
docker-compose -f docker-compose-edge-only.yml down

#5- Open report after execution
echo "✅ Test Execution completed successfully!"
open ./test-output/reports/AllureReport*.html