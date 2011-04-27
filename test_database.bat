@echo off

set bin="./bin"
set src="./src"

javac -Xlint -d %bin% -classpath %src% src/Database.java
java -classpath %bin% Database ist411sp11_finalproject

pause