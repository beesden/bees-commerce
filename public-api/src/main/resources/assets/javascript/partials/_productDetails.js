var btf = btf || {};

btf.product = (function(d) {

	/** 
	 *	Fetch the recently viewed products
	 *  If scripts are already loaded, call the pre-loaded generator function
	 * 
	 *	@param {element} elWrapper - Related / recently viewed product wrapper
	 */ 
	function recentlyViewed(elWrapper) {
		var productId = elWrapper.getAttribute('data-product-id'),
			wrapper = d.createElement('div');
		// Get the recently viewed products and append them to the product
		btf.utils.ajax({
			data: 'ProductID=' + productId,
			url: '/pws/RecentlyViewed.ice',
			success: function(data) {
				elWrapper.insertAdjacentHTML('afterEnd', data);
			}
		})
	}

	/** 
	 *	Load social share plugins such as facebook, twitter, google+
	 *  If scripts are already loaded, call the pre-loaded generator function
	 * 
	 *	@param {element} elSocial - Social links container
	 */ 
	function socialLinks (elSocial) {
		var socialPlugins = [
				{
					id: 'share-FB',
					refresh: 'FB-XFBML-parse',
					url: '//connect.facebook.net/en_GB/all.js#xfbml=1'
				},
				{
					id: 'share-TW',
					refresh: 'twttr-widgets-load',
					url: '//platform.twitter.com/widgets.js'
				},
				{
					id: 'share-gp',
					refresh: 'gapi-plusone-go',
					url: '//apis.google.com/js/platform.js'
				},
				{
					id: 'parsePinBtns',
					refresh: 'window-parsePinBtns',
					url: '//assets.pinterest.com/js/pinit.js'
				}
			],
			plugin,
			script;
		return;
		// Loop over links object and add script / refresh scripts
		for (var i = 0; i < socialPlugins.length; i++) {
			plugin = socialPlugins[i];
			// Re-run an existing function
			if (d.getElementById(plugin.id)) {
				btf.utils.loadFunction(links.refresh);
			} 
			// Add a new script tag
			else {
				script = btf.utils.loadScript(plugin.url, plugin.id);	
				// Bit of a hack for pinterest
				script.setAttribute('data-pin-build', 'parsePinBtns');	
			}				
		}
		// The social features are hidden by default
		elSocial.classList.add('toggleShow');		
	}

	return {
		recent: recentlyViewed, social: socialLinks
	}

})(document);