@echo off
set output="./docs"
set src="./src"
javadoc -author -version -d %output% -classpath %src% ./src/*

pause