@echo off

set bin=./bin
set CLASSPATH=%bin%/;%CLASSPATH%
echo CLASSPATH=%CLASSPATH%
start rmiregistry

java -classpath %bin% Server ist411sp11_finalproject

pause