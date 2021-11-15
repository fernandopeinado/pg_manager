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
import java.util.*;

@Service
public class AshJob {

    static final int SNAPSHOT_SAMPLES = 15;

    private NamedParameterJdbcTemplate jdbc;
    private ExportQueue exportQueue;
    private Map<String, String> waitClasses = new HashMap<>();
    private List<ActiveSession> activeSessions;
    private int samples;
    private Set<String> unsampledClasses;

    public AshJob(DataSource dataSource, ExportQueue snapshots) {
        this.jdbc = new NamedParameterJdbcTemplate(dataSource);
        this.exportQueue = snapshots;
        this.waitClasses.put("CPU", "00");
        this.waitClasses.put("IO", "01");
        this.waitClasses.put("LWLock", "02");
        this.waitClasses.put("Lock", "03");
        this.waitClasses.put("Activity", "04");
        this.waitClasses.put("Client", "05");
        this.waitClasses.put("BufferPin", "06");
        this.waitClasses.put("IPC", "07");
        this.waitClasses.put("Timeout", "08");
        this.waitClasses.put("Extension", "09");
        this.waitClasses.put("Other", "99");
        resetSamples();
    }

    private void resetSamples() {
        this.samples = 0;
        this.unsampledClasses = new HashSet<>(waitClasses.keySet());
        this.activeSessions = new ArrayList<>();
    }

    private String buildWaitClassDescription(String waitClass) {
        String prefix = this.waitClasses.get(waitClass);
        if (prefix != null) {
            return prefix  + '_' + waitClass;
        }
        return "99_Other";
    }

    private ActiveSession createUnsampledClass(String waitClass) {
        ActiveSession session = new ActiveSession();
        session.waitClass = buildWaitClassDescription(waitClass);
        session.value = 0d;
        return session;
    }

    @Transactional
    public void collect() {
        collectMetaData();
        String query = SqlResourceLoader.getInstance().loadQuery("active_sessions");
        if (query != null) {
            RowMapper<ActiveSession> rowMapper = (ResultSet rs, int index) -> {
                ActiveSession session = new ActiveSession();
                session.pid = "" + rs.getInt(1);
                session.database = rs.getString(2);
                session.username = rs.getString(3);
                session.program = rs.getString(4);
                session.query = rs.getString(5);
                session.event = rs.getString(6);
                String waitClass = rs.getString(7);
                session.waitClass = buildWaitClassDescription(waitClass);
                if (unsampledClasses.contains(waitClass)) {
                    this.unsampledClasses.remove(waitClass);
                }
                session.backendType = rs.getString(8);
                session.value = 1.0d / SNAPSHOT_SAMPLES;
                return session;
            };

            long timestamp = System.currentTimeMillis();
            List<ActiveSession> sample = this.jdbc.query(query, Collections.EMPTY_MAP, rowMapper);
            synchronized (this.activeSessions) {
                this.activeSessions.addAll(sample);
                this.samples++;
                if (this.samples == SNAPSHOT_SAMPLES) {
                    System.out.println("Coletando ASH " + LocalDateTime.now());
                    this.activeSessions.forEach(as -> {
                        as.timestamp = timestamp;
                        this.exportQueue.add(as);
                    });
                    this.unsampledClasses.forEach(usc -> {
                        ActiveSession as = createUnsampledClass(usc);
                        as.timestamp = timestamp;
                        this.exportQueue.add(as);
                    });
                    resetSamples();
                }
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
