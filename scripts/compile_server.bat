@echo off
echo Compiling Core Module...
dir /s /b mini-redis-core\src\main\java\*.java > core_sources.txt
mkdir mini-redis-core\target\classes 2>nul
javac -source 1.8 -target 1.8 -d mini-redis-core\target\classes @core_sources.txt

echo Compiling Server Module...
dir /s /b mini-redis-server\src\main\java\*.java > server_sources.txt
mkdir mini-redis-server\target\classes 2>nul
set "CP=mini-redis-core\target\classes;libs\*"
javac -source 1.8 -target 1.8 -d mini-redis-server\target\classes -cp "%CP%" @server_sources.txt

echo Copying Static Resources...
mkdir mini-redis-server\target\classes\static 2>nul
copy /y mini-redis-server\src\main\resources\static\* mini-redis-server\target\classes\static\ > nul

del core_sources.txt server_sources.txt
