#!/bin/bash

set -euo pipefail

usage() {
    echo "usage: pgman run"
}

pgman_run() {
    PGMAN_SYS_PROPS+=" -Dspring.profiles.include=foreground"
    exec $pgman_bin_file $PGMAN_SYS_PROPS
}

pgman_home=$(dirname "$(readlink -f "$BASH_SOURCE")")
pgman_bin_file=$(echo "$pgman_home"/pgman)
logs_dir="$pgman_home/logs"

if [[ -z ${PGMAN_SYS_PROPS:-} ]]; then
    PGMAN_SYS_PROPS="-Dpgman.home=$pgman_home"
    PGMAN_SYS_PROPS+=" -Dspring.config.additional-location=$pgman_home/config.yml"
fi

mkdir -p "$logs_dir"

case "${1-}" in
    run) pgman_run ;; *) usage ;;
esac

