#!/bin/bash

if [ -z "$1" ]; then
    echo "Usage: $0 <KotlinFile.kts>"
    exit 1
fi

KOTLIN_FILE="$1"
if [ ! -f "$KOTLIN_FILE" ]; then
	echo "File not found: $KOTLIN_FILE"
	exit 1
fi

WATCH_DIR="."

# I don't use sdkman functions Autoloaded
source ~/workspace/script/enable_sdkman.sh
enable_sdkman

while true; do

	inotifywait -e modify,create,delete "$WATCH_DIR"

	time  kotlinc -script "$KOTLIN_FILE"

done

