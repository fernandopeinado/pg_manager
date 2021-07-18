select st.queryid as queryid
    , db.datname as database
    , st.query as query
    , st.calls as calls
    , st.total_exec_time as totalTime
    , st.min_exec_time as minTime
    , st.max_exec_time as maxTime
    , st.mean_exec_time as meanTime
    , st.rows as rows
    , st.blk_read_time as blockReadTime
    , st.blk_write_time as blockWriteTime
from pg_stat_statements st
    inner join pg_database db on st.dbid = db.oid