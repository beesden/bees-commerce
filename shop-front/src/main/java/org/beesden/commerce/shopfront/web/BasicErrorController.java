package org.beesden.commerce.shopfront.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class BasicErrorController implements ErrorController {

	private static final String PATH = "/error";

	@Autowired
	private ErrorAttributes errorAttributes;

	public String getErrorPath() {
		return PATH;
	}

	@RequestMapping(value = PATH)
	public ModelAndView webError(HttpServletRequest request) {
		ModelAndView model = new ModelAndView("error");
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		model.addObject("ctx", sra.getRequest().getContextPath());

		model.addObject("error", errorAttributes.getErrorAttributes(new ServletRequestAttributes(request), true));

		return model;
	}
}