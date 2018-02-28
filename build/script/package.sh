#!/bin/bash

echo "Checking maven..."

# -z用于判断字符串为空
if [ -z "$(command -v mvn)" ]
then
    echo "Please install maven first so that Kylin packaging can proceed"
    exit 1
else
    echo "maven check passed"
fi

echo "Checking git..."

if [ -z "$(command -v git)" ]
then
    echo "Please install git first so that Kylin packaging can proceed"
    exit 1
else
    echo "git check passed"
fi


echo "Checking npm..."

if [ -z "$(command -v npm)" ]
then
    echo "Please install npm first so that Kylin packaging can proceed"
    exit 1
else
    echo "npm check passed"
fi

dir=$(dirname ${0})
cd ${dir}/../..
version=`mvn org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.version | grep -Ev '(^\[|Download\w+:)'`
echo "kylin version: ${version}"
export version

#commit id
cat << EOF > build/commit_SHA1
LICENSE
EOF
git rev-parse HEAD >> build/commit_SHA1

# 编译所有子项目
sh build/script/build.sh || { exit 1; }
# 下载tomcat并重新配置
sh build/script/download-tomcat.sh || { exit 1; }
# 打包前的准备 server/target/kylin-server-${version}.war => build/tomcat/webapps/kylin.war
sh build/script/prepare.sh || { exit 1; }
# 压缩打包
sh build/script/compress.sh || { exit 1; }

