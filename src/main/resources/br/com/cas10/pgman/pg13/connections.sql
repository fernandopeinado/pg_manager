with state as (
select
    (select sum(numbackends) from pg_stat_database) as connections_open
    , (select setting from pg_settings where name = 'max_connections')::bigint as connections_max
)
select *, ((connections_open * 10000 / connections_max)::float8 / 100) as connections_use_pct from state
