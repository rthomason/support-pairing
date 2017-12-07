#!/bin/bash

if [ $# -ne 1 ]; then
  echo "must supply java process id"
  exit 1
fi

FILE=./tdump_$(date +"%m-%d-%Y_%H%M%S").out
echo "writing to $FILE"

while true
do
  top -n1 -b -H >> $FILE ; jstack $1 >> $FILE ; sleep 3
done

