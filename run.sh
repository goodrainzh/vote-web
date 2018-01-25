#!/bin/bash

PORT=${PORT:-5000}

sleep ${PAUSE:-0}

exec java -jar target/*.jar