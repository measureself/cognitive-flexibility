
ts.ui = {};

ts.ui.clear = function() {
    if (ts.debug) {
        $("#timingbox").hide();
    }

    $("#" + TOP).html("&nbsp;");
    $("#" + BOTTOM).html("&nbsp;");
    $("#guide").html("&nbsp;");
};

ts.ui.getUrlParam = function(key) {
    var result = new RegExp(key + "=([^&]*)", "i").exec(window.location.search);
    return result && unescape(result[1]) || "";
};

ts.ui.bindKeys = function(leftKeyCode, leftFunctionCall, rightKeyCode, rightFunctionCall, anyOtherFunctionCall) {
    console.log("Binding keys for test");
    $(document).keydown(function(e) {
        switch (e.which) {
            case leftKeyCode: // x
                leftFunctionCall();
                break;
            case rightKeyCode: // n
                rightFunctionCall();
                break;
            default:
                anyOtherFunctionCall(e.which); // any other key
                break;
        }

        e.preventDefault();
    });
};

ts.ui.testsFinished = function(guideText) {
    $("#wrapper").hide();
    $("#guide").html(guideText);
    $("#guide").show();
};

ts.ui.init = function(text) {
    $("#entry").hide();
    $("#wrapper").hide();

    $("#guide").show();
    $("#guide").html(text);
};

ts.ui.showBasicStats = function(statContainer) {
    $("#hitsPercentage").html(statContainer.hitsPercentage);
    $("#reactionTime").html(statContainer.reactionTime);
    $("#result").show();
};

ts.ui.showContent = function(location, content) {
    $("#" + location).html(content);
    if (ts.debug) {
        $("#timingbox").show();
    }
};

ts.ui.showGameArea = function() {
    $("#result").hide();
    $("#guide").hide();
    $("#wrapper").show();
};

ts.ui.showGuideText = function(guideText) {
    $("#wrapper").hide();
    $("#guide").html(guideText);
    $("#guide").show();
};

console.log("ui loaded");