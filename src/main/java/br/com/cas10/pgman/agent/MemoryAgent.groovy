package br.com.cas10.pgman.agent

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import br.com.cas10.pgman.analitics.Snapshot;
import br.com.cas10.pgman.analitics.Snapshots;

@Component
class MemoryAgent extends Agent {

	private static final Pattern PATTERN = Pattern.compile("\\s+(\\d+)\\s(.*)")
	
	MemoryAgent() {
		super("memory", 12000L, 300)
	}
	
	@Override
	public void run() {
		Snapshot s = new Snapshot()
		s.type = this.type
		s.timestamp = System.currentTimeMillis()
		"vmstat -s".execute().text.eachLine { l ->
			Matcher matcher = PATTERN.matcher(l)
			if (matcher.matches()) {
				String obs = matcher.group(2)
				Long value = Long.valueOf(matcher.group(1))
				if (obs.endsWith("total memory")) {
					s.observations["total"] = value 
				}
				else if (obs.endsWith("free memory")) {
					s.observations["free"] = value
				}
				else if (obs.endsWith("buffer memory")) {
					s.observations["buffer"] = value
				} 
				else if (obs.endsWith("swap cache")) {
					s.observations["cache"] = value
				}
				else if (obs.endsWith("total swap")) {
					s.observations["swap_total"] = value
				}
				else if (obs.endsWith("swap cache")) {
					s.observations["swap_used"] = value
				}
			}
		}
		s.observations["used"] = s.observations["total"] - (s.observations["free"] + s.observations["buffer"] + s.observations["cache"]);
		snapshots.add(s)
	}
}
