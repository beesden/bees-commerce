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
			postcodeData.push({
				name: 'isSearchPostcode',
				value: true
			});
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
		countrySelect: countrySelect,
		postcodeLookup: postcodeLookup
	}

})(document);;


var btf = btf || {};

btf.basket = (function(d) {

	var config = {
		overlayClass: 'overlay-loading',
		updateUrl: '/pws/AJUpdateBasket.ice'
	}

	/** 
	 *	Submit form over AJAX on submission and update multiple basket types.
	 * 	Bind event to the button instead of the form, so that we can capture different form actions (e.g. update, add, remove).
	 *
	 *	@param {object} elQuantity - Quantity input element
	 */
	function form(elButton) {
		var message = d.createElement('span'),
			basketForm,
			basketData,
			updateType;
		message.className = 'basket-message-update';

		elButton.addEventListener('click', function(e) {
			basketForm = this.form;
			if (!basketForm) {
				return;
			}
			updateType = basketForm.getAttribute('data-update')
			e.preventDefault();
			// check for validation and double submission
			if (basketForm.loading || !btf.validate.form(basketForm)) {
				return;
			}
			// Assemble the form data
			basketData = btf.utils.serialize(basketForm, true);
			if (this.name && this.value) {
				basketData.push({
					name: this.name,
					value: this.value
				});
			}
			btf.utils.ajax({
				data: basketData,
				dataType: 'json',
				url: config.updateUrl,
				beforeSend: function() {
					// Prevent subsequent submits and add overlay
					basketForm.loading = true;
					basketForm.classList.add(config.overlayClass);
				},
				success: function(data) {
					// Add a success message after the form
					if (updateType === 'message' && data.message) {
						basketForm.appendChild(message);
						message.innerHTML = data.message;
						if (data.quantityApplied) {
							message.innerHTML += ' ' + data.messageLink;
						}
					}
					// Refresh the entire page from the first parent container
					else {
						var container = basketForm;
						do {
							container = container.parentNode;
						} while (!container.classList.contains('container'));
						container.innerHTML = data;
						btf.utils.pageLoad(container);
					}
				},
				complete: function() {
					// Enable subsequent submits and remove overlay
					basketForm.loading = false;
					basketForm.classList.remove(config.overlayClass);
				}
			})

		});
	}

	/** 
	 *	Bind multiple UX functions to the quantity input.
	 * 	Display as a dropdown for small values, and hide update buttons until required.
	 *
	 *	@param {object} elQuantity - Quantity input element
	 */
	function quantity(elQuantity) {
		// Toggle update button on quantity change
		var update = elQuantity.form.querySelector('.basket-update-button'),
			numberSelect,
			hideButton;
		// Use select element for small values		
		if (btf.forms.numberSelect) {
			btf.forms.numberSelect(elQuantity);
		}
		// Submit button is only shown if value is different to original
		toggleButton(elQuantity, update)
	}

	/** 
	 *	Link a form button to display only when the input displays a different value than it's default.
	 *
	 *	@param {element} input - Input element which requires a different value
	 *	@param {element} button - Button to be shown / hidden
	 */
	function toggleButton(input, button) {
		var toggle;
		if (!input || !button) {
			return;
		}
		// Hide update button by default
		input.baseValue = input.value;
		button.style.display = 'none';
		// Reveal update button if current value != default value
		input.onchange = function() {
			if (button.loading) {
				return false;
			}
			toggle = input.baseValue === input.value;
			if (!toggle === !button.offsetParent) {
				input.readonly = true;
				btf.toggle.toggle(button, 'fade');
			}
		}
	}

	return {
		form: form,
		quantity: quantity
	}

})(document);;


var btf = btf || {};

