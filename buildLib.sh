#!/bin/bash

# Set the SRC_DIR to the current working directory
SRC_DIR="$PWD/src"
BIN_DIR="$PWD/bin"

# Create the bin directory if it doesn't exist
mkdir -p "$BIN_DIR"

# Compile the Java source files using modules
javac --module-source-path "$SRC_DIR" -d "$BIN_DIR" $(find "$SRC_DIR/ScoreboardLib" -name "*.java")

# Create the JAR file
echo "Creating JAR file..."
JAR_NAME="scoreboard-library.jar"
jar --create --file="$BIN_DIR/$JAR_NAME" --main-class="ScoreboardLib.api.Scoreboard" -C "$BIN_DIR" .

# Confirm the JAR creation
if [ -f "$BIN_DIR/$JAR_NAME" ]; then
  echo "JAR file created successfully: $BIN_DIR/$JAR_NAME"
else
  echo "JAR creation failed!"
  exit 1
fi