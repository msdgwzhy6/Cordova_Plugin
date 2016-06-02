"use strict";
function YCProblemSetPlugin() {
}


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
YCProblemSetPlugin.prototype.recordWrongProblems = function (answers,options) {
    options = options || {};
    cordova.exec(options.successCallback || null, options.errorCallback || null, "YCProblemSetPlugin", "recordWrongProblems", [answers,options]);
};
YCProblemSetPlugin.prototype.levelFail = function (levelId,isHanger,options) {
    options = options || {};
    cordova.exec(options.successCallback || null, options.errorCallback || null, "YCProblemSetPlugin", "levelFail", [levelId,isHanger,options]);
};
YCProblemSetPlugin.prototype.levelSuccess = function (levelId,postBody,isLastLevel,options) {
    options = options || {};
    cordova.exec(options.successCallback || null, options.errorCallback || null, "YCProblemSetPlugin", "levelSuccess", [levelId,postBody,isLastLevel,options]);
};

YCProblemSetPlugin.prototype.finishExam = function (pass,examTopicId,failedGuide,options) {
    options = options || {};
    cordova.exec(options.successCallback || null, options.errorCallback || null, "YCProblemSetPlugin", "finishExam", [pass,examTopicId,failedGuide,options]);
};

YCProblemSetPlugin.install = function () {
	if (!window.plugins) {
		window.plugins = {};
	}
	window.plugins.YCProblemSetPlugin = new YCProblemSetPlugin();
	return window.plugins.YCProblemSetPlugin;
};

cordova.addConstructor(YCProblemSetPlugin.install);
