#!/bin/bash

# $0 代表脚本名称
# dirname命令可以取给定路径的目录部分
echo  ${0}
dir=$(dirname ${0})
echo ${dir}
cd ${dir}/../..

if [ -z "$version" ]
then
    echo 'version not set'
    version=`mvn org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.version | grep -v '\['`
fi
echo "version ${version}"
export version

sh build/script/prepare_libs.sh || { exit 1; }

cp server/target/kylin-server-${version}.war build/tomcat/webapps/kylin.war
chmod 644 build/tomcat/webapps/kylin.war

echo "add js css to war"
if [ ! -d "webapp/dist" ]
then
    echo "error generate js files"
    exit 1
fi

cd webapp/dist
for f in * .[!.]*
do
    echo "Adding $f to war"
    jar -uf ../../build/tomcat/webapps/kylin.war $f
done
