select datname
    , numbackends
    , xact_commit
    , xact_rollback
    , blks_hit
    , blks_read
    , (10000 * blks_hit / (blks_hit + blks_read))::float8 / 100 as cache_hit
    , blk_read_time
    , blk_write_time
    , conflicts
    , deadlocks
    , tup_fetched
    , tup_returned
    , tup_inserted
    , tup_updated
    , tup_deleted
    , pg_catalog.pg_database_size(datname) database_size
from pg_stat_database
where datname not like 'template%'


