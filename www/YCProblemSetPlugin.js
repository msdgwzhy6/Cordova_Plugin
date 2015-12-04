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

YCProblemSetPlugin.prototype.loadProblemContext = function (options) {
    options = options || {};
    cordova.exec(options.successCallback || null, options.errorCallback || null, "YCProblemSetPlugin", "loadProblemContext", [options]);
};

YCProblemSetPlugin.prototype.closeProblemSet = function (options) {
    options = options || {};
    cordova.exec(options.successCallback || null, options.errorCallback || null, "YCProblemSetPlugin", "closeProblemSet", [options]);
};

YCProblemSetPlugin.prototype.finishProblemSet = function (pass,setId, options) {
    options = options || {};
    cordova.exec(options.successCallback || null, options.errorCallback || null, "YCProblemSetPlugin", "finishProblemSet", [pass,setId, options]);
};

YCProblemSetPlugin.prototype.loadProblemSet = function (options) {
    options = options || {};
    cordova.exec(options.successCallback || null, options.errorCallback || null, "YCProblemSetPlugin", "loadProblemSet", [options]);
};
YCProblemSetPlugin.prototype.recordTrackInfo = function (points,options) {
    options = options || {};
    cordova.exec(options.successCallback || null, options.errorCallback || null, "YCProblemSetPlugin", "recordTrackInfo", [points,options]);
};
YCProblemSetPlugin.prototype.recordWrongProblems = function (options) {
    options = options || {};
    cordova.exec(options.successCallback || null, options.errorCallback || null, "YCProblemSetPlugin", "recordWrongProblems", [options]);
};

YCProblemSetPlugin.install = function () {
	if (!window.plugins) {
		window.plugins = {};
	}
	window.plugins.YCProblemSetPlugin = new YCProblemSetPlugin();
	return window.plugins.YCProblemSetPlugin;
};

cordova.addConstructor(YCProblemSetPlugin.install);
