#!/bin/bash
# Script used to download external Java libraries

# Save the path to this file
LIBDIR_PATH=$(dirname $0)

# Download json-simple-1.1.jar
wget http://www.java2s.com/Code/JarDownload/json-simple/json-simple-1.1.jar.zip

# Unzip jar file
unzip ./json-simple-1.1.jar.zip -d $LIBDIR_PATH

# Clean the zip file
rm ./json-simple-1.1.jar.zip
