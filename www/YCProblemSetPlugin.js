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

YCProblemSetPlugin.prototype.loadProblemContext = function (url, options) {
    options = options || {};
    cordova.exec(options.successCallback || null, options.errorCallback || null, "YCProblemSetPlugin", "loadProblemContext", [url, options]);
};

YCProblemSetPlugin.prototype.closeProblemSet = function (url, options) {
    options = options || {};
    cordova.exec(options.successCallback || null, options.errorCallback || null, "YCProblemSetPlugin", "closeProblemSet", [url, options]);
};

YCProblemSetPlugin.prototype.finishProblemSet = function (url, options) {
    options = options || {};
    cordova.exec(options.successCallback || null, options.errorCallback || null, "YCProblemSetPlugin", "finishProblemSet", [url, options]);
};

YCProblemSetPlugin.prototype.loadProblemSet = function (url, options) {
    options = options || {};
    cordova.exec(options.successCallback || null, options.errorCallback || null, "YCProblemSetPlugin", "loadProblemSet", [url, options]);
};


YCProblemSetPlugin.install = function () {
	if (!window.plugins) {
		window.plugins = {};
	}
	window.plugins.YCProblemSetPlugin = new YCProblemSetPlugin();
	return window.plugins.YCProblemSetPlugin;
};

cordova.addConstructor(YCProblemSetPlugin.install);
