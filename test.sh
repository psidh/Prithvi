#!/bin/bash

javac -d out $(find src -name "*.java") $(find tests -name "*.java")

java -cp out tests.ParserTest