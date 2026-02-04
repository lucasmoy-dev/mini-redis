@echo off
set "CP=mini-redis-server\target\classes;mini-redis-core\target\classes;libs\*"
echo Starting Mini Redis Server on http://localhost:8080 ...
java -cp "%CP%" com.miniredis.MiniRedisApplication
