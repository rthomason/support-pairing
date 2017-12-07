#!/bin/bash

JAR="target/pairing-0.1.jar"

if [ ! -f $JAR ]; then
  mvn clean package
fi

java -jar $JAR
