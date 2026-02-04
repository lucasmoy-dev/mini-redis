@echo off
set "CP=mini-redis-core\target\classes;libs\*"
mkdir mini-redis-server\target\classes 2>nul
javac -source 1.8 -target 1.8 -d mini-redis-server\target\classes -cp "%CP%" @server_sources.txt
