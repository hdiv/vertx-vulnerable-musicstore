#!/bin/sh

exec java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=7778 -Dvertx.disableDnsResolver=true -Djava.awt.headless=true -javaagent:/opt/vertx-musicstore/agent/hdiv-ee-agent.jar -Xmx2G -Dhealth.reports.offline=true -Dhdiv.soap.enabled=true -Dhdiv.xss.advanced=true -Dhdiv.console.level=INFO -Dhdiv.file.level=FINER -Dhdiv.toolbar.enabled=true -Dhdiv.config.dir=/opt/vertx-musicstore/agent_config -Dhdiv.console.level=INFO -Dhdiv.server.name=Server-Name  -Dhdiv.agent.plain.save=true -Dhdiv.single.app=true -jar ${APP_FILE} -conf ${APP_CONF}
