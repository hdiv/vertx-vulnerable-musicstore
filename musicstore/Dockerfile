FROM openjdk:8-jre-alpine

RUN apk add --update curl && rm -rf /var/cache/apk/*

ENV APP_HOME /opt/vertx-musicstore
ENV APP_SCRIPT start.sh
ENV APP_FILE vertx-musicstore.jar
ENV APP_CONF conf.json

EXPOSE 8080

COPY src/main/docker/$APP_SCRIPT $APP_HOME/
COPY target/$APP_FILE $APP_HOME/
COPY src/main/docker/$APP_CONF $APP_HOME/

RUN mkdir /opt/vertx-musicstore/agent_config
RUN mkdir /opt/vertx-musicstore/agent/
COPY src/main/docker/$APP_SCRIPT $APP_HOME/
COPY hdiv/agent/* /opt/vertx-musicstore/agent/
COPY hdiv/conf/* /opt/vertx-musicstore/agent_config/

WORKDIR $APP_HOME

ENTRYPOINT ["sh", "-c"]
CMD ["$APP_HOME/$APP_SCRIPT"]
