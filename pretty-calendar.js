// This code is from: https://github.com/neelsomani/pretty-calendar

PrettyCalendar.prototype.weekView = true;
PrettyCalendar.prototype.transition = false;
PrettyCalendar.UNDEFINED_TIME = -2;
PrettyCalendar.EVENT_PADDING = 10;
PrettyCalendar.prototype.wrappingDiv;

function PrettyCalendar(events, divToPut, start, end, navigation, customLabels) {
    var timeRange = end - start;
    if (typeof navigation == 'undefined') navigation = false;
    if (typeof customLabels == 'undefined') {
        var weekday = new Array(5);
        weekday[0] = "Monday";
        weekday[1] = "Tuesday";
        weekday[2] = "Wednesday";
        weekday[3] = "Thursday";
        weekday[4] = "Friday";
        customLabels = weekday;
    }
    this.wrappingDiv = divToPut;
    this.genCalendar(customLabels, timeRange, start, end);
    if (navigation) {
        PrettyCalendar.addNavigation();
    }
    this.initTransitions();
    PrettyCalendar.commitEvents(events, timeRange, start, end);
}

PrettyCalendar.arrangeInDays = function (events) {
    var eventsToday = [];
    for (var i = 0; i < 5; i++) eventsToday[i] = [];
    for (var i = 0; i < events.length; i++) {
        var dayToArrange = 0;
        switch (events[i][0].toLowerCase()) {
        case "monday":
            dayToArrange = 0;
            break;
        case "tuesday":
            dayToArrange = 1;
            break;
        case "wednesday":
            dayToArrange = 2;
            break;
        case "thursday":
            dayToArrange = 3;
            break;
        case "friday":
            dayToArrange = 4;
            break;
        }
        var tempIndex = eventsToday[dayToArrange].length;
        eventsToday[dayToArrange][tempIndex] = [];
        eventsToday[dayToArrange][tempIndex][0] = events[i][1];
        eventsToday[dayToArrange][tempIndex][1] = events[i][2];
        eventsToday[dayToArrange][tempIndex][2] = events[i][3];
        if (events[i].length > 4) {
            eventsToday[dayToArrange][tempIndex][3] = events[i][4];
        }
    }
    return eventsToday;
}

PrettyCalendar.prototype.genCalendar = function (customLabels, timeRange, start, end) {
    $("#" + this.wrappingDiv).css("font-family", "Tahoma,Arial,sans-serif");
    $("#" + this.wrappingDiv).css("overflow-x", "hidden");
    $("#" + this.wrappingDiv).css("overflow-y", "hidden");
    $("#" + this.wrappingDiv).css("min-width", "760px");
    $("#" + this.wrappingDiv).css("min-height", "500px");
    var wrapperDiv = document.createElement("div");
    $(wrapperDiv).attr("id", "wrapper");
    var calendarDiv = document.createElement("div");
    $(calendarDiv).attr("id", "calendar");
    var sidebarDiv = document.createElement("div");
    $(sidebarDiv).attr("id", "sidebar");
    for (var i = start; i < end; i+=2) { //sets the time labels on the left margin
        var timeLabelDiv = document.createElement("div");
        $(timeLabelDiv).attr("class", "timeLabel");
        if (i==0) {
            textLabel = "12am"
        } else if (i==12) {
            textLabel = "12pm"
        } else if (i > 12) {
            textLabel = `${i-12}pm`
        } else {
            textLabel = `${i}am`
        }
        $(timeLabelDiv).text(textLabel);
        sidebarDiv.appendChild(timeLabelDiv);
    }
    for (var i = 0; i < 5; i++) {
        var dayDiv = document.createElement("div");
        $(dayDiv).attr("id", "day" + (i + 1));
        $(dayDiv).attr("class", "day");
        var dayLabel = document.createElement("div");
        $(dayLabel).attr("class", "dayName sep");
        var dayLabelText = "Sunday";
        dayLabelText = customLabels[i];
        $(dayLabel).text(dayLabelText);
        dayDiv.appendChild(dayLabel);
        for (var j = 0; j < timeRange+1; j++) {  // sets the separators (horizontal lines)
            var tempDiv = document.createElement("div");
            $(tempDiv).attr("class", "sep");
            dayDiv.appendChild(tempDiv);
        }
        calendarDiv.appendChild(dayDiv);
    }
    wrapperDiv.appendChild(sidebarDiv);
    wrapperDiv.appendChild(calendarDiv);
    document.getElementById(this.wrappingDiv).appendChild(wrapperDiv);
}

PrettyCalendar.timeToHours = function (formatted) {
    var timeHours = 0;
    var timeWithLabel = formatted;
    if (timeWithLabel.replace("pm", "") != timeWithLabel) {
        timeHours += 12;
    }
    var twoPieces = timeWithLabel.split(":");
    if (twoPieces[0] == "12") {
        timeHours -= 12;
    }
    timeHours = Number(timeHours) + Number(twoPieces[0]);
    timeHours = Number(timeHours) + Number(twoPieces[1].replace("am", "").replace("pm", "")) / 60;
    return timeHours;
}

PrettyCalendar.hoursToPercent = function (hours, timeRange) {
    return (100 * hours / timeRange) + ((24/timeRange) * 4.16);
}

function overlapsWithLastEvent(eventStart, eventEnd, lastEventStart, lastEventEnd) {
    var result = false;
    if (eventStart === lastEventStart) {
        result = true;
    }
    if (eventStart < lastEventStart && lastEventStart < eventEnd) {
        result = true;
    } else if (lastEventStart < eventStart && eventStart < lastEventEnd) {
        result = true;
    }
    return result;
}

PrettyCalendar.populateEvents = function (eventsToday, timeRange, start, end) {
    var counterTemp = 0;
    for (var j = 0; j < 5; j++) {
        var lastTime = PrettyCalendar.UNDEFINED_TIME;
        var lastEventEndHours = PrettyCalendar.UNDEFINED_TIME;
        var lastEventSummary = "";
        var lastEventStartHours = PrettyCalendar.UNDEFINED_TIME;
        var numToCompress = 1;
        if (eventsToday[j].length != 0) {
            eventsToday[j] = eventsToday[j].sort(function (a, b) {
                return PrettyCalendar.timeToHours(a[0]) - PrettyCalendar.timeToHours(b[0]);
            });
        }
        for (var i = 0; i < eventsToday[j].length; i++) {
            counterTemp++;

            var lastEventSummary = eventSummary;
            var lastEventEndHours = eventEndHours;
            var lastEventStartHours = eventStartHours;

            var eventStart = eventsToday[j][i][0];
            var eventSummary = eventsToday[j][i][1];
            var eventColor = eventsToday[j][i][2];
            var eventEnd = eventsToday[j][i][3];

            var eventStartHours = PrettyCalendar.timeToHours(eventStart); //startTime of event as 24h clock hour
            var eventEndHours = PrettyCalendar.timeToHours(eventEnd);

            if (eventStartHours < start || eventStartHours >= end) {
                continue;
            }

            var startOffset = PrettyCalendar.hoursToPercent((eventStartHours-start), timeRange); // start offset
            var lastPercentTemp = PrettyCalendar.hoursToPercent(lastTime, timeRange); // used to handle width offset when multiple events per hour
            var height = (PrettyCalendar.hoursToPercent((PrettyCalendar.timeToHours(eventEnd)-start), timeRange) - startOffset);

            // this bit is handling multiple events on the same hour
            //var multipleEventsAtSamehour = $("#calendar").height() * lastPercentTemp / 100 + $("#event" + (counterTemp - 1)).innerHeight()+ PrettyCalendar.EVENT_PADDING > $("#calendar").height() * startOffset / 100
            var overlapsWithlastEvent = overlapsWithLastEvent(eventStartHours, eventEndHours, lastEventStartHours, lastEventEndHours)
            if (overlapsWithlastEvent) {
                numToCompress++;
            } else {
                numToCompress = 1;
            }
            var formatWidth = 100 / numToCompress;

            lastTime = eventStartHours;
            var eventTempDiv = document.createElement("div");
            $(eventTempDiv).attr("class", "event");
            $(eventTempDiv).attr("id", "event" + (counterTemp));
            var heightSet = "height:auto;";
            if (eventsToday[j][i].length > 3) {
                heightSet = "height:" + height + "%;";
            }

            $(eventTempDiv).attr("style", "top:" + startOffset + "%;width:" + formatWidth + "%;background-color:" + eventsToday[j][i][2] + ";left:" + (100 - formatWidth) + "%;" + heightSet);

            // this also handles multiple events on the same hour
            if (formatWidth != 100) {
                for (var x = 0; x < numToCompress - 1; x++) {
                    $("#event" + (counterTemp - (x + 1))).css("width", formatWidth + "%");
                    $("#event" + (counterTemp - (x + 1))).css("left", (100 - formatWidth * (x + 2)) + "%");
                    $("#event" + (counterTemp - (x + 1))).attr("title", $("#event" + (counterTemp - (x + 1))).text());
                }
                $(eventTempDiv).attr("title", eventSummary);
            }

            $(eventTempDiv).text(eventSummary);
            $("#day" + (j + 1)).append(eventTempDiv);
        }
    }
}

PrettyCalendar.updateEvents = function (events, start, end) {
    var timeRange = end - start;
    $(".event").remove();
    PrettyCalendar.commitEvents(events, timeRange, start, end);
}

PrettyCalendar.prototype.initTransitions = function () {
    var tempCal = this;
    $(".day").click(function () {
        if (!tempCal.transition) {
            if (tempCal.weekView) {
                tempCal.transition = true;
                tempCal.weekView = false;
                $("#day5").not(this).css("width", "13%");
                $("#day1").not(this).css("width", "13%");
                $(".day").not(this).css("min-width", "0px");
                $(".day").not(this).animate({
                    width: "0%",
                }, 1000, function () {
                    $(".day").not(this).css("display", "none");
                });
                $(this).animate({
                    width: "100%",
                }, 1000, function () {
                    $(this).css("display", "block");
                    tempCal.transition = false;
                });
            } else {
                tempCal.transition = true;
                $(".day").css("display", "block");
                tempCal.weekView = true;
                $(".day").not(this).animate({
                    width: "20%",
                }, 1000, function () {
                    $(".day").css("min-width", "100px");
                    tempCal.transition = false;
                });
                $(this).animate({
                    width: "20%",
                }, 970);

            }
        }
    });
}

PrettyCalendar.prototype.addFooter = function (footerContents) {
    var footer = document.createElement("footer");
    $(footer).html(footerContents);
    document.getElementById(this.wrappingDiv).appendChild(footer);
}

PrettyCalendar.addNavigation = function () {
    var leftNavBtn = '<div class="directionNav" onclick="leftNav()"><span class="directionLabel">&lt;</span></div>';
    var rightNavBtn = '<div class="directionNav" onclick="rightNav()" style="left:95%;"><span class="directionLabel">&gt;</span></div>';
    $("#wrapper").append(leftNavBtn + rightNavBtn);
}

PrettyCalendar.commitEvents = function (events, timeRange, start, end) {
    events = PrettyCalendar.arrangeInDays(events);
    PrettyCalendar.populateEvents(events, timeRange, start, end);
}
