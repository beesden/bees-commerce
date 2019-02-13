var btf = btf || {};

btf.toggle = (function(d) {

	var config = {
		toggleSpeed: 400,
		toggleClass: 'toggleActive',
		toggleFunctions: {
			class: function(el, options) {
				// N.b. classname toggle is run by all other toggle functions
				el.classList.toggle(options.toggleClass);
				el.loading = false;
				// Run callback function
				if (typeof options.callback == 'function') {
					options.callback(el);
				}
			},
			fade: function(el, options) {
				var show = el.offsetParent !== null;
				// Reset the element with default CSS
				el.style.display = 'block';
				el.style.opacity = show ? 1 : 0;
				// Ensure animiation with short delay
				setTimeout(function () {
					el.style.transition = options.toggleSpeed + 'ms opacity ease-in-out';	
					el.style.opacity = show ? 0 : 1;
				}, 10);
				// Stop loading on complete and hide if transparent
				el.addEventListener('transitionend', function() {
					if (!el.loading) {
						return;
					}					
					options.toggleFunctions.class(el, options);
					el.style.removeProperty('transition');
					if (el.style.opacity == 0) {
						el.style.display = 'none';			
					}
				})		
			},
			slide: function(el, options) {
				var show = el.offsetParent !== null,
					props,
					maxHeight;
				// Reset the element with default CSS
				el.style.display = 'block';
				el.style.overflow = 'hidden';
				maxHeight = el.clientHeight;
				el.style.height = show ? maxHeight + 'px' : 0;
				if (!show) {
					el.style.marginTop = el.style.marginBottom = el.style.paddingTop = el.style.paddingBottom = 0;
				}
				// Ensure animiation with short delay
				setTimeout(function () {
					el.style.transition = options.toggleSpeed + 'ms all ease-in-out';
					if (show) {
						el.style.marginTop = el.style.marginBottom = el.style.paddingTop = el.style.paddingBottom = 0;
					} else {				
						props = ['padding-top', 'padding-bottom', 'margin-top', 'margin-bottom']
						for (var i = 0; i < props.length; i++) {
							el.style.removeProperty(props[i]);				
						}
					}
					el.style.height = show ? 0 : maxHeight + 'px';
				}, 10);
				// Stop loading on complete and hide if transparent
				el.addEventListener('transitionend', function() {
					if (!el.loading) {
						return;
					}
					options.toggleFunctions.class(el, options);
					if (el.clientHeight == 0) {
						el.style.display = 'none';			
					}
					props = ['height', 'padding-top', 'padding-bottom', 'margin-top', 'margin-bottom', 'overflow', 'transition']
					for (var i = 0; i < props.length; i++) {
						el.style.removeProperty(props[i]);				
					}
				})						
			},
			toggle: function(el) {
				el.style.display = el.offsetParent ? 'none' : 'block';
				options.toggleFunctions.class(el, options);
			}
		}
	}

	/**  
	 *	Scans the HTML for specific data- objects and converts these to toggle elements
	 *	Uses the 'data-toggle' attribute to determine toggle type
	 *	Toggles elements in the same 'data-toggle-group', e.g. for tabs
	 */ 
	function auto() {
		var toggleLinks = d.querySelectorAll('[data-toggle]'),
			toggleGroup,
			toggleValid,
			toggleItem;

		// Delegate the click event from the document 
		d.addEventListener('click', function(e) {
			// Get the click target and check parents
			target = e.target || e.srcElement;
			while (target.parentNode !== d) {
				toggleValid = target.getAttribute('data-toggle');
				if (toggleValid) {
					break;
				}
				target = target.parentNode;
			}
			if (!toggleValid) {
				return;
			}
			e.preventDefault();
			toggleGroup = target.getAttribute('data-toggle-group');				
			if (toggleGroup) {					
				toggleGroup = d.querySelectorAll('[data-toggle-group="' + toggleGroup + '"]');
				for (var i = 0; i < toggleGroup.length; i++) {
					toggleItem = toggleGroup[i];
					if (toggleItem !== target && toggleItem.classList.contains(config.toggleClass)) {
						dataToggle(toggleItem);
					}
				}
			}
			// Toggle selected element
			dataToggle(target);
		});
	}

	/** 
	 *	Executes an elemental toggle method based on assigned data attributes
	 *
	 *	@param {object} el - Element to be toggled
	 */ 
	function dataToggle(el) {
		var target = d.querySelector(el.hash);
		// Prevent multiple clicks
		if (!target || target.loading) {
			return;
		}
		// Otherwise run function
		btf.toggle.toggle(target, el.getAttribute('data-toggle'));	
	}

	/** 
	 *	Parent method for various toggle functionality, configurable via the options attribute.
	 * 
	 *	@param {object} el - Element target to toggle visibility of
	 *	@param {string} type - Toggle visibility method, e.g. 'opacity', 'slide', 'class'
	 *	@param {object} options - Override default config options
	 */ 
	function toggle(el, type, options) {
		// Prevent double clicks
		if (el.loading) {
			return;
		}
		el.loading = true;
		// Extend default config
		options = options || {};
		for (var key in config) {
			if (!options[key]) {
				options[key] = config[key];
			}
		}
		// Call function from config
		type = typeof config.toggleFunctions[type] === 'function' ? type : 'toggle';
		config.toggleFunctions[type](el, options);
	}
 
	return {
		auto: auto, toggle:toggle

	}	

})(document);