#!/usr/bin/env sh

mkdir -p server

LATEST_VERSION=$(curl -s https://fill.papermc.io/v3/projects/${PROJECT} | \
    jq -r '.versions | to_entries[0] | .value[0]')

echo "Downloading the latest version: $LATEST_VERSION"

# Get the download URL directly, or null if no stable build exists
PAPERMC_URL=$(curl -s https://fill.papermc.io/v3/projects/${PROJECT}/versions/${LATEST_VERSION}/builds | \
  jq -r 'first(.[] | select(.channel == "STABLE") | .downloads."server:default".url) // "null"')

# Download the latest server version
curl -o ./server/server.jar $PAPERMC_URL
echo "Download completed"
