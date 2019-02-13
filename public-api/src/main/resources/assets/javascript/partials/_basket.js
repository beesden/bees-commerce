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
				basketData.push({name:this.name, value:this.value});
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
		input.onchange =  function() {
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
		form: form, quantity: quantity
	}	

})(document);