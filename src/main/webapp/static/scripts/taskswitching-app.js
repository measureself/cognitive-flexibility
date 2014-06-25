if (!ts) {
    alert("LOAD TASKSWITCHING-CORE.JS FIRST");
}

// data that we gather from user inputs will be
// appended to this object
ts.result = null;

// and the main code
ts.program = {
    primaryInitDone: false,
    waitingForPress: false,
    keysBound: false,
    listId: 0,
    initVars: function(testType) {
        ts.program.currentTimeoutVariable = null;
        ts.program.currentDataElement = null;
        ts.program.lastShowTime = TOO_EARLY;
        ts.program.currentTest = null;
        ts.program.currentTestData = null;
        ts.program.currentTestConfig = null;
        ts.program.testType = testType;
        ts.program.lastReactionTime = null;
        ts.program.testTypeCounts = 0;
    },
    step: 0,
    init: function() {
        console.log("pre-init, step: " + ts.program.step);
        ts.specificTest = ts.ui.getUrlParam("test");
        ts.limitReactions = ts.ui.getUrlParam("limit");

        ts.program.step++;

        if (ts.specificTest && !ts.primaryInitDone) {
            switch (ts.specificTest) {
                case "REACTION":
                    ts.program.step = 1;
                    break;
                case "NUMBERREACTION":
                    ts.program.step = 2;
                    break;
                case "CHARACTERREACTION":
                    ts.program.step = 4;
                    break;
                case "TASKSWITCHING":
                    ts.program.step = 6;
                    break;
            }

            ts.primaryInitDone = true;
        }

        if (ts.program.step > 7) {
            console.log("attempting to initiate a test with step " + ts.program.step);
            console.log("-- we're finished :)");
            return;
        }

        var testName = "";
        var testType = "";
        var startTexts = null;
        var endTexts = null;

        switch (ts.program.step) {
            case 1:
                ts.config.elementVisibleInMs = 1950;
                testName = "REACTION";
                startTexts = ts.texts.REACTION_TEST_START_TEXT;
                endTexts = ts.texts.REACTION_TEST_END_TEXT;
                break;
            case 2:
                ts.config.pauseBeforeFirstShow = 2500;
                ts.config.pauseAfterWrongAnswerInMs = 1500;
                ts.config.pauseAfterCorrectAnswerInMs = 150;
                ts.config.elementVisibleInMs = 2540;

                testName = "NUMBERREACTION";
                startTexts = ts.texts.NUMBERTASK_PRACTICE_START_TEXT;
                endTexts = ts.texts.NUMBERTASK_PRACTICE_END_TEXT;
                break;
            case 3:
                testName = "NUMBERREACTION";
                startTexts = ts.texts.NUMBERTASK_START_TEXT;
                endTexts = ts.texts.NUMBERTASK_END_TEXT;
                break;
            case 4:
                testName = "CHARACTERREACTION";
                startTexts = ts.texts.CHARACTERTASK_PRACTICE_START_TEXT;
                endTexts = ts.texts.CHARACTERTASK_PRACTICE_END_TEXT;
                break;
            case 5:
                testName = "CHARACTERREACTION";
                startTexts = ts.texts.CHARACTERTASK_START_TEXT;
                endTexts = ts.texts.CHARACTERTASK_END_TEXT;
                break;
            case 6:
                testName = "TASKSWITCHING";
                startTexts = ts.texts.TASKSWITCHING_PRACTICE_START_TEXT;
                endTexts = ts.texts.TASKSWITCHING_PRACTICE_END_TEXT;
                break;
            case 7:
                testName = "TASKSWITCHING";
                startTexts = ts.texts.TASKSWITCHING_START_TEXT;
                endTexts = ts.texts.TASKSWITCHING_END_TEXT;
                break;
            default:
                console.log("unable to initialize test");
        }

        if (ts.program.step % 2 === 0) {
            testType = "practice";
            ts.config.loadPracticeTest(testName,
                    startTexts,
                    endTexts);
        } else {
            testType = "game";

            var url = ts.fn.getListUrl(testName, testType);
            ts.config.loadTestSet(testName,
                    startTexts,
                    endTexts,
                    url);
        }

        ts.program.initializeTest(testType);
    },
    initializeTest: function(testType) {
        ts.program.initVars(testType);

        // init binds keys for functions -- only during first round
        // 
        // use keydown to get the action as fast as possible
        // check http://css-tricks.com/snippets/javascript/javascript-keycodes/
        // for other keycodes

        // bind keys only at the very beginning
        if (!ts.program.keysBound) {
            ts.ui.bindKeys(KEY_LEFT,
                    function() {
                        ts.program.pressed("LEFT");
                    },
                    KEY_RIGHT,
                    function() {
                        ts.program.pressed("RIGHT");

                    },
                    function(pressedKey) {
                        ts.program.pressed(pressedKey);

                    });
            ts.program.keysBound = true;
        }

        ts.program.startNextTest();
    },
    startNextTest: function() {
        ts.program.lastShowTime = TOO_EARLY;
        ts.program.currentDataElement = 0;

        // get current test
        ts.program.currentTest = ts.tests[0];
        ts.program.currentTestData = ts.program.currentTest.elements[0];

        // init result variables
        var participant = {};
        participant.username = $("#participant-id").val();

        ts.result = new ResultObject(ts.program.listId, ts.program.currentTest.testType, ts.program.testType, participant);

        ts.ui.init(ts.program.currentTest.startText);
        // program will start once user presses space
        ts.program.bindSpace();
    },
    bindSpace: function() {
        ts.program.waitingForPress = true;
        $(document).one('keydown', function(e) {
            ts.program.handleSpace(e);
        });
    },
    handleSpace: function(e) {
        if (e.which !== 32) {
            ts.program.bindSpace();
            return;
        }

        ts.program.waitingForPress = false;
        ts.ui.showGameArea();
        ts.program.start();
    },
    start: function() {
        ts.result.setStartTime();

        ts.program.clear();
        // call the function showNext after a pre-configured pause
        var waitTime = ts.config.pauseBeforeFirstShow;

        var element = ts.program.currentTestData[ts.program.currentDataElement];
        if (element.waitForMs) {
            waitTime = element.waitForMs;

            if (ts.program.lastReactionTime !== null) {
                waitTime -= ts.program.lastReactionTime;
            }
        }

        ts.program.currentTimeoutVariable
                = setTimeout(ts.program.show, waitTime);
    },
    show: function() {
        ts.program.clear();
        var data = ts.program.currentTestData[ts.program.currentDataElement];

        var content = data.text;
        switch (data.align) {
            case NEAR_LEFT:
                content += NEAR_BLANK;
                break;
            case FAR_LEFT:
                content += FAR_BLANK;
                break;
            case NEAR_RIGHT:
                content = NEAR_BLANK + content;
                break;
            case FAR_RIGHT:
                content = FAR_BLANK + content;
                break;
                // default MIDDLE
        }

        ts.program.lastShowTime = ts.time();
        ts.program.currentTimeoutVariable
                = setTimeout(ts.program.hideAndWaitForNext, ts.config.elementVisibleInMs);

        ts.ui.showContent(data.location, content);
    },
    hideAndWaitForNext: function(didReact, wasCorrect) {
        if (!didReact && ts.program.lastShowTime > 0) {
            ts.program.lastReactionTime = null;

            if (ts.result.testType === "TASKSWITCHING") {
                var testIdx = ts.program.currentDataElement;
                ts.result.addReactionInformation({
                    stimulantIndex: ts.program.currentDataElement,
                    showTime: ts.program.lastShowTime,
                    keyPressTime: null,
                    keyPress: "NONE",
                    correct: false,
                    elementType: ts.program.currentTestData[testIdx].location,
                    place: ts.program.currentTestData[testIdx].place,
                    chainLength: ts.program.currentTestData[testIdx].chainLength,
                    lastSeries: ts.program.currentTestData[testIdx].lastSeries
                });
            } else {
                ts.result.addReactionInformation({
                    stimulantIndex: ts.program.currentDataElement,
                    showTime: ts.program.lastShowTime,
                    keyPressTime: null,
                    keyPress: "NONE",
                    correct: false,
                    elementType: ts.program.currentTestData[ts.program.currentDataElement].location
                });
            }
        }

        ts.program.lastShowTime = TOO_LATE;
        ts.program.clear();

        ts.program.currentDataElement++;

        // if test is done, go for the next test
        if (ts.program.currentDataElement >= ts.program.currentTestData.length || (ts.limitReactions && ts.program.currentDataElement >= 5)) {
            ts.program.nextTestOrEnd();
            return;
        }

        var waitTime = ts.config.pauseAfterWrongAnswerInMs;
        if (wasCorrect) {
            waitTime = ts.config.pauseAfterCorrectAnswerInMs;
        }

        try {
            var element = ts.program.currentTestData[ts.program.currentDataElement];
            if (element.waitForMs) {
                waitTime = element.waitForMs;

                if (ts.program.lastReactionTime !== null) {
                    waitTime -= ts.program.lastReactionTime;
                }
            }
        } finally {

        }

        ts.program.currentTimeoutVariable
                = setTimeout(ts.program.show, waitTime);

    },
    additionalPress: function(key) {
        if (ts.result.testType === "TASKSWITCHING") {
            if (ts.program.currentDataElement < ts.program.currentTestData.length) {
                console.log("additional press during test");
                var testIdx = ts.program.currentDataElement;
                ts.result.addAdditionalKeyPressInformation({
                    stimulantIndex: ts.program.currentDataElement,
                    keyPress: key,
                    keyPressTime: ts.time(),
                    place: ts.program.currentTestData[testIdx].place,
                    chainLength: ts.program.currentTestData[testIdx].chainLength,
                    lastSeries: ts.program.currentTestData[testIdx].lastSeries
                });
            } else {
                console.log("additional press after test");
            }

        } else {
            ts.result.addAdditionalKeyPressInformation({
                stimulantIndex: ts.program.currentDataElement,
                keyPress: key,
                keyPressTime: ts.time()
            });
        }
    },
    clear: function() {
        ts.ui.clear();

        if (ts.program.currentTimeoutVariable) {
            clearTimeout(ts.program.currentTimeoutVariable);
        }
    },
    pressed: function(answer) {
        if (ts.program.waitingForPress) {
            console.log("pressed a key while waiting for a keypress (e.g. startup, init): ignoring from logs.");
            return;
        }

        if (answer !== KEY_LEFT && answer !== "LEFT" && answer !== KEY_RIGHT && answer !== "RIGHT") {
            ts.program.additionalPress("INVALID_KEY:" + answer);
            return;
        }

        if (ts.program.lastShowTime === TOO_EARLY || ts.program.lastShowTime === TOO_LATE) {
            ts.program.additionalPress("PRESS_OUTSIDE_TIMEFRAME:" + answer);
            return;
        }

        var elementShowTime = ts.program.lastShowTime;

        ts.program.clear();

        var currentTime = ts.time();
        var currentElement = ts.program.currentTestData[ts.program.currentDataElement];
        var answerWasCorrect = currentElement.correctAnswer === answer;

        if (currentElement.correctAnswer === "ALL") {
            answerWasCorrect = true;
        }

        var elementType = "";
        if (currentElement.location === TOP) {
            elementType = "NUMBER";
        } else if (currentElement.location === BOTTOM) {
            elementType = "CHARACTER";
        }

        if (ts.result.testType === "TASKSWITCHING") {
            ts.result.addReactionInformation({
                stimulantIndex: ts.program.currentDataElement,
                showTime: elementShowTime,
                keyPressTime: currentTime,
                keyPress: answer,
                correct: answerWasCorrect,
                elementType: elementType,
                place: currentElement.place,
                chainLength: currentElement.chainLength,
                lastSeries: currentElement.lastSeries
            });
        } else {
            ts.result.addReactionInformation({
                stimulantIndex: ts.program.currentDataElement,
                showTime: elementShowTime,
                keyPressTime: currentTime,
                keyPress: answer,
                correct: answerWasCorrect,
                elementType: elementType
            });
        }


        ts.program.lastReactionTime = (currentTime - elementShowTime);
        ts.program.hideAndWaitForNext(true, answerWasCorrect); // if we are here, we have always reacted
    },
    nextTestOrEnd: function() {
        ts.program.testTypeCounts++;
        ts.result.testEndTime = ts.time();
        ts.program.lastShowTime = TOO_LATE;
        ts.program.clear();

        console.log("submitting results, count of current type of tests: " + ts.program.testTypeCounts);

        // not really shown for long
        ts.ui.showGuideText(ts.program.currentTest.endText);
        ts.program.submitResults(function(response) {
            ts.ui.showBasicStats(response);
        });


        // if we've been practicing, give an option to do something else for
        // a while
        if (ts.program.testType === "practice") {

            if (ts.program.testTypeCounts >= ts.config.maxTrainingIterations) {
                console.log("has practiced enough");
                ts.program.init();
                return;
            }

            console.log("can still practice");

            var practiceAttemptsLeftText = "";
            if (ts.result.testType === "TASKSWITCHING") {
                practiceAttemptsLeftText = ts.texts.TASKSWITCHING_PRACTICE_ATTEMPTS_LEFT;
            } else if (ts.result.testType === "NUMBERREACTION") {
                practiceAttemptsLeftText = ts.texts.NUMBERTASK_PRACTICE_ATTEMPTS_LEFT;
            } else if (ts.result.testType === "CHARACTERREACTION") {
                practiceAttemptsLeftText = ts.texts.CHARACTERTASK_PRACTICE_ATTEMPTS_LEFT;
            } else {
                console.log("not a valid test type");
            }

            $("#guide").html(practiceAttemptsLeftText);
            console.log("binding a keypress for listning to the practice attempt");
            
            ts.program.waitingForPress = true;
            $(document).one('keydown', function(e) {
                $("#result").hide();

                ts.program.waitingForPress = false;
                if (e.keyCode === 0 || e.keyCode === 32) {
                    // space
                    console.log("pressed space, init practice");
                    ts.program.startNextTest();
                } else {
                    // any key
                    console.log("pressed other than space, init test");
                    ts.program.init();
                }

            });

            return;
        }

        console.log("the previous round was not a practice round, init next test type");
        // we were not practicing, lets init the next step
        ts.program.init();
    },
    submitResults: function(successFunction) {
        console.log("submitting: ");
        console.log(ts.result);
        var data = JSON.stringify(ts.result);
        $.ajax({
            type: "POST",
            url: ts.config.backendResultAddress,
            dataType: "json",
            contentType: "application/json; charset=UTF-8",
            data: data,
            success: successFunction
        });
    }
};


