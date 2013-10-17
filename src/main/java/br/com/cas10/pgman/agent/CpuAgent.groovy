package br.com.cas10.pgman.agent

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import br.com.cas10.pgman.analitics.Snapshot;
import br.com.cas10.pgman.analitics.Snapshots;

@Component
class CpuAgent extends Agent {

	private static final Pattern PATTERN = Pattern.compile("\\s+(\\d+)\\s(.*)")
	
	CpuAgent() {
		super("cpu", 6000L, 300)
	}
	
	@Override
	public void run() {
		CpuSnapshot s = new CpuSnapshot()
		s.type = this.type
		s.timestamp = System.currentTimeMillis()
		Long userCPU = 0
		Long systCPU = 0
		"vmstat -s".execute().text.eachLine { l ->
			Matcher matcher = PATTERN.matcher(l)
			if (matcher.matches()) {
				String obs = matcher.group(2)
				Long value = Long.valueOf(matcher.group(1))
				if (["non-nice user cpu ticks", "nice user cpu ticks"].contains(obs)) {
					userCPU += value
				}
				else if (["system cpu ticks", "IRQ cpu ticks", "softirq cpu ticks"].contains(obs)) {
					systCPU += value
				}
				else if (obs == "idle cpu ticks") {
					s.observations["idle"] = value
				}
				else if (obs == "IO-wait cpu ticks") {
					s.observations["wait"] = value
				}
				else if (obs == "stolen cpu ticks") {
					s.observations["steal"] = value
				}
			}
		}
		s.observations["user"] = userCPU
		s.observations["system"] = systCPU
		snapshots.add(s)
	}
	
	static class CpuSnapshot extends Snapshot {

		@Override
		public void postDeltas(Snapshot prev) {
			if (deltaObs.user != null && deltaObs.idle != null && deltaObs.system != null && deltaObs.wait != null && deltaObs.steal != null) {
				long total = deltaObs.user + deltaObs.idle + deltaObs.system + deltaObs.wait + deltaObs.steal
				deltaObs["user_pc"] = deltaObs.user * 100.0d / total
				deltaObs["system_pc"] = deltaObs.system * 100.0d / total
				deltaObs["idle_pc"] = deltaObs.idle * 100.0d / total
				deltaObs["wait_pc"] = deltaObs.wait * 100.0d / total
				deltaObs["steal_pc"] = deltaObs.steal * 100.0d / total
			}
		}
	}
}
