#!/bin/bash

dir=$(dirname ${0})
cd ${dir}/../..

if [ -z "$version" ]
then
    echo 'version not set'
    version=`mvn org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.version | grep -v '\['`
fi
echo "version ${version}"

echo "copy lib file"
rm -rf build/lib build/tool
mkdir build/lib build/tool
cp assembly/target/kylin-assembly-${version}-job.jar build/lib/kylin-job-${version}.jar
cp storage-hbase/target/kylin-storage-hbase-${version}-coprocessor.jar build/lib/kylin-coprocessor-${version}.jar
cp jdbc/target/kylin-jdbc-${version}.jar build/lib/kylin-jdbc-${version}.jar
cp tool/target/kylin-tool-${version}-assembly.jar build/tool/kylin-tool-${version}.jar

# Copied file becomes 000 for some env (e.g. my Cygwin)
chmod 644 build/lib/kylin-job-${version}.jar
chmod 644 build/lib/kylin-coprocessor-${version}.jar
chmod 644 build/lib/kylin-jdbc-${version}.jar
chmod 644 build/tool/kylin-tool-${version}.jar
