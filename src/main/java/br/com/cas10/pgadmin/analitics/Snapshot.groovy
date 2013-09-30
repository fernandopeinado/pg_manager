package br.com.cas10.pgadmin.analitics

import java.sql.Time;
import java.sql.Timestamp;

class Snapshot {
	String type
	long timestamp
	Map<String, Object> observations = new HashMap()
	Map<String, Object> deltaObs = new HashMap()
	
	void calculateDelta(Snapshot prev) {
		for (obs in observations.entrySet()) {
			Object lastVal = prev.observations[obs.key];
			deltaObs[obs.key] = delta(lastVal, obs.value);
		}
		postDeltas(prev);
	}
	
	Object delta(Object last, Object current) {
		if (last != null) {
			return current - last;
		}
		return null;
	}

	String getDateTime() {
		new Time(timestamp);
	}
		
	void postDeltas(Snapshot prev) {
	}
}
