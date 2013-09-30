package br.com.cas10.pgman.agent

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AgentConfig {

	@Bean
	CpuAgent cpuAgent() {
		return new CpuAgent();
	}
	
}
