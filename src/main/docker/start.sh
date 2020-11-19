#!/bin/sh

DEFAULT_JAVA_OPTS="-Dvertx.disableDnsResolver=true -Xmx2G -Djava.awt.headless=true -javaagent:/hdiv/hdiv-ee-agent.jar -Dhdiv.config.dir=/hdiv/config -Dhdiv.file.level=FINER -Dhdiv.toolbar.enabled=true -Dhdiv.agent.plain.save=true -Dhdiv.single.app=true"
JAVA_OPTS="${DEFAULT_JAVA_OPTS} ${JAVA_OPTS:-}"
set -x
exec java ${JAVA_OPTS} -jar "${APP_FILE}" -conf "${APP_CONF}"
