#!/bin/bash

LOAD_THRESHOLD=0.7
APP_NAME="pairing-0.1"

while true
do
  AVG=$(cat /proc/loadavg | cut -d ' ' -f1)
  PID=$(ps aux | grep "pairing-0.1" | grep -v grep | awk '{print $2}')

  if (( $(echo "$AVG > $LOAD_THRESHOLD" | bc -l) )); then
    FILE=./tdump_$(date +"%m-%d-%Y_%H%M%S").out
    echo "writing top and thread dumps to $FILE"
    top -n1 -b -H >  $FILE ; jstack $PID >> $FILE ; sleep 3
    top -n1 -b -H >> $FILE ; jstack $PID >> $FILE ; sleep 3
    top -n1 -b -H >> $FILE ; jstack $PID >> $FILE ; sleep 3
  fi

  sleep 60
done