btf.forms = (function(d) {

	var config = {
		inputQuant: 10,
		overlayClass: 'overlay-loading'
	};

	/** 
	 *	Toggle between new / saved address forms on a checkout address.
	 * 	E.g. US country shows state field, UK country shows postcode.
	 *  The attribute [data-country-conceal] shows an element unless a specified country iso is selected.
	 *  The attribute [data-country-reveal] hides an element unless a specified country iso is selected.
	 *
	 *	@param {object} addressToggle - Address type <input> element
	 *	@module jsLoad
	 */
	function checkoutAddressToggle(addressToggle) {
		if (!addressToggle || addressToggle.nodeName !== 'INPUT') {
			return;
		}
		// Update on change
		addressToggle.onchange = function() {
			// Show target element
			var addressForms = d.querySelectorAll('input[name=' + this.name + ']'),
				showAddress = d.getElementById(this.value),
				hideAddress;
			// Show the selected address type
			if (!showAddress.offsetParent) {
				btf.toggle.toggle(showAddress, 'slide');
			}
			// Hide other address types
			for (var i = 0; i < addressForms.length; i++) {
				hideAddress = d.getElementById(addressForms[i].value);
				if (hideAddress !== showAddress) {
					btf.toggle.toggle(hideAddress, 'slide');
				};
			}
		};
	}

	/** 
	 *	Gift card balance checker fetches the form repsonse over AJAX and displays the current avaialble credit.
	 *
	 *	@param {string} cardNumber - ID of card number input used to select the gift card balance form
	 */
	function giftCardCheck(giftCardForm) {
		giftCardForm = giftCardForm.form;
		if (!giftCardForm) {
			return;
		}
		// Check gift card balance on submission
		giftCardForm.onsubmit = function(e) {
			// is it a payment form? Go ahead!
			if (giftCardForm.paymentMode) {
				return;
			}
			e.preventDefault();
			// Validate the form before continuing
			if (!btf.validate.form(this)) {
				return;
			}
			// Get the form data and add ajax parameter
			var formFields = btf.utils.serialize(this, true);
			formFields.push({
					name: 'ajax',
					value: true
				})
				// Submit AJAX request and update form
			btf.utils.ajax({
				data: formFields,
				url: this.action,
				beforeSend: function() {
					giftCardForm.classList.add(config.overlayClass);
				},
				success: function(data) {
					giftCardForm.innerHTML = data;
					// Reload Captcha	
					if (typeof(Recaptcha) !== 'undefined' && typeof(RecaptchaOptions) !== 'undefined') {
						Recaptcha.create(RecaptchaOptions.publicKey);
					}
				},
				complete: function() {
					giftCardForm.classList.remove(config.overlayClass);
				}
			});
		}
	}

	/** 
	 *	Show a select for small values but allow text input for larger values(e.g. > 10).
	 * 	Therefore hide the quantity input and build a second select element that updates the original on change.
	 * 	N.B. the select element will not display if the number element is reduced past the breakpoint until refresh.
	 *
	 *	@param {object} elQuant - Numeric form input element
	 */
	function numberSelect(elQuant) {
		var select = d.createElement('select'),
			option = d.createElement('option');
		// Value is already above the max, so we don't style
		if (elQuant.value >= config.inputQuant) {
			return;
		}
		// Keep the style consistent and hide the old value
		elQuant.parentNode.appendChild(select);
		select.className = elQuant.className;
		elQuant.style.display = 'none';
		// Normal selectable options
		for (var i = 1; i <= config.inputQuant; i++) {
			select.appendChild(option)
			option.selected = (parseInt(elQuant.value) === i);
			option.value = i;
			option.innerHTML = i + (i == config.inputQuant ? '+' : '');
			option = option.cloneNode();
		}
		// On change, update the quantity
		select.onchange = function() {
			elQuant.value = select.value;
			if (elQuant.onchange) {
				elQuant.onchange();
			}
			// If we choose the max, hide the select and show the number
			if (parseInt(elQuant.value) === config.inputQuant) {
				select.style.display = 'none'
				elQuant.style.removeProperty('display')
				elQuant.focus();
			}
		}
	}

	/** 
	 *	Validate a form using btf.validate functionality, and supply success / failure functionality
	 *	Bind validation to the blur and submit events
	 *
	 *	@param {object} elForm - Form element to be validated
	 */
	function validate(elForm) {
		var formErrors;
		if (!elForm || elForm.nodeName.toLowerCase() !== 'form' || !btf.validate) {
			return;
		}
		// Prevent default HTML5 validation
		elForm.setAttribute('novalidate', 'novalidate');

		// Configure validation options
		var options = {
			onSuccess: function(el) {
				// Remove any error object
				el.classList.add('form-success');
				el.classList.remove('form-error');
				if (el.errorMessage && el.errorMessage.parentNode == el.parentNode) {
					el.parentNode.removeChild(el.errorMessage);
				}
			},
			onError: function(el, error) {
				// Add a single message object
				el.errorMessage = el.errorMessage || d.createElement('label');
				el.classList.add('form-error');
				el.classList.remove('form-success');
				el.parentNode.appendChild(el.errorMessage);
				// Update the message object if necessary
				el.errorMessage.className = 'validation error';
				el.errorMessage.innerHTML = error.message;
				el.errorMessage.setAttribute('for', el.id);
			}
		}
		elForm.options = options;

		elForm.addEventListener('submit', function(e) {
			// If no errors, submit the form (simples!)
			if (!btf.validate.form(this)) {
				e.preventDefault();
			}
		})

		// Run validation on blur
		for (var i = 0; i < elForm.elements.length; i++) {
			elForm.elements[i].addEventListener('blur', function() {
				btf.validate.element(this)
			});
		}
	}

	return {
		checkoutAddressToggle: checkoutAddressToggle,
		giftCardCheck: giftCardCheck,
		numberSelect: numberSelect,
		validate: validate
	}

})(document);;


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
	function socialLinks(elSocial) {
		var socialPlugins = [{
				id: 'share-FB',
				refresh: 'FB-XFBML-parse',
				url: '//connect.facebook.net/en_GB/all.js#xfbml=1'
			}, {
				id: 'share-TW',
				refresh: 'twttr-widgets-load',
				url: '//platform.twitter.com/widgets.js'
			}, {
				id: 'share-gp',
				refresh: 'gapi-plusone-go',
				url: '//apis.google.com/js/platform.js'
			}, {
				id: 'parsePinBtns',
				refresh: 'window-parsePinBtns',
				url: '//assets.pinterest.com/js/pinit.js'
			}],
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
		recent: recentlyViewed,
		social: socialLinks
	}

})(document);;


