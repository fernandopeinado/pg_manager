select pid
     , datname as database
     , usename as username
     , application_name as program
     , query as sql
     , coalesce(wait_event, 'CPU') as event
     , coalesce(wait_event_type, 'CPU') as waitclass
     , 'unknown' as backendType
from pg_stat_activity
where state <> 'idle'
  and application_name <> 'pgman'