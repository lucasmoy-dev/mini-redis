@echo off
set "CP=mini-redis-server\target\classes;mini-redis-core\target\classes;libs\*"
echo Starting Mini Redis Server...
start /b java -cp "%CP%" com.miniredis.MiniRedisApplication > server_log.txt 2>&1

echo Waiting for server to start (10s)...
timeout /t 10 /nobreak > nul

echo.
echo Running Acceptance Scenario 1: SET and GET
curl -s "http://localhost:8080/?cmd=SET+user:1+Lucas"
echo.
curl -s "http://localhost:8080/?cmd=GET+user:1"
echo.

echo.
echo Running Acceptance Scenario 2: Increment
curl -s "http://localhost:8080/?cmd=SET+counter+10"
echo.
curl -s "http://localhost:8080/?cmd=INCR+counter"
echo.

echo.
echo Running Acceptance Scenario 3: Sorted Sets
curl -s "http://localhost:8080/?cmd=ZADD+levels+100+legend"
echo.
curl -s "http://localhost:8080/?cmd=ZADD+levels+50+rookie"
echo.
curl -s "http://localhost:8080/?cmd=ZRANGE+levels+0+-1"
echo.

echo.
echo Shutting down server...
taskkill /f /im java.exe > nul 2>&1
echo Done.
