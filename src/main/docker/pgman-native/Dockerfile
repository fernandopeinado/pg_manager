FROM debian:bookworm-slim

#RUN apk update && apk add --no-cache bash

COPY maven/ /opt/

ENTRYPOINT ["/opt/pgman/pgman.sh", "run"]