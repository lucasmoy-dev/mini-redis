@echo off
set "CP=mini-redis-server\target\classes;mini-redis-core\target\classes;libs\*"
echo Starting Mini Redis Server for 10 seconds to verify startup...
start /b java -cp "%CP%" com.miniredis.MiniRedisApplication
timeout /t 10
taskkill /f /im java.exe
