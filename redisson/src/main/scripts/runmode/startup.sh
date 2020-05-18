#!/bin/bash -
#===============================================================================
#
#          FILE: startup.sh
#
#         USAGE: ./startup.sh
#
#   DESCRIPTION: springboot jar 启动脚本
#
#       OPTIONS: springboot executable jar 依赖系统环境变量：APP_NAME
#  REQUIREMENTS: ---
#          BUGS: ---
#         NOTES: ---
#        AUTHOR: Roc Wong (https://roc-wong.github.io), float.wong@icloud.com
#  ORGANIZATION: 中泰证券
#       CREATED: 04/19/2019 02:21:48 PM
#      REVISION:  ---
#===============================================================================

# set -o nounset                              # Treat unset variables as an error

#===========================================================================================
# dynamic parameter, it will be replaced with mvn process-resources
#===========================================================================================

SERVICE_NAME=@project.artifactId@

LOG_DIR=@logging.path@

GC_DIR=${LOG_DIR}/${SERVICE_NAME}/gc
HEAP_DUMP_DIR=${LOG_DIR}/${SERVICE_NAME}/gc/heapDump

SERVER_PORT=@server.port@

CONTEXT_PATH=@server.servlet.context-path@

#===========================================================================================
# JVM Configuration
#===========================================================================================
#JAVA_OPTS="${JAVA_OPTS} -server -Xms4g -Xmx4g -Xmn2g -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=320m -XX:NewSize=1536m -XX:MaxNewSize=1536m -XX:SurvivorRatio=8"
JAVA_OPTS="${JAVA_OPTS} -XX:+UseConcMarkSweepGC -XX:+UseCMSCompactAtFullCollection -XX:CMSInitiatingOccupancyFraction=70 -XX:+CMSParallelRemarkEnabled -XX:SoftRefLRUPolicyMSPerMB=0 -XX:+CMSClassUnloadingEnabled -XX:SurvivorRatio=8 -XX:-UseParNewGC -XX:+ScavengeBeforeFullGC -XX:+PrintGCDateStamps"
JAVA_OPTS="${JAVA_OPTS} -Dserver.port=$SERVER_PORT -verbose:gc -Xloggc:$GC_DIR/gc.log -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=100 -XX:GCLogFileSize=25M -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${HEAP_DUMP_DIR} -XX:+PrintGCDetails -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime"
JAVA_OPTS="${JAVA_OPTS} -XX:-OmitStackTraceInFastThrow"
JAVA_OPTS="${JAVA_OPTS} -Duser.timezone=Asia/Shanghai -Dclient.encoding.override=UTF-8 -Dfile.encoding=UTF-8 -Djava.security.egd=file:/dev/./urandom"
#export JAVA_OPTS="$JAVA_OPTS -XX:-ReduceInitialCardMarks"
export JAVA_OPTS=${JAVA_OPTS}


PATH_TO_JAR=${SERVICE_NAME}".jar"

SERVER_URL="http://localhost:${SERVER_PORT}/${CONTEXT_PATH}"

if [[ ! -d ${GC_DIR} ]]; then
    mkdir -p ${GC_DIR}
fi

if [[ ! -d "${HEAP_DUMP_DIR}" ]]; then
    mkdir -p ${HEAP_DUMP_DIR}
fi

cd `dirname $0`/..

for i in `ls ${SERVICE_NAME}-*.jar 2>/dev/null`
do
    if [[ ! $i == *"-sources.jar" ]]
    then
        PATH_TO_JAR=$i
        break
    fi
done

if [[ ! -f PATH_TO_JAR && -d current ]]; then
    cd current
    for i in `ls ${SERVICE_NAME}-*.jar 2>/dev/null`
    do
        if [[ ! $i == *"-sources.jar" ]]
        then
            PATH_TO_JAR=$i
            break
        fi
    done
fi

if [[ -f ${SERVICE_NAME}".jar" ]]; then
  rm -rf ${SERVICE_NAME}".jar"
fi

printf "$(date) ==== Starting ==== \n"

ln ${PATH_TO_JAR} ${SERVICE_NAME}".jar"

exec nohup java ${JAVA_OPTS} -jar ${SERVICE_NAME}".jar" "$@" > /dev/null 2>&1 &