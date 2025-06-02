package br.com.cas10.pgman.jobs;


import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cas10.pgman.PgmanProperties;
import br.com.cas10.pgman.SqlResourceLoader;
import br.com.cas10.pgman.index.ConnectionsSnapshot;
import br.com.cas10.pgman.index.DatabaseSnapshot;
import br.com.cas10.pgman.index.ExportQueue;


@Service
public class DatabasesJob {

    private PgmanProperties pgmanProperties;
    private NamedParameterJdbcTemplate jdbc;
    private ExportQueue exportQueue;

    public DatabasesJob(DataSource dataSource, ExportQueue exportQueue, PgmanProperties pgmanProperties) {
        this.pgmanProperties = pgmanProperties;
        this.jdbc = new NamedParameterJdbcTemplate(dataSource);
        this.exportQueue = exportQueue;
    }

    @Transactional
    public void collect() {
        preCollect();
        collectMetaData();
        String query = SqlResourceLoader.getInstance().loadQuery("databases");

        RowMapper<DatabaseSnapshot> rowMapper = (ResultSet rs, int index) -> {
            DatabaseSnapshot snapshot = new DatabaseSnapshot();
            snapshot.setInstanceName(pgmanProperties.getInstanceName());
            snapshot.setDatabase(rs.getString("datname"));
            snapshot.setNumBackends(rs.getLong("numbackends"));
            snapshot.setXactCommit(rs.getLong("xact_commit"));
            snapshot.setXactRollback(rs.getLong("xact_rollback"));
            snapshot.setBlksHit(rs.getLong("blks_hit"));
            snapshot.setBlksRead(rs.getLong("blks_read"));
            snapshot.setCacheHit(rs.getFloat("cache_hit"));
            snapshot.setBlksReadTime(rs.getDouble("blk_read_time"));
            snapshot.setBlksWriteTime(rs.getDouble("blk_write_time"));
            snapshot.setConflicts(rs.getLong("conflicts"));
            snapshot.setDeadlocks(rs.getLong("deadlocks"));
            snapshot.setTupFeched(rs.getLong("tup_fetched"));
            snapshot.setTupReturned(rs.getLong("tup_returned"));
            snapshot.setTupInserted(rs.getLong("tup_inserted"));
            snapshot.setTupUpdated(rs.getLong("tup_updated"));
            snapshot.setTupDeleted(rs.getLong("tup_deleted"));
            snapshot.setDatabaseSize(rs.getLong("database_size"));
            return snapshot;
        };

        long timestamp = System.currentTimeMillis();
        List<DatabaseSnapshot> result = this.jdbc.query(query, Collections.EMPTY_MAP, rowMapper);
        result.forEach(snapshot -> snapshot.setTimestamp(timestamp));
        exportQueue.add(result.toArray(new DatabaseSnapshot[result.size()]));
    }

    private void collectMetaData() {
        if (!SqlResourceLoader.getInstance().hasMetaData()) {
            ConnectionCallback callback = (Connection con) -> {
                SqlResourceLoader.getInstance().collectMetaData(con);
                return null;
            };
            jdbc.getJdbcOperations().execute(callback);
        }
    }

    private void preCollect() {
        System.out.println("Coletando Databases " + LocalDateTime.now());
    }

}
