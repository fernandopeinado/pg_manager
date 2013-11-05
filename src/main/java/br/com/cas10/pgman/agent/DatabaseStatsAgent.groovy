package br.com.cas10.pgman.agent

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.cas10.pgman.analitics.Snapshot;
import br.com.cas10.pgman.analitics.Snapshots;
import br.com.cas10.pgman.service.PostgresqlService;

@Component
class DatabaseStatsAgent extends Agent {

	private static final Pattern PATTERN = Pattern.compile("\\s+(\\d+)\\s(.*)")
	
	@Autowired
	private PostgresqlService postgresqlService;
	
	DatabaseStatsAgent() {
		super("database_stats", 6000L, 300)
	}
	
	@Override
	public void run() {
		DatabaseStatsSnapshot s = new DatabaseStatsSnapshot(this.interval)
		s.type = this.type
		s.timestamp = System.currentTimeMillis()
		List<Map<String,Object>> list = postgresqlService.getDatabaseStats()
		list.each { row ->
			Map<String, Long> data = new HashMap<String, Long>()
			row.each { Entry<String, Object> entry ->
				if (entry.key != 'database') {
					data[entry.key] = (Long) entry.value
				}
			}
			s.observations[row.database] = data
		}
		snapshots.add(s)
	}
	
	static class DatabaseStatsSnapshot extends Snapshot {
		
		double interval = 1.0d

		DatabaseStatsSnapshot(long interval) {
			this.interval = interval / 1000.0d;
		}
		
		@Override
		Object delta(Object last, Object current) {
			if (last != null) {
				Map<String, Long> lastMap = (Map<String, Long>) last
				Map<String, Long> currentMap = (Map<String, Long>) current
				Map<String, Long> deltaMap = new HashMap<String, Long>()
				if (currentMap['xact_commit'] != null) {
					deltaMap['commits_ps'] = (currentMap['xact_commit'] - lastMap['xact_commit']) / (double) interval
				}
				if (currentMap['xact_rollback'] != null) {
					deltaMap['rollback_ps'] = (currentMap['xact_rollback'] - lastMap['xact_rollback']) / (double) interval
				}
				if (currentMap['blks_read'] != null) {
					deltaMap['blks_read_ps'] = (currentMap['blks_read'] - lastMap['blks_read']) / (double) interval
				}
				if (currentMap['blks_hit'] != null) {
					deltaMap['blks_hit_ps'] = (currentMap['blks_hit'] - lastMap['blks_hit']) / (double) interval
				}
				if (currentMap['tup_returned'] != null) {
					deltaMap['tup_returned_ps'] = (currentMap['tup_returned'] - lastMap['tup_returned']) / (double) interval
				}
				if (currentMap['tup_fetched'] != null) {
					deltaMap['tup_fetched_ps'] = (currentMap['tup_fetched'] - lastMap['tup_fetched']) / (double) interval
				}
				if (currentMap['tup_inserted'] != null) {
					deltaMap['tup_inserted_ps'] = (currentMap['tup_inserted'] - lastMap['tup_inserted']) / (double) interval
				}
				if (currentMap['tup_updated'] != null) {
					deltaMap['tup_updated_ps'] = (currentMap['tup_updated'] - lastMap['tup_updated']) / (double) interval
				}
				if (currentMap['tup_deleted'] != null) {
					deltaMap['tup_deleted_ps'] = (currentMap['tup_deleted'] - lastMap['tup_deleted']) / (double) interval
				}
				return deltaMap;
			}
			return null;
		}
	}
}
