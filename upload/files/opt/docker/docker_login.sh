#!/bin/bash

getValue() {
    local file=".env"
    local name="$1"
    local line
    line=$(grep -E "^$name=" "$file") || return 1
    echo "${line#*=}" | tr -d '\r\n'
}

REGISTRY_URL=$(getValue "REGISTRY_URL")
REGISTRY_USERNAME=$(getValue "REGISTRY_USERNAME")
REGISTRY_PASSWORD=$(getValue "REGISTRY_PASSWORD")

docker login -u $REGISTRY_USERNAME -p $REGISTRY_PASSWORD $REGISTRY_URL