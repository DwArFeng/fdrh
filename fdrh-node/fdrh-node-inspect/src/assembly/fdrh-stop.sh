#!/bin/sh
# 程序的根目录
basedir=/usr/local/fdrh-inspect

PID=$(cat $basedir/fdrh.pid)
kill "$PID"
