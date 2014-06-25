var KEY_LEFT = 88; // x
var KEY_RIGHT = 77; // m

var FAR_BLANK = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
var NEAR_BLANK = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
var TOO_EARLY = -100;
var TOO_LATE = -1;

var TOP = "TOP";
var BOTTOM = "BOTTOM";

var NEAR_LEFT = "NEAR_LEFT";
var FAR_LEFT = "FAR_LEFT";
var NEAR_RIGHT = "NEAR_RIGHT";
var FAR_RIGHT = "FAR_RIGHT";
var MIDDLE = "MIDDLE";

// use ts (taskswitching) from the global namespace
var ts = {};

ts.time = (function() {
    var performance = window.performance || {};
    var performanceFunction = performance.now
            || performance.mozNow
            || performance.webkitNow
            || performance.msNow
            || performance.oNow;

    if (performanceFunction) {
        return performanceFunction.bind(performance);
    }

    return function() {
        return new Date().getTime();
    };
})();

// init debug mode if needed
ts.debug = window.location.search.match(".*debug.*");

ts.lastReactionTime = null;


var ResultObject = function(listId, testType, info, participant) {
    this.listId = listId;
    this.testType = testType;
    this.info = info;
    this.additionalKeyPresses = [];
    this.reactions = [];
    this.participant = participant;
    this.initTime = ts.time();
    this.testStartTime = null;
    
    this.setStartTime = function() {
        this.testStartTime = ts.time();
        console.log("start time set");
    };
    
    this.addReactionInformation = function(reactionData) {
        this.reactions.push(reactionData);
    };
    
    this.addAdditionalKeyPressInformation = function(additionalKeyPressData) {
        this.additionalKeyPresses.push(additionalKeyPressData);
    };
    
    console.log("new resultobject created, time: " + this.initTime);    
};

console.log("core loaded");