// basic func to make sure that data is submitted
window.onbeforeunload = function() {
    if (ts.result && ts.result.length > 0) {
        ts.program.submitResults(function() {
            alert("pewpew");
        });
    }
};
$(document).ready(function() {
    $('a[rel!=ext]').click(function() {
        window.onbeforeunload = null;
    });
    $('form').submit(function() {
        window.onbeforeunload = null;
    });
});


ts.fn = {};
ts.fn.createTest = function(testType, startText, endText, elements) {
    this.testType = testType;
    this.startText = startText;
    this.endText = endText;
    this.elements = elements;
};

ts.fn.getNextListId = function(userId, testType, info) {
    console.log("retrieving next list id, user: " + userId + ", test type: " + testType + ", info: " + info);
    var result;
    $.ajax({
        type: "GET",
        url: ts.config.backendListCountAddress + "?userId=" + userId + "&testType=" + testType + "&info=" + info,
        async: false,
        success: function(data) {
            result = data;
        }
    });

    return result;
};
ts.fn.getListUrl = function(testName, testType) {
    ts.program.listId = ts.fn.getNextListId($("#participant-id").val(), testName, testType);

    if (testName === "TASKSWITCHING") {
        return "static/data/single/taskswitching-data-" + (ts.program.listId % 40) + ".json";
    } else if (testName === "CHARACTERREACTION") {
        return "static/data/single/characterreaction-data-" + (ts.program.listId % 40) + ".json";
    } else if (testName === "NUMBERREACTION") {
        return "static/data/single/numberreaction-data-" + (ts.program.listId % 40) + ".json";
    } else if (testName === "REACTION") {
        return "static/data/single/reaction-data-" + (ts.program.listId % 10) + ".json";
    }
};