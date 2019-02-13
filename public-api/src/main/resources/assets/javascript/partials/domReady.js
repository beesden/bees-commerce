var btf = btf || {};

(function (d) {
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