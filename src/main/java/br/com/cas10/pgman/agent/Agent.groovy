package br.com.cas10.pgman.agent

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.cas10.pgman.analitics.CircularList;
import br.com.cas10.pgman.analitics.Snapshot;
import br.com.cas10.pgman.analitics.Snapshots;

abstract class Agent implements Runnable {
	protected String type
	protected long interval
	
	@Autowired
	protected Snapshots snapshots	
	
	Agent(String type, long interval) {
		this.type = type
		this.interval = interval
	}

	List<Snapshot> getData() {
		CircularList<Snapshot> circList = snapshots.getStorage(type)
		return circList.asList()
	}	
	
}
