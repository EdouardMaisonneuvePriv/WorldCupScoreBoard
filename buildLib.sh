#!/bin/bash

# Set the SRC_DIR to the current working directory
SRC_DIR="$PWD/src"
BIN_DIR="$PWD/bin"

# Compile the Java source files using absolute paths
javac -d "$BIN_DIR" "$SRC_DIR/module-info.java" "$SRC_DIR/api/"*.java "$SRC_DIR/internal"/*.java
