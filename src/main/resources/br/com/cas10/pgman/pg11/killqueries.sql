select pg_terminate_backend(pid)
from pg_stat_activity
where state <> 'idle'
  and now() - query_start > :max_duration -- '60m'
  and backend_type = 'client backend'