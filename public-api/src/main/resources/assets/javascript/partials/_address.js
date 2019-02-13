var btf = btf || {};

btf.address = (function(d) {

	var config = {

	},
	message = d.createElement('div');
	

	/** 
	 *	Toggle different address inputs on country node value change. 
	 * 	E.g. US country shows state field, UK country shows postcode. 
	 *  The attribute [data-country-conceal] shows an element unless a specified country iso is selected. 
	 *  The attribute [data-country-reveal] hides an element unless a specified country iso is selected. 
	 *
	 *	@param {object} countrySelect - Country <select> element
	 *	@module jsLoad
	 */ 
	function countrySelect(countrySelect) {
		if (!countrySelect || countrySelect.nodeName !== 'SELECT') {
			return;
		}
		// Update current inputs and bind event
		countryFieldToggle(countrySelect);
		countrySelect.addEventListener('change', function() {
			countryFieldToggle(this);
		});
	}

	/** 
	 *	Toggle different address fields on country selection
	 *
	 *	@param {object} countryElement - Country <select> element to trigger input display changes
	 */ 
	function countryFieldToggle(countryElement) {
		var el,
			toggle;
		for (i = 0; i < countryElement.form.elements.length; i++) {
			el = countryElement.form.elements[i];
			// Conceal if ISO is matched
			toggle = el.getAttribute('data-country-conceal');
			if (toggle && toggle.indexOf(countryElement.value) < 0) {
				el.parentNode.style.removeProperty('display');
			} else if (toggle) {
				el.parentNode.style.display = 'none'
			}
			// Reveal if ISO is matched
			toggle = el.getAttribute('data-country-reveal');
			if (toggle && toggle.indexOf(countryElement.value) < 0) {
				el.parentNode.style.display = 'none'
			} else if (toggle) {
				el.parentNode.style.removeProperty('display');
			}
		}
	}

	/** 
	 *	Perform an AJAX search for a postcode lookup request
	 *	An error message is displayed if no results found
	 *	The address form will auto-populate on single result
	 *	A selection will be displayed on multiple results
	 *
	 *	@param {object} button - Original action button used to initiate the search
	 *	@param {boolean} search - Toggle whether or not we are searching for or selecting an address
	 *	@TODO Fix postcode search in form submissions
	 */ 
	function postcodeSearch(button, search) {
		var postcodeForm = button.form,
			postcodeData = btf.utils.serialize(postcodeForm, true);
		if (search) {
			postcodeData.push({name: 'isSearchPostcode', value: true});
		}
		message.className = 'validation';
		// TODO - add validation fr postcode to ensure filled in
		btf.utils.ajax({
			url: postcodeForm.action,
			data: postcodeData,
			success: function(data) {
				button.disabled = false;
				// Invalid results
				if (!data.addressIdList && !data.address) {
					message.innerHTML = button.getAttribute('data-error');
					button.parentNode.insertBefore(message, button);
				} 
				// Single result
				else if (!data.addressIdList) {
					if (message.parentNode) {
						message.parentNode.removeChild(message);
					}
					postcodeSelect(button, data.address, postcodeForm);
				} 
				// Multiple results
				else {
					if (message.parentNode) {
						message.parentNode.removeChild(message);	
					}
					postcodeFetch(button, data.addressIdList, postcodeForm);	
				}
			}
		});
	}

	/** 
	 *	Generate a list of fetched addresses to select from
	 *
	 *	@param {object} button - Original action button used to initiate the search
	 *	@param {object} data - Single returned postcode address result
	 *	@param {object} postcodeForm - Enclosing form
	 */ 
	function postcodeFetch(button, data, postcodeForm) {
		var postcodeTemplate = d.getElementById('template-address'),
			existingSelect = d.getElementById('postcodeAddressID'),
			postcodeForm,
			postcodeOptions
			option = d.createElement('option');
		// Stop if no template found
		if (!postcodeTemplate) {
			return;
		}
		// Prevent duplicate select boxes
		if (existingSelect) {
			existingSelect.parentNode.parentNode.removeChild(existingSelect.parentNode);
		}		
		// Get the template and update for JS
		postcodeTemplate = d.importNode(postcodeTemplate.content, true);
		postcodeOptions = postcodeTemplate.querySelector('select');
		postcodeOptions.size = 5;
		// Append the addresses into the selection
		for (var id in data) {
			option = option.cloneNode();
			option.innerHTML = data[id].itemText;
			option.value = data[id].itemValue;
			postcodeOptions.appendChild(option);
		}
		button.parentNode.insertBefore(postcodeTemplate, button.nextSibling);
		// Fetch full postcode on select
		postcodeOptions.onchange = function() {
			postcodeSearch(button);		
		}
		// TODO - Scroll into view
		// btf.toggle.scrollTo(button.offsetTop, 200);
	}

	/** 
	 *	Postcode lookup sends a request to the application to return postcode results for a particular search. 
	 * 	Capture the postcode submit button event and submit the request over AJAX instead. 
	 *
	 *	@param {object} postcodeButton - Postcode submit <button> element
	 *	@module jsLoad
	 */ 
	function postcodeLookup(postcodeButton) {
		if (!postcodeButton || postcodeButton.nodeName !== 'BUTTON') {
			return;
		}
		var postcodeToggle = d.querySelector('.postcode-toggle'),
			sibling;
		// Hide subsequent address form fields
		if (postcodeButton.getAttribute('data-hide-addresss') == 'true') {
			sibling = postcodeButton.nextSibling;
			while (sibling) {
				if (sibling.nodeType === 1 && !sibling.classList.contains('postcode-toggle')) {
					sibling.style.display = 'none';
				}
				sibling = sibling.nextSibling;
			}
		} else {
			postcodeToggle.style.display = 'none';
		}
		// Fetch results on submit
		postcodeToggle.onclick = function(e) {
			e.preventDefault();
			this.style.display = 'none';	
			sibling = this.nextSibling;			
			while (sibling) {
				if (sibling.nodeType === 1) {
					btf.toggle.toggle(sibling, 'fade');
				}
				sibling = sibling.nextSibling;
			}
		};
		// Fetch results on submit
		postcodeButton.onclick = function(e) {
			e.preventDefault();
			this.disabled = 'disabled';
			postcodeSearch(postcodeButton, true);
		};
	}

	/** 
	 *	Populate the address form with the postcode results
	 *	This assumed the address form is adjacent to the postcode search button
	 *
	 *	@param {object} button - Original action button used to initiate the search
	 *	@param {object} data - Single returned postcode address result
	 *	@param {object} postcodeForm - Enclosing form
	 */ 
	function postcodeSelect(button, data, postcodeForm) {
		// Reveal subsequent address form fields
		sibling = button.nextSibling;
		while (sibling) {
			if (sibling.nodeType === 1) {
				sibling.style.removeProperty('display');
			}
			sibling = sibling.nextSibling;
		}
		// Populate fields
		for (field in data) {
			postcodeForm[field].value = data[field];
		}
		// TODO - Scroll into view
		// btf.toggle.scrollTo(button.offsetTop, 200);
	}
 
	return {
		countrySelect: countrySelect, postcodeLookup: postcodeLookup
	}	

})(document);