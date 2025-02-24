#!/bin/bash

# Set the SRC_DIR to the current working directory
SRC_DIR="$PWD/src"
BIN_DIR="$PWD/bin"

# Create the bin directory if it doesn't exist
mkdir -p "$BIN_DIR"

# Compile the Java source files using modules
javac --module-source-path "$SRC_DIR" -d "$BIN_DIR" $(find "$SRC_DIR/ScoreboardLib" -name "*.java")