#!/bin/bash

SERVER=${DEPLOY_SERVER}
USER=${DEPLOY_USER}
DEPLOY_KEY="${DEPLOY_KEY}"
KEY_PATH=/tmp/deploy_key
TARGET_DIR=/home/ec2-user/app
JAR_FILE="chatdiary-0.0.1-SNAPSHOT-plain.jar"

echo "$DEPLOY_KEY" > $KEY_PATH
chmod 600 $KEY_PATH

echo "Deploying to server: $SERVER"
echo "Deploying as user: $USER"
echo "Target directory: $TARGET_DIR"

echo "Copying JAR file to server..."
scp -i $KEY_PATH ./build/libs/$JAR_FILE $USER@$SERVER:$TARGET_DIR

echo "Executing application on server..."
ssh -i $KEY_PATH $USER@$SERVER << 'ENDSSH'
cd $TARGET_DIR
java -jar $JAR_FILE &
ENDSSH

# 임시 키 파일 삭제
rm $KEY_PATH
