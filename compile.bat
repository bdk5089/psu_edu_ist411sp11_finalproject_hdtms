@echo off

set bin=./bin
set src=./src

javac -d %bin% -classpath %src% src/*.java

pause