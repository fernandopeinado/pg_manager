package br.com.cas10.pgman.jobs;


import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cas10.pgman.PgmanProperties;
import br.com.cas10.pgman.SqlResourceLoader;
import br.com.cas10.pgman.index.ConnectionsSnapshot;
import br.com.cas10.pgman.index.ExportQueue;

import io.micrometer.common.util.StringUtils;


@Service
public class ConnectionsJob {

    private PgmanProperties pgmanProperties;
    private NamedParameterJdbcTemplate jdbc;
    private ExportQueue exportQueue;

    public ConnectionsJob(DataSource dataSource, ExportQueue exportQueue, PgmanProperties pgmanProperties) {
        this.pgmanProperties = pgmanProperties;
        this.jdbc = new NamedParameterJdbcTemplate(dataSource);
        this.exportQueue = exportQueue;
    }

    @Transactional
    public void collect() {
        preCollect();
        collectMetaData();
        String query = SqlResourceLoader.getInstance().loadQuery("connections");

        RowMapper<ConnectionsSnapshot> rowMapper = (ResultSet rs, int index) -> {
            ConnectionsSnapshot snapshot = new ConnectionsSnapshot();
            snapshot.setInstanceName(pgmanProperties.getInstanceName());
            snapshot.setOpen(rs.getLong("connections_open"));
            snapshot.setMax(rs.getLong("connections_max"));
            snapshot.setUsePct(rs.getFloat("connections_use_pct"));
            return snapshot;
        };

        long timestamp = System.currentTimeMillis();
        List<ConnectionsSnapshot> result = this.jdbc.query(query, Collections.EMPTY_MAP, rowMapper);
        result.forEach(snapshot -> snapshot.setTimestamp(timestamp));
        exportQueue.add(result.toArray(new ConnectionsSnapshot[result.size()]));

        postCollect();
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
        System.out.println("Coletando Connections " + LocalDateTime.now());
    }

    private void postCollect() {
        String maxQueryDuration = pgmanProperties.getMaxQueryDuration();
        if (!StringUtils.isBlank(maxQueryDuration)) {
            System.out.println("Matando queries que estão durando mais que " + maxQueryDuration);
            String query = SqlResourceLoader.getInstance().loadQuery("connections");
            Map<String, Object> params = new HashMap<>();
            params.put("max_duration", maxQueryDuration);
            this.jdbc.execute(query, params, (ps) -> null);
        }
    }
}
