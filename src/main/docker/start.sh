#!/bin/sh

exec java -Dvertx.disableDnsResolver=true ${JAVA_OPTS} -jar ${APP_FILE} -conf ${APP_CONF}
