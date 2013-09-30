var framework = framework || 
(function() {
	function pageScript(config, containerSelector) {
		if (config) {
			for (bindName in config) {
				var bindsFound = null;
				if (containerSelector) {
					bindsFound = $(containerSelector + ' [data-bind='+ bindName + ']');
				}
				else {
					bindsFound = $('[data-bind='+ bindName + ']');
				}
				for (var eventName in config[bindName]) {
					if (typeof config[bindName][eventName] == 'function') {
						bindsFound.on(eventName, {method: config[bindName][eventName]}, methodCallback);
					}
				}
			}
		}
		var dataLoads = $('div[data-load]');
		var i = 0, size = dataLoads.length;
		for (i = 0; i < size; i++) {
			var url = $(dataLoads[i]).data().load;
			$(dataLoads[i]).removeData("load");
			$(dataLoads[i]).load(url);
		}
	}
	
	function methodCallback(event) {
		var target = $(event.delegateTarget);
		event.data.method(target.data(), target);
	}
	
	function getUrl(url) {
		var contextPath = $('body').data().contextpath;
		if (contextPath) {
			url = contextPath + "/" + url;
		}
		return url;
	}
	
	return {
		pageScript: pageScript,
		getUrl: getUrl
	};
})();