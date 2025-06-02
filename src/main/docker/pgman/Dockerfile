FROM eclipse-temurin:21-jre-alpine

RUN apk update && apk add --no-cache bash

COPY maven/ /opt/

ENTRYPOINT ["/opt/pgman/pgman", "run"]