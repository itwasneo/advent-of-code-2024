#!/usr/bin/bash

# Check if the user provided an argument
if [ -z "$1" ]; then
    echo "Usage: $0 <day_number>"
    exit 1
fi

# Use the provided argument to construct the script file name
SCRIPT_FILE="day_$1.kts"

# Check if the script file exists
if [ ! -f "$SCRIPT_FILE" ]; then
    echo "File not found: $SCRIPT_FILE"
    exit 1
fi

# Run the script with time measurement
time kotlinc -script "$SCRIPT_FILE"

