@echo off

set GRADLE_USER_HOME=.gradle
set GRADLE_OPTS=" -Dorg .gradle.daemon=false\

.\\gradle\\gradle-8.0.2\\bin\\gradle.bat %*
