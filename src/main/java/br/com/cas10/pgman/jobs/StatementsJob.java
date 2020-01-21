package br.com.cas10.pgman.jobs;

import br.com.cas10.pgman.SqlResourceLoader;
import br.com.cas10.pgman.index.QueryService;
import br.com.cas10.pgman.index.QuerySnapshot;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class StatementsJob {

    private NamedParameterJdbcTemplate jdbc;
    private QueryService queryService;

    public StatementsJob(DataSource dataSource, QueryService queryService) {
        this.jdbc = new NamedParameterJdbcTemplate(dataSource);
        this.queryService = queryService;
    }

    @Transactional
    public void collect() {
        try {
            preCollect();
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

            List<QuerySnapshot> result = this.jdbc.query(query, Collections.EMPTY_MAP, rowMapper);
            queryService.save(result.toArray(new QuerySnapshot[result.size()]));
        } finally {
            postColelct();
        }
    }

    private void preCollect() {
        System.out.println("Coletando " + LocalDateTime.now());
    }

    private void postColelct() {
        this.jdbc.queryForList("SELECT pg_stat_statements_reset();", Collections.EMPTY_MAP);
    }

}
