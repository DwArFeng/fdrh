#!/bin/bash
# 程序的根目录
basedir=/usr/local/fdrh-all

PID=$(cat $basedir/fdrh.pid)
kill "$PID"
