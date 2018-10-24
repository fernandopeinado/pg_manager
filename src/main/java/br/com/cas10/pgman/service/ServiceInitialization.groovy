package br.com.cas10.pgman.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

@Component
class ServiceInitialization {

	@Autowired
	private PostgresqlService postgresService

	@Autowired
	private InternalService internalService

	@PostConstruct
	void initialize() {
		internalService.initDatabase()
		postgresService.initDatabase()
	}

}
