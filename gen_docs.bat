@echo off
set output="./javadocs"
set src="./src"
javadoc -author -version -d %output% -classpath %src% ./src/*

pause