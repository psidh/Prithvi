#!/bin/bash

mkdir -p out


javac -d out $(find src -name "*.java") $(find tests -name "*.java")


java -cp out src.Prithvi
