package org.beesden.commerce.controller.interceptor;

import org.beesden.commerce.utils.MasterDataLoader;
import org.beesden.commerce.utils.model.Locality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class LocalityHandler {

	@Autowired
	MasterDataLoader masterDataLoader;

	@ModelAttribute("locality")
	public Locality getLocality() {
		Locality locality = new Locality();
		locality.updateLocality(masterDataLoader);
		return locality;
	}
}