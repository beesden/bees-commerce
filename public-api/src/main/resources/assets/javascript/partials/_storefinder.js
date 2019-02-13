var btf = btf || {};

btf.storefinder = (function(d) {

	var config = {
		overlayClass: 'overlay-loading',
		maxStores: 10
	};

	/** 
	 *	Link up the Google places search box to search via google maps.
	 * 	Normal form activity is blocked for google search inputs.
	 *	By default searches include both Places and Maps search. 
	 *	https://developers.google.com/maps/documentation/javascript/examples/places-searchbox
	 *	
	 *	@param {object} input - Input element used for autocompletion
	 */ 
	function autocomplete(input) {
		var searchBox = new google.maps.places.SearchBox(input);
		// Fetch stores from API on change
		google.maps.event.addListener(searchBox, 'places_changed', function() {			
			var place = searchBox.getPlaces()[0];
			if (place.geometry) {
				updateUrl({
					name: input.value,
					latitude:place.geometry.location.lat(), 
					longitude:place.geometry.location.lng(), 
					maxStores: config.maxStores
				});
			}
		})
		// Prevent form submittion on 'enter'
		google.maps.event.addDomListener(input, 'keydown', function(e) { 
			if (e.keyCode === 13) { 
				e.preventDefault(); 
			}
		}); ;
	}

	/** 
	 *	Build template object via HTML5 template API
	 *	
	 *	@param {object} template - Template element to be cloned
	 *	@param {object} store - Returned store result information to be added into the template
	 */ 
	function buildTemplate(template, store) {
		var addressParts = ['address1','address2','address3','city','postcode'],
			days = ['mon','tue','wed','thu','fri','sat','sun'],
			address = '';
		for (var part in addressParts) {
			part = addressParts[part];
			address += (address && store[part] ? ', ' : '') + (store[part] ? store[part] : '');
		}
		for (var day in days) {
			day = days[day];
			template.querySelector('.opening .' + day).innerHTML += store[day + 'OpenTimes'];
		}
		template.querySelector('article').id = 'store-' + store.id;
		template.querySelector('h1').innerHTML = store.nameTranslated;
		template.querySelector('.address').innerHTML = address;
		template.querySelector('.phone').innerHTML += store.phone;
		template.querySelector('.more a').href = '#store-info-' + store.id;
		template.querySelector('.details').id = 'store-info-' + store.id;
		return template;
	}

	/** 
	 *	Draws a map centered displaying a set of results, completely resetting the map view and remove any previous markers.
	 *	Also populates the store list with returned data.
	 *
	 *	@param {object} results - Store results array to be added to the map / list
	 */ 
	function drawMap(results) {
		var mapLayer = d.getElementById('store-map'),
			storeList = d.getElementById('store-list'),
			storeEmpty = d.getElementById('store-empty'),
			storeTemplate = d.getElementById('template-store'),
			bounds,
			map;
		// Show elements as necessary
		storeList.style.display = 'block';
		while (storeList.childNodes.length > 1) {
			storeList.removeChild(storeList.lastChild);
		}
		if (!results) {
			mapLayer.style.display = 'none';
			storeEmpty.style.display == 'block';
			return;
		}
		mapLayer.style.display = 'block';
		// Set up google maps variables
		map = new google.maps.Map(mapLayer);
		bounds = new google.maps.LatLngBounds();
		// Show current location if supplied
		if (config.location) {
			var marker = new google.maps.Marker({
				icon: {
					path: google.maps.SymbolPath.BACKWARD_CLOSED_ARROW,
					scale: 4
				},
				map: map,
				position: new google.maps.LatLng(config.location.latitude, config.location.longitude)
			});
		}
		// Add markers to the map and info to the list
		for (var i = 0; i < results.length; i++) {			
			var result = results[i],
				location = new google.maps.LatLng(result.latitude, result.longitude),
				marker;
			// Map will break if bound points not correctly mapped
			if (!result.latitude || !result.longitude) {
				continue;
			}
			bounds.extend(location);
			// Add store markers to the map
			marker = new google.maps.Marker({
				map: map,
				id: result.id,
				title: result.name,
				position: location
			});
			// Add store info via the template
			if (!storeTemplate) {
				continue
			}
			// Clone and populate the template element
			var template = document.importNode(storeTemplate.content, true);
			template = buildTemplate(template, result);
			storeList.appendChild(template);
		}
		// Update and scroll to map
 		map.fitBounds(bounds);
 		btf.utils.scrollTo(mapLayer.offsetTop - 24, 500)
	}

	/** 
	 *	Get the current location of the user from the HTML5 geolocation API
	 *	https://developer.mozilla.org/en-US/docs/Web/API/Geolocation/Using_geolocation
	 *
	 *	@param {element} elButton - Toggle button element for starting a geolocation search
	 */ 
	function geolocate(elButton) {
		// Require HTML5 geolocation
		if (!navigator.geolocation) {
			return
		}
		// Create error message element
		var countries = d.getElementById('store-countries'),
			errorMessage = d.createElement('span');
		errorMessage.className = 'store-error';
		// Replace storecountry links with geocode form
		elButton.parentNode.classList.add('toggleShow');
		if (countries) {
			countries.classList.add('toggleHide');
		}
		// Check for location on click
		elButton.onclick = function() {
			navigator.geolocation.getCurrentPosition(function(location) {				
				if (!location.coords) {
					errorMessage.innerHTML = elButton.getAttribute('data-error');
					elButton.parentNode.appendChild(errorMessage);
					return;
				}
				config.location = location.coords;
				updateUrl({
					latitude: config.location.latitude, 
					longitude: config.location.longitude, 
					maxStores: config.maxStores
				});
			});
		}
	}

	/** 
	 *	Callback function for Google Maps API initialization.
	 *	Loads autocomplete functionality and refreshes the page
	 */ 
	function googleConfig() {
		var searchBox = d.getElementById('geocode');	
		// Use the helpful google autocomplete
		if (searchBox) {
			autocomplete(searchBox);
		}
		// Get the results and map for the current page
		config.storefinder.classList.remove(config.overlayClass);
		// Implements history for browser back / forward buttons
		storeSearch(location.href);
		window.onpopstate = function(e) {
			storeSearch(location.href);
		};
	}

	/** 
	 *	Adds an overaly and calls Google Maps API to initate storefinder functionality
	 *
 	 *	@param {element} elStoreFinder - Storefinder wrapper element
	 */ 
	function setup(elStorefinder) {
		// Don't let it be used until google is ready
		config.storefinder = elStorefinder;
		elStorefinder.classList.add(config.overlayClass);
		// Fetch google maps to continue loading
		btf.utils.loadScript('//maps.googleapis.com/maps/api/js?v=3&libraries=places&callback=btf.storefinder.googleConfig');
	}

	/** 
	 *	Directly start a storefinder search request by URL
	 *	On success renders the results onto the map / store list
	 *
 	 *	@param {string} url - FCP storefinder search URL
	 */ 
	function storeSearch(url) {
		btf.utils.ajax({
			dataType: "json",
			url: url,
			beforeSend: function() {
				config.storefinder.classList.add(config.overlayClass);
				window.history.pushState(null, d.title, this.url);
			},
			error: function() {
				// No stores found!
			},
			success: function(data) {
				if (!data.stores) {
					return
				}		
				drawMap(data.stores);
			},
			complete: function() {
				config.storefinder.classList.remove(config.overlayClass);					
			}
		})
	}

	/** 
	 *	Sends a request to the storefinder API to find the closest stores.
	 *	On success, generate an interactive map and store list
	 *
	 *	@param {object} parameters - Search parameters to send to the API
	 */ 
	function updateUrl(parameters) {
		var url = '/pws/StoreFinder.ice',
			loop;
		parameters.findStore = true;
		for (var key in parameters) {
			url += (loop ? '&' : '?') + encodeURIComponent(key) + "=" + encodeURIComponent(parameters[key]);
			loop = true;
		}
		storeSearch(url);
	}

	return {
		setup: setup, geolocate: geolocate, googleConfig: googleConfig
	}

})(document);