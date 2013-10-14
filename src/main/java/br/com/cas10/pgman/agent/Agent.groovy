package br.com.cas10.pgman.agent

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.cas10.pgman.analitics.CircularList;
import br.com.cas10.pgman.analitics.Snapshot;
import br.com.cas10.pgman.analitics.Snapshots;

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
		circList = snapshots.getStorage(type, storageSize);
	}
	
	List<Snapshot> getData() {
		return circList.asList()
	}	
	
}
