#!/bin/bash

OUT_DIR="$PWD/bin"
TEST_DIR="$PWD/tests"
BIN_DIR="$PWD/bin"

JUNIT_JAR="/home/edouard/libs/junit-platform-console-standalone-1.10.0.jar"

echo "Compiling unit tests..."
javac -cp "$OUT_DIR/$JAR_NAME:$JUNIT_JAR" -d "$OUT_DIR" "$TEST_DIR"/*.java || { echo "Unit test compilation failed!"; exit 1; }
echo "Running unit tests..."
java -jar "$JUNIT_JAR" --class-path "$OUT_DIR:$OUT_DIR/$JAR_NAME" --scan-classpath
