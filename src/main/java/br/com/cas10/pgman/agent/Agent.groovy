package br.com.cas10.pgman.agent

import br.com.cas10.pgman.analitics.CircularList
import br.com.cas10.pgman.analitics.Snapshot
import br.com.cas10.pgman.analitics.Snapshots
import groovy.util.logging.Log4j
import org.springframework.beans.factory.annotation.Autowired

import javax.annotation.PostConstruct

@Log4j
abstract class Agent implements Runnable {
	protected String type
	protected long interval
	protected int storageSize
	protected CircularList<Snapshot> circList
	
	@Autowired
	protected Snapshots snapshots	
	
	Agent(String type, long interval, int storageSize) {
		this.type = type
		this.interval = interval
		this.storageSize = storageSize
	}

	@PostConstruct
	private void initialize() {
		long time = interval * storageSize / 60000
		log.info("Agent $type - ${interval} milisec (${time} min) starting")
		circList = snapshots.getStorage(type, storageSize);
	}
	
	List<Snapshot> getData() {
		return circList.asList()
	}	
	
}