var btf = btf || {};

btf.productImages = (function(d) {

	var config = {
		altImageMax: 5,
		zoomClass: 'product-zoom'
	};

	/** 
	 *	Alternate images are added to show differnet photo variants for a given product.
	 *	A HEAD request is used to check the images exist, and if they do they are inserted onto the page.
	 *	A click event is added which exchanges the alternate image with the main image.
	 *	The Do-While loop ensures at least one image is always shown.
	 *
	 *	@param {element} container - Image element container to add new alternate images into
	 */
	function altImages(container) {
		var productImage = d.querySelector('.product__large img'),
			imageSource = productImage.src,
			imageAlternate = imageSource,
			imageCurrent,
			altImage,
			i = 1;
		do {
			altImage = addImage(container, imageAlternate)
			imageAlternate = imageSource.replace('.jpg', '_' + i + '.jpg')
				// Select the first image
			if (!imageCurrent) {
				imageCurrent = altImage;
				imageCurrent.classList.add('selected');
			}
			// Bind alternate image functionality to main image
			altImage.addEventListener('click', function(e) {
				e.preventDefault();
				if (this == imageCurrent) {
					return;
				}
				// Apply fade animation to product image
				productImage.style.opacity = 0.3;
				productImage.onload = function() { // TODO - test w/ caching
						this.style.opacity = 1;
					}
					// Update currently highlighed altimage			
				imageCurrent.classList.remove('selected');
				imageCurrent = this;
				imageCurrent.classList.add('selected');
				// Update the main product image
				productImage.src = this.variants.medium;
				productImage.parentNode.href = this.variants.zoom;
				zoom(productImage.parentNode);
			});
		} while (++i <= config.altImageMax && checkFileStatus(imageAlternate));
	}

	/** 
	 *	Build an altImage element using the medium URL
	 *
	 *	@param {element} container - Image element container to add new alternate images into
	 *	@param {string} imageSource - Base medium image URL used for generating different sizes
	 */

	function addImage(container, imageSource) {
		var altImageWrap = d.createElement('li'),
			altImage = d.createElement('img'),
			variants = {};
		// Configure variant sizes
		variants.medium = imageSource;
		variants.small = variants.medium.replace('/440/', '/80/');
		variants.zoom = variants.medium.replace('/440/', '/1200/');
		// Set attributes for image changes
		altImage.src = variants.small;
		altImage.variants = variants;
		// Append containers to the list
		container.appendChild(altImageWrap);
		altImageWrap.appendChild(altImage);
		return altImage;
	}

	/** 
	 *	Use a HEAD request to check the availability of an alternate image on the server
	 *
	 *	@param {string} url - URL of the base image url to check
	 */
	function checkFileStatus(url) {
		btf.utils.ajax({
			url: url,
			type: 'HEAD',
			complete: function(xhr) {
				return xhr.status == 200
			}
		})
	}

	/** 
	 *	Calculate the current position of the zoom image, based on event / window position.
	 *
	 *	@param {object} productImage - Product image wrapper
	 *	@param {object} zoomImage - Large zoom Image element
	 *	@param {object} event - Event element used for calulating position
	 */
	function moveImage(productImage, zoomImage, event) {
		var topRatio = -(zoomImage.offsetHeight - productImage.offsetHeight) / productImage.offsetHeight,
			leftRatio = -(zoomImage.offsetWidth - productImage.offsetWidth) / productImage.offsetWidth;
		zoomImage.style.left = (event.clientX - productImage.getBoundingClientRect().left) * leftRatio + 'px';
		zoomImage.style.top = (event.clientY - productImage.getBoundingClientRect().top) * topRatio + 'px';
	}

	/** 
	 *	Zoom image functionality for the product details page.
	 *	Constructs a pannable zoom element within the target anchor.
	 *	Zoom image displayed on click / hover, and moves on mouse / touch movement.
	 *
	 *	@param {object} productImage - Target <a> element used as the zoom wrapper
	 */
	function zoom(imageLink) {
		var zoomImage = d.getElementsByClassName(config.zoomClass)[0] || d.createElement('img'),
			productImage = imageLink.querySelector('img');
		// Prevent click regardless
		imageLink.onclick = function(e) {
				e.preventDefault();
				zoomImage.style.opacity = zoomImage.style.opacity == 1 ? 0 : 1;
			}
			// Prevent broken zoom images
		if (!checkFileStatus(imageLink.href)) {
			return;
		}
		// Set up the zoom image element
		zoomImage.className = config.zoomClass;
		zoomImage.src = imageLink.href;
		zoomImage.onload = function() {
			imageLink.appendChild(zoomImage);
			// Show zoom image on hover
			imageLink.onmouseover = function() {
					zoomImage.style.opacity = 1;
				}
				// Hide zoom image on mouseout
			imageLink.onmouseout = function() {
					zoomImage.style.opacity = 0;
				}
				// Move zoom image with mouse
			imageLink.onmousemove = function(e) {
					moveImage(imageLink, zoomImage, e);
				}
				// Move zoom image with touch
			imageLink.ontouchmove = function(e) {
				moveImage(imageLink, zoomImage, e);
			}
		}
	}

	return {
		altImages: altImages,
		zoom: zoom
	}

})(document);;


var btf = btf || {};

btf.search = (function(d) {
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
			beforeSend: function() {
				config.wrapper.classList.add(config.overlayClass);
				config.wrapper.loading = true;
			},
			success: function(data) {
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
		filters: function(wrapper) {
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

}(document));;


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
						latitude: place.geometry.location.lat(),
						longitude: place.geometry.location.lng(),
						maxStores: config.maxStores
					});
				}
			})
			// Prevent form submittion on 'enter'
		google.maps.event.addDomListener(input, 'keydown', function(e) {
			if (e.keyCode === 13) {
				e.preventDefault();
			}
		});;
	}

	/** 
	 *	Build template object via HTML5 template API
	 *
	 *	@param {object} template - Template element to be cloned
	 *	@param {object} store - Returned store result information to be added into the template
	 */
	function buildTemplate(template, store) {
		var addressParts = ['address1', 'address2', 'address3', 'city', 'postcode'],
			days = ['mon', 'tue', 'wed', 'thu', 'fri', 'sat', 'sun'],
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
		setup: setup,
		geolocate: geolocate,
		googleConfig: googleConfig
	}

})(document);;


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
		xmlhttp.onreadystatechange = function() {
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
						} catch (err) {
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
	function loadScript(url, id) {
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

		var animateScroll = function() {
			currentTime += increment;
			var val = Math.easeInOutQuad(currentTime, start, change, duration);
			document.body.scrollTop = val;
			if (currentTime < duration) {
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
				query.push({
					name: element.name,
					value: element.value
				});
			}
		}
		return array ? query : objectToString(query);
	}

	return {
		ajax: ajax,
		loadFunction: loadFunction,
		loadScript: loadScript,
		pageLoad: pageLoad,
		scrollTo: scrollTo,
		serialize: serialize
	}

})(document);;


