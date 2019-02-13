var btf = btf || {};

btf.search = (function (d) {
	var config = {
		overlayClass: 'overlay-loading',
		callbacks: {}
	};

	/** 
	 *	Submit a request to the FCP Search service over AJAX. 
	 * 	Adds an overlay whilst request is processing. 
	 *
	 *	@param {string} url - Target pre-built URL for submission
	 */ 
	function updateContent(url) {
		// Prevent double click
		if (!config.wrapper || config.wrapper.loading) {
			return;
		}
		var link = d.createElement('a');
		link.href = url;		
		// Update the URL bar
		window.history.pushState(null, d.title, url);
		// Send the AJAX request and update the search page
		btf.utils.ajax({
			url: url,
			dataType: 'html',
			beforeSend: function () {
				config.wrapper.classList.add(config.overlayClass);
				config.wrapper.loading = true;
			},
			success: function (data) {
				data = data.getElementById(config.wrapper.id);
				if (!data) {
					return;
				}
				config.wrapper.innerHTML = data.innerHTML;
			},
			complete: function() {
				config.wrapper.classList.remove(config.overlayClass);
				config.wrapper.loading = false;
			}
		});
	};
		
	return {

		/** 
		 *	Configure filters to update over AJAX to improve page performance. 
		 * 	Submits over AJAX for forms (select / input) changes and for links with '.btf-filter'.
		 *
		 *	@param {object} wrapper - Parent wrapper object for delegating filter requests
		 *	@module jsLoad
		 */ 
		filters : function (wrapper) {
			var target,
				selector,
				url;
			config.wrapper = wrapper;
			// Set up links as ajax filters
			config.wrapper.onclick = function(e) {
				target = e.target || e.srcElement;
				if (!target.classList.contains('btf-filter')) {
					return;
				}
				updateContent(target.href);
				return false;
			};
			// Set up sort order and price slider as ajax filters
			config.wrapper.onchange = function(e) {
				target = e.target || e.srcElement;
				if (target.tagName.toLowerCase() !== 'select' && target.tagName.toLowerCase() !== 'input') {
					return;
				}
				url = target.form.action + (target.form.action.indexOf('?') > -1 ? '&' : '?') + btf.utils.serialize(target.form);
				updateContent(url);
			};
			// Implements history for browser back / forward buttons
			window.onpopstate = function(e) {
				updateContent(location.href);
			};
		}
	};

}(document));