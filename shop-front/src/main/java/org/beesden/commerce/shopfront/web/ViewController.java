package org.beesden.commerce.shopfront.web;

import org.beesden.commerce.content.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * New controller for handling the new UI / API interactions.
 *
 * @author Pulse Innovations Ltd
 */
@Controller
public class ViewController {

	@Autowired
	private Environment env;

	@Autowired
	private ProductService productService;

	@RequestMapping("/product/{productId}")
	public ModelAndView webView(@PathVariable String productId) {
		ModelAndView model = new ModelAndView("product_details");

		model.addObject("env", env);
		model.addObject("ctx", "/");

		model.addObject("product", productService.getProduct(productId));

		return model;
	}
}
