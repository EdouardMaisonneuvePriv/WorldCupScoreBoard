#!/bin/bash

OUT_DIR="$PWD/bin"
TEST_DIR="$PWD/tests"
SRC_DIR="$PWD/src"
BIN_DIR="$PWD/bin"
JUNIT_JAR="/home/edouard/libs/junit-platform-console-standalone-1.10.0.jar"

# Create the bin directory if it doesn't exist
mkdir -p "$OUT_DIR"

# Compile the main source code
echo "Compiling main source code..."
javac -d "$OUT_DIR" "$SRC_DIR/ScoreboardLib/api/Scoreboard.java" "$SRC_DIR/ScoreboardLib/internal/Match.java" "$SRC_DIR/ScoreboardLib/internal/Team.java" || { echo "Main source code compilation failed!"; exit 1; }

# Compile the unit tests (ensure ScoreboardLib is in classpath)
echo "Compiling unit tests..."
javac -cp "$OUT_DIR:$JUNIT_JAR" -d "$OUT_DIR" "$TEST_DIR"/*.java || { echo "Unit test compilation failed!"; exit 1; }

# Run the tests
echo "Running unit tests..."
java -cp "$OUT_DIR:$JUNIT_JAR" org.junit.platform.console.ConsoleLauncher --scan-classpath