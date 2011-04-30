@echo off

set bin="./bin"
set src="./src"

javac -Xlint -d %bin% -classpath %src% src/Server.java

pause