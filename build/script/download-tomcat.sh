#!/bin/bash

dir=$(dirname ${0})
cd ${dir}/../..

rm -rf build/tomcat

alias md5cmd="md5sum"
if [[ `uname -a` =~ "Darwin" ]]; then
    alias md5cmd="md5 -q"
fi

if [ ! -f "build/apache-tomcat-7.0.69.tar.gz" ]
then
    echo "no binary file found"
    wget --directory-prefix=build/ http://archive.apache.org/dist/tomcat/tomcat-7/v7.0.69/bin/apache-tomcat-7.0.69.tar.gz || echo "download tomcat failed"
else
    if [ `md5cmd build/apache-tomcat-7.0.69.tar.gz | awk '{print $1}'` != "10a071e5169a1a8b14ff35a0ad181052" ]
    then
        echo "md5 check failed"
        rm build/apache-tomcat-7.0.69.tar.gz
        wget --directory-prefix=build/ http://archive.apache.org/dist/tomcat/tomcat-7/v7.0.69/bin/apache-tomcat-7.0.69.tar.gz || echo "download tomcat failed"
    fi
fi
unalias md5cmd

tar -zxvf build/apache-tomcat-7.0.69.tar.gz -C build/
mv build/apache-tomcat-7.0.69 build/tomcat
rm -rf build/tomcat/webapps/*

mv build/tomcat/conf/server.xml build/tomcat/conf/server.xml.bak
mv build/tomcat/conf/context.xml build/tomcat/conf/context.xml.bak
cp build/deploy/server.xml build/tomcat/conf/server.xml
echo "server.xml overwritten..."
cp build/deploy/context.xml build/tomcat/conf/context.xml
echo "context.xml overwritten..."


if [ -z "$version" ]
then
    echo 'version not set'
    version=`mvn org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.version | grep -v '\['`
fi
echo "version ${version}"
export version

cp tomcat-ext/target/kylin-tomcat-ext-${version}.jar build/tomcat/lib/kylin-tomcat-ext-${version}.jar
chmod 644 build/tomcat/lib/kylin-tomcat-ext-${version}.jar

