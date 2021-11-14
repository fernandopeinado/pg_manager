package br.com.cas10.pgman.jobs;

import br.com.cas10.pgman.SqlResourceLoader;
import br.com.cas10.pgman.index.ExportQueue;
import br.com.cas10.pgman.index.IndexService;
import br.com.cas10.pgman.index.QuerySnapshot;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class StatementsJob {

    private NamedParameterJdbcTemplate jdbc;
    private ExportQueue exportQueue;

    public StatementsJob(DataSource dataSource, ExportQueue exportQueue) {
        this.jdbc = new NamedParameterJdbcTemplate(dataSource);
        this.exportQueue = exportQueue;
    }

    @Transactional
    public void collect() {
        try {
            preCollect();
            collectMetaData();
            String query = SqlResourceLoader.getInstance().loadQuery("statements");

            RowMapper<QuerySnapshot> rowMapper = (ResultSet rs, int index) -> {
                QuerySnapshot snapshot = new QuerySnapshot();
                snapshot.setQueryId(rs.getLong("queryid"));
                snapshot.setDatabase(rs.getString("database"));
                snapshot.setQuery(rs.getString("query"));
                snapshot.setCalls(rs.getLong("calls"));
                snapshot.setTotalTime(rs.getDouble("totalTime"));
                snapshot.setMinTime(rs.getDouble("minTime"));
                snapshot.setMaxTime(rs.getDouble("maxTime"));
                snapshot.setMeanTime(rs.getDouble("meanTime"));
                snapshot.setRows(rs.getLong("rows"));
                snapshot.setBlockReadTime(rs.getDouble("blockReadTime"));
                snapshot.setBlockWriteTime(rs.getDouble("blockWriteTime"));
                return snapshot;
            };

            long timestamp = System.currentTimeMillis();
            List<QuerySnapshot> result = this.jdbc.query(query, Collections.EMPTY_MAP, rowMapper);
            for (QuerySnapshot snapshot : result) {
                snapshot.setTimestamp(timestamp);
                exportQueue.add(snapshot);
            }
        } finally {
            postColelct();
        }
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
        System.out.println("Coletando Statements " + LocalDateTime.now());
    }

    private void postColelct() {
        this.jdbc.queryForList("SELECT pg_stat_statements_reset();", Collections.EMPTY_MAP);
    }

}
