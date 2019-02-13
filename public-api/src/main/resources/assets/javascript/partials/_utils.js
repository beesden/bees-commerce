var btf = btf || {};

btf.utils = (function(d) {

	var config = {
		dataAttr: 'data-js',
		dataDelim: '-',
		dataSplit: ' ',
		nameSpace: btf
	};

	/** 
	 *	Submit an AJAX request to the server. 
	 *	Designed to match the most popular jQuery ajax config options
	 *
	 *	@param {object} config - A set of key/value pairs that configure the Ajax request - see http://api.jquery.com/jquery.ajax/
	*/
	function ajax(config) {
		var xmlhttp = new XMLHttpRequest(),
			requestUrl = config.url,
			response;
		// Assemble the request URL and add parameters if required
		if (!requestUrl) {
			return;
		}	
		config.data = typeof config.data === 'object' ? objectToString(config.data) : config.data;
		if (!config.method || config.method.toUpperCase() === 'GET') {
			requestUrl += (config.url.indexOf('?') < 0 ? '?' : '&') + config.data;
		}
		// Submit AJAX request function
		xmlhttp.onreadystatechange = function () {
			// Allow for state updates
			if (config.stateChange && typeof(config.stateChange) === 'function') {
				config.stateChange(xmlhttp);
			}
			if (xmlhttp.readyState === 4) {
				if (xmlhttp.status === 200) {
					response = xmlhttp.response
					// Convert to JSON if possible
					if (config.dataType === 'json') {
						try {
							response = JSON.parse(response);
						} catch(err) {
							// Not JSON!
						}
					}
					// Run 'success' function on 200 response
					if (config.success && typeof(config.success) === 'function') {
						config.success(response, xmlhttp.statusText, xmlhttp);
					}
				} else {
					// Run 'error' function on all other responses
					if (config.error && typeof(config.error) === 'function') {
						config.error(xmlhttp);
					}
				}
				// Always run 'complete' function
				if (config.complete && typeof(config.complete) === 'function') {
					config.complete(xmlhttp, xmlhttp.statusText);
				}
			}
		};
		// Run 'beforeSend' function before ajax submission
		if (config.beforeSend && typeof(config.beforeSend) === 'function') {
			config.beforeSend(xmlhttp);
		}
		// Data type set to expect DOM content
		if (config.dataType == 'html') {
			xmlhttp.responseType = "document";
		}
		// ajax request settings
		xmlhttp.open(config.method || 'GET', requestUrl, true);
		xmlhttp.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
		xmlhttp.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
		// Prevention of cache
		if (config.cache === false) {
			xmlhttp.setRequestHeader('Cache-Control', 'no-cache');
		}
		xmlhttp.send(config.data);
	}

	/** 
	 *	Functions applied directly to an HTML element. 
	 *	e.g. <div data-js="forms-example"> will load a javascript method of btf.forms.example(this)
	 *	Multiple functions can be called by splitting via the dataSplit character. e.g. <div data-js="forms-example toggle-auto">
	 *
	 *	@param {object} jsNode - Target node to be passed to the dynamic function.
	*/
	function loadFunction(jsNode) {
		var funcArray = jsNode.getAttribute(config.dataAttr),
			funcArray = funcArray.split(config.dataSplit),
			pathArray,
			func;

		for (var i = 0; i < funcArray.length; i++) {
			pathArray = funcArray[i].split(config.dataDelim);
			func = config.nameSpace;
			for (var j = 0; j < pathArray.length; j++) {
				if (func[pathArray[j]] !== undefined) {
					func = func[pathArray[j]];
				} else {
					break;
				}
			}
			if (typeof func === 'function') {
				func(jsNode);
			}
		}
	};
	
	/** 
	 *	Asynchronously load an external script file
	 * 
	 *	@param {string} url - Target URL to fetch script from
	 *	@param {string} id - Unique ID to prevent multiple script downloads, e.g. quick view products
	 *
	 *	@returns {boolean} - Return the script object
	 */  
	function loadScript (url, id) {
		var script = d.getElementById(id);
		if (script) {
			return script;
		}
		script = d.createElement('script');
		script.async = true;
		script.id = id;
		script.src = url;
		script.type = 'text/javascript';
		d.body.appendChild(script);
		return script;
	}

	/** 
	 *	Convert a serialized object into a string
	 *
	 *	@param {object} object - Target {name, value} object to be converted
	 *
	 *	@returns {object} - Parameters returned as URL string
	 */ 
	function objectToString(object) {
		var string = '',
			element;
		// Loop over object and append to string
		for (i = 0; i < object.length; i++) {
			string += object[i].name + '=' + object[i].value + '&';
		}
		return string;
	}

	/** 
	 *	Automatically run javascript based on DOM configuration. 
	 *	Run javascript files with a '[data-js]' value. 
	 *
	 *	@param {object} form - Target form to be serialized
	 *	@param {boolean} array - Enable results back in an array instead of as a string
	 */ 
	function pageLoad(wrapper) {
		wrapper = wrapper || d;
		var jsNodes = wrapper.querySelectorAll('[' + config.dataAttr + ']');
		if (jsNodes) {
			for (var i = 0; i < jsNodes.length; i++) {
				loadFunction(jsNodes[i]);
			};
		}
	}

	/** 
	 *	Scroll to a certain position on the page
	 * 
	 *	@param {string} to - Toggle visibility method, e.g. 'opacity', 'slide', 'class'
	 *	@param {integer} duration - Time (ms) taken to scroll to position
	 */ 
	function scrollTo(to, duration) {
	    var start = document.body.scrollTop,
			change = to - start,
			currentTime = 0,
			increment = 20;

		var animateScroll = function(){		
			currentTime += increment;
			var val = Math.easeInOutQuad(currentTime, start, change, duration);						
			document.body.scrollTop = val; 
			if(currentTime < duration) {
				setTimeout(animateScroll, increment);
			}
		};
		animateScroll();
	}

	/** 
	 *	Convert a form into a serialized object
	 *
	 *	@param {object} form - Target form to be serialized
	 *	@param {boolean} array - Enable results back in an array instead of as a string
	 *
	 *	@returns {object} - Parameters returned as URL string or as array of {name, value} objects
	 */ 
	function serialize(form, array) {
		var query = [],
			element;
		for (i = 0; i < form.elements.length; i++) {
			element = form.elements[i];
			// Exclude unchecked radio / checkbox types
			if (element.type.toLowerCase() === 'checkbox' || element.type.toLowerCase() === 'radio' && !element.checked) {
				continue;
			}
			// Push into an extensible array object (allows duplicate values)
			if (element.name && element.value && element.nodeName.toLowerCase() !== 'button') {
				query.push({name: element.name, value: element.value});
			}
		}
		return array ? query : objectToString(query);
	}
 
	return {
		ajax: ajax, loadFunction: loadFunction, loadScript: loadScript, pageLoad: pageLoad, scrollTo: scrollTo, serialize: serialize
	}	

})(document);