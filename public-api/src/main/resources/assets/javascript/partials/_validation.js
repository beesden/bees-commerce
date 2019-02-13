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
			if ( value.length < 13 || value.length > 19 ) {
				return false;
			}
			for (n = value.length - 1; n >= 0; n--) {
				cDigit = value.charAt(n);
				nDigit = parseInt(cDigit, 10);
				if (bEven) {
					if ((nDigit *= 2 ) > 9) {
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
			for(var i = 0; i < el.value.length; i++) {
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
		element: validateElement, form: validateForm, rule: validateRule, methods: methods
	}

})(document);