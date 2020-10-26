#!/bin/bash
if [ ! -e $2 ]; then
  echo $1 >> $2
fi