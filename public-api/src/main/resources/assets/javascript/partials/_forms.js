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
			for (var i = 0; i <  addressForms.length; i++) {
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
			formFields.push({name: 'ajax', value: true})
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
		checkoutAddressToggle: checkoutAddressToggle, giftCardCheck: giftCardCheck, numberSelect: numberSelect, validate: validate		
	}	

})(document);