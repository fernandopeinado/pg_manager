package br.com.cas10.pgman.jobs;

import br.com.cas10.pgman.SqlResourceLoader;
import br.com.cas10.pgman.ash.ActiveSession;
import br.com.cas10.pgman.index.ExportQueue;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AshJob {

    static final int SNAPSHOT_SAMPLES = 15;

    private NamedParameterJdbcTemplate jdbc;
    private ExportQueue exportQueue;
    private List<ActiveSession> activeSessions = new ArrayList<>();
    private int samples = 0;

    public AshJob(DataSource dataSource, ExportQueue snapshots) {
        this.jdbc = new NamedParameterJdbcTemplate(dataSource);
        this.exportQueue = snapshots;
    }

    @Transactional
    public void collect() {
        collectMetaData();
        String query = SqlResourceLoader.getInstance().loadQuery("active_sessions");

        RowMapper<ActiveSession> rowMapper = (ResultSet rs, int index) -> {
            ActiveSession session = new ActiveSession();
            session.pid = "" + rs.getInt(1);
            session.database = rs.getString(2);
            session.username = rs.getString(3);
            session.program = rs.getString(4);
            session.query = rs.getString(5);
            session.event = rs.getString(6);
            session.waitClass = rs.getString(7);
            session.backendType = rs.getString(8);
            session.value = 1.0d / SNAPSHOT_SAMPLES;
            return session;
        };

        long timestamp = System.currentTimeMillis();
        List<ActiveSession> sample = this.jdbc.query(query, Collections.EMPTY_MAP, rowMapper);
        synchronized (activeSessions) {
            activeSessions.addAll(sample);
            samples++;
            if (samples == SNAPSHOT_SAMPLES) {
                System.out.println("Coletando ASH " + LocalDateTime.now());
                activeSessions.forEach(as -> {
                    as.timestamp = timestamp;
                    exportQueue.add(as);
                });
                activeSessions = new ArrayList<>();
                samples = 0;
            }
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

}
