package org.beesden.cms.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * New controller for handling the new UI / API interactions.
 *
 * @author Pulse Innovations Ltd
 */
@Controller
public class ViewController {
	@Autowired
	private Environment env;

	@Value( "${bees.api.url}" )
	private String apiEndpoint;

	@Value( "${google.maps.api}" )
	private String googleMapsKey;

	@RequestMapping( "/**" )
	public ModelAndView webView( HttpServletRequest request ) {
		ModelAndView model = new ModelAndView( "web" );
		ServletRequestAttributes sra = ( ServletRequestAttributes ) RequestContextHolder.getRequestAttributes();
		model.addObject( "ctx", sra.getRequest().getContextPath() );

		String environment = "local";
		if ( env.getActiveProfiles().length > 0 ) {
			environment = env.getActiveProfiles()[ 0 ];
		}

		model.addObject( "apiEndpoint", apiEndpoint );
		model.addObject( "appVersion", getClass().getPackage().getImplementationVersion() );
		model.addObject( "debug", "local".equals( environment ) || "true".equals( request.getParameter( "debug" ) ) );
		model.addObject( "environment", environment );
		model.addObject( "googleMapsKey", googleMapsKey );

		return model;
	}
}