var btf = btf || {};

btf.validate = (function(d) {

	var config = {
			messagePrefix: 'data-msg-',
			rulePrefix: 'data-rule-'
		},
		methods = {

			alphanumeric: function(el) {
				var regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).+$/;
				console.log(el.value)
				return !el.value || regex.test(el.value);
			},

			creditcard: function(el) {
				if (!el.value) {
					return true
				}
				if (/[^0-9 \-]+/.test(el.value)) {
					return false;
				}
				var nCheck = 0,
					nDigit = 0,
					bEven = false,
					value = el.value.replace(/\D/g, ""),
					cDigit,
					n;
				if (value.length < 13 || value.length > 19) {
					return false;
				}
				for (n = value.length - 1; n >= 0; n--) {
					cDigit = value.charAt(n);
					nDigit = parseInt(cDigit, 10);
					if (bEven) {
						if ((nDigit *= 2) > 9) {
							nDigit -= 9;
						}
					}
					nCheck += nDigit;
					bEven = !bEven;
				}
				return (nCheck % 10) === 0;
			},

			email: function(el) {
				var regex = /^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,4})$/;
				return !el.value || regex.test(el.value);
			},

			equalto: function(el) {
				var target = el.getAttribute('data-rule-equalto');
				target = d.getElementById(target);
				return el.value == target.value;
			},

			fromgroup: function(el) {
				var group = el.getAttribute('data-rule-fromgroup'),
					groupElements = el.form.querySelectorAll('[data-rule-fromgroup=' + group + ']');
				for (var i = 0; i < groupElements.length; i++) {
					testElement = groupElements[i]
					if (groupElements[i].type.toLowerCase() === 'checkbox' || groupElements[i].type.toLowerCase() === 'radio' ? groupElements[i].checked : groupElements[i].value) {
						return true;
					}
				}
				return false;
			},

			length: function(el) {
				var test = parseInt(el.getAttribute('data-rule-length'));
				return test === 'NaN' || el.value.length === test;
			},

			maxlength: function(el) {
				var test = parseInt(el.getAttribute('data-rule-maxlength'));
				return test === 'NaN' || el.value.length <= test;
			},

			minlength: function(el) {
				var test = parseInt(el.getAttribute('data-rule-minlength'));
				return test === 'NaN' || !el.value || el.value.length >= test;
			},

			max: function(el) {
				var value = parseInt(el.value),
					test = parseInt(el.getAttribute('data-rule-max'));
				return test === 'NaN' || !el.value || (value !== 'NaN' && value <= test);
			},

			min: function(el) {
				var value = parseInt(el.value),
					test = parseInt(el.getAttribute('data-rule-min'));
				return test === 'NaN' || !el.value || (value !== 'NaN' && value >= test);
			},

			notequalto: function(el) {
				var test = el.getAttribute('data-rule-notequalto');
				test = d.getElementById(test);
				return !test || el.value != test.value || !test.value;
			},

			number: function(el) {
				var regex = /^[\d]*$/;
				return regex.test(el.value);
			},

			numeric: function(el) {
				var regex = /^[\d ]*?$/;
				return regex.test(el.value);
			},

			postcode: function(el) {
				var country = d.getElementById('country'),
					postcodeRegex = {
						"US": /(^\d{5}$)|(^\d{5}-\d{4}$)/,
						"CA": /^([ABCEGHJKLMNPRSTVXY]\d[ABCEGHJKLMNPRSTVWXYZ])\ {0,1}(\d[ABCEGHJKLMNPRSTVWXYZ]\d)$/,
						"GB": /^(GIR|[A-Z]\d[A-Z\d]??|[A-Z]{2}\d[A-Z\d]??)[ ]??(\d[A-Z]{2})$/,
						"DE": /\b((?:0[1-46-9]\d{3})|(?:[1-357-9]\d{4})|(?:[4][0-24-9]\d{3})|(?:[6][013-9]\d{3}))\b/,
						"FR": /^(F-)?((2[A|B])|[0-9]{2})[0-9]{3}$/,
						"IT": /^(V-|I-)?[0-9]{5}$/,
						"AU": /^(0[289][0-9]{2})|([1345689][0-9]{3})|(2[0-8][0-9]{2})|(290[0-9])|(291[0-4])|(7[0-4][0-9]{2})|(7[8-9][0-9]{2})$/,
						"NL": /^[1-9][0-9]{3}\s?([a-zA-Z]{2})?$/,
						"ES": /^([1-9]{2}|[0-9][1-9]|[1-9][0-9])[0-9]{3}$/,
						"DK": /^([D-d][K-k])?( |-)?[1-9]{1}[0-9]{3}$/,
						"SE": /^(s-|S-){0,1}[0-9]{3}\s?[0-9]{2}$/,
						"BE": /^[1-9]{1}[0-9]{3}$/
					};
				country = country ? country.value : 'GB';
				el.value = el.value.toUpperCase();
				return !el.value || postcodeRegex[country].test(el.value);
			},

			required: function(el) {
				if (el.type.toLowerCase() === 'checkbox' || el.type.toLowerCase() === 'radio') {
					return el.checked;
				}
				return el.value.trim().length > 0;
			},

			specialcharacters: function(el) {
				var regex = /^\w\d*$/;
				return !regex.test(el.value);
			},

			tel: function(el) {
				var regex = /^(\+\d{1,2})?[\s\-]?[\(]?[0-9]{2,5}[\)]?[\s\-]?\d{3,4}[\s\-]?\d{3,5}$/;
				return regex.test(el.value);
			},

			uniquecharacters: function(el) {
				var test = parseInt(el.getAttribute('data-rule-uniquecharacters')),
					letters = {},
					letter;
				if (!el.value) {
					return true;
				}
				for (var i = 0; i < el.value.length; i++) {
					var letter = el.value.charAt(i)
					letters[letter] = (isNaN(letters[letter]) ? 1 : letters[letter] + 1);
				}
				return test === 'NaN' || Object.keys(letters).length >= test
			}
		}

	/** 
	 *	Validate a specific element by the rules define by the data- attributes.
	 * 	Loops through every data attribute and checks if there is a rule specified in config.methods.
	 *	If an error is found, an error object is added to the returned array.
	 *
	 *	@param {object} elQuantity - Quantity input element
	 *	@param {object} options - Form options for success / error tests etc
	 *
	 *	@returns {object} - Array of errors to display
	 */
	function validateElement(input, options) {
		var inputErrors = [],
			attribute,
			attrName;
		// Loop over configured attributes
		for (var i = 0; i < input.attributes.length; i++) {
			attribute = input.attributes[i];
			// Only test attributes with configured prefix
			if (attribute.nodeName.substring(0, config.rulePrefix.length) !== config.rulePrefix) {
				continue;
			}
			// Test the attribute and append each error to the errors array
			attrName = attribute.nodeName.replace(config.rulePrefix, '');
			if (!validateRule(input, attrName)) {
				inputErrors.push({
					element: input,
					message: input.getAttribute(config.messagePrefix + attrName),
					property: attrName
				})
			};
		}
		// If no options defined, check the form
		options = options || input.form.options || {};
		// Run error function if error messages
		if (inputErrors.length && typeof options.onError === 'function') {
			options.onError(input, inputErrors[0]);
		}
		// Run success function if no error messages
		else if (!inputErrors.length && typeof options.onSuccess === 'function') {
			options.onSuccess(input);
		}
		return inputErrors;
	}

	/** 
	 *	Loop through all the elements of a given form and test each of these by the rules defined.
	 *	Constructs an object of errors which can be used for debugging at a later stage.
	 *
	 *	@param {object} elQuantity - Quantity input element
	 *	@param {object} options - Form options for success / error tests
	 *
	 *	@returns {boolean} - Returns false if there are input errors
	 */
	function validateForm(form, options) {
		var formElement,
			formErrors = [],
			inputErrors;
		// Configure form options
		options = options || form.options || {};
		form.options = options;
		// Loop over each of the form elements
		for (var i = 0; i < form.elements.length; i++) {
			formElement = form.elements[i];
			// Skip over hidden or disabled elements
			if (formElement.type.toLowerCase() === 'hidden' || !formElement.offsetParent || formElement.disabled || formElement.readonly) {
				continue
			}
			// Validate elements
			inputErrors = validateElement(formElement);
			if (inputErrors.length) {
				formErrors.push(inputErrors);
			}
		}
		return formErrors.length === 0;
	}

	/** 
	 *	Validate an element with a single, specific rule.
	 *	N.B. FCP rules can be found at com.uovo.utils.StringManip.
	 *
	 *	@param {object} elQuantity - Quantity input element
	 *	@param {string} rule - Rule to be tested from config.methods
	 *
	 *	@returns {boolean} - Returns if the element validates
	 */
	function validateRule(input, rule) {
		if (typeof methods[rule] === 'function') {
			return methods[rule](input);
		}
		return true;
	}

	return {
		element: validateElement,
		form: validateForm,
		rule: validateRule,
		methods: methods
	}

})(document);;


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
				setTimeout(function() {
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
				setTimeout(function() {
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
		auto: auto,
		toggle: toggle

	}

})(document);;


var btf = btf || {};

(function(d) {
	// Chgeck jsLoader dependency
	if (!btf.utils || !btf.utils.pageLoad) {
		return;
	}
	// Add event unless content is not 'loading'
	if (d.readyState !== 'loading') {
		btf.utils.pageLoad(d);
	} else {
		d.addEventListener("DOMContentLoaded", function() {
			btf.utils.pageLoad(d)
		});
	}

}(document));
