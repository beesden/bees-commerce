var btf = btf || {};

btf.productImages = (function(d) {

	var config = {
		altImageMax: 5,
		zoomClass: 'product-zoom'
	}

	/** 
	 *	Alternate images are added to show differnet photo variants for a given product. 
	 *	A HEAD request is used to check the images exist, and if they do they are inserted onto the page.
	 *	A click event is added which exchanges the alternate image with the main image.
	 *	The Do-While loop ensures at least one image is always shown.
	 *
	 *	@param {element} container - Image element container to add new alternate images into
	 */ 
	function altImages(container) {
		var productImage = d.querySelector('.product-large img'),
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
		zoomImage.onload = function () {
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
		altImages: altImages, zoom: zoom
	}	

})(document); 