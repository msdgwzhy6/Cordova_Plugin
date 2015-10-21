"use strict";
function YCProblemSetPlugin() {
}

YCProblemSetPlugin.prototype.loadImage = function (url, options) {
	options = options || {};
	cordova.exec(options.successCallback || null, options.errorCallback || null, "YCProblemSetPlugin", "loadImage", [url, options]);
};
               
YCProblemSetPlugin.prototype.loadProblem = function (url, options) {
    options = options || {};
    cordova.exec(options.successCallback || null, options.errorCallback || null, "YCProblemSetPlugin", "loadProblem", [url, options]);
};


YCProblemSetPlugin.install = function () {
	if (!window.plugins) {
		window.plugins = {};
	}
	window.plugins.YCProblemSetPlugin = new YCProblemSetPlugin();
	return window.plugins.YCProblemSetPlugin;
};

cordova.addConstructor(YCProblemSetPlugin.install);
