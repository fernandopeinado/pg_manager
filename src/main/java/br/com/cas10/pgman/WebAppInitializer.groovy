package br.com.cas10.pgman

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import br.com.cas10.pgman.service.ServiceConfig;
import br.com.cas10.pgman.web.WebConfig;

class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return [ServiceConfig.class]
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return [WebConfig.class]
	}

	@Override
	protected String[] getServletMappings() {
		return ["/ws/*"]
	}
}