#!/bin/bash

sudo apt update

if [[ `which java` == "" ]]
then
  sudo apt-get install openjdk-11-jdk
fi

if [[ `which mvn` == "" ]]
then
  sudo apt install maven
fi

if [[ `which ffmpeg` == "" ]]
then
  sudo apt install ffmpeg
fi

java -jar install.jar