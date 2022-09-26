let baseUrl = "https://rccal.fly.dev"

function parseEvents(events) {
    let eventsToRender = [];
    for (let i = 0; i < events.length; i++) {
          let dayOfWeek = events[i]["dayOfWeek"];
          let summary = (events[i].id > 0) ? `${events[i]["summary"]} (id:${events[i]["id"]})` : events[i]["summary"];
          let sd = new Date(events[i]["start"])
          let start = `${sd.getHours() > 12 ? sd.getHours() - 12 : sd.getHours()}:${sd.getMinutes().toString().padStart(2, '0')}${sd.getHours() < 12 ? "am" : "pm"}`
          let ed = new Date(events[i]["end"])
          let end = `${ed.getHours() > 12 ? ed.getHours() - 12 : ed.getHours()}:${ed.getMinutes().toString().padStart(2, '0')}${ed.getHours() < 12 ? "am" : "pm"}`
          let color = events[i].color.length > 0 ? events[i].color : "#c0c0c0";
          eventsToRender.push([dayOfWeek, start, summary, color, end]);
    }
    return eventsToRender
}

function getCalendar(calToken, renderCallback) {
      const xMLHttpRequest = new XMLHttpRequest();
      xMLHttpRequest.responseType = "json";
      let url = `${baseUrl}/calendars/${calToken}`;
      console.log(`GET to ${url}`);
      xMLHttpRequest.open("GET", url);
      xMLHttpRequest.responseType = "json";
      xMLHttpRequest.send();
      xMLHttpRequest.onload = function () {
            if (xMLHttpRequest.response) {
                console.log(xMLHttpRequest.response);
                let eventsToRender = parseEvents(xMLHttpRequest.response.events);
                let onlineHour = new Date(xMLHttpRequest.response.onlineTime).getHours();
                let offlineHour = new Date(xMLHttpRequest.response.offlineTime).getHours();
                var calendar = {
                  name: xMLHttpRequest.response.name,
                  onlineHour: onlineHour,
                  offlineHour: offlineHour,
                  events: eventsToRender,
                };
            } else {
                var calendar = null
            }
            renderCallback(calendar)
      }
}

function createEvent(calToken, summary, start, end, color, renderCallback) {
      const postHttp = new XMLHttpRequest()
      postHttp.open('POST', `${baseUrl}/events`)
      postHttp.setRequestHeader('Content-type', 'application/json')
      postHttp.responseType = "json";
      let data = {
            summary: summary,
            start: start,
            end: end,
            calToken: calToken,
            color: color,
      }
      postHttp.send(JSON.stringify(data))
      postHttp.onload = function() {
          console.log(postHttp.response);
          getCalendar(calToken, renderCallback);
      }
}

function deleteEvent(id, renderCallback) {
    const postHttp = new XMLHttpRequest()
    postHttp.open('DELETE', `${baseUrl}/events`)
    postHttp.setRequestHeader('Content-type', 'application/json')
    postHttp.responseType = "json";
    let data = {
          id:id,
    }
    postHttp.send(JSON.stringify(data))
    postHttp.onload = function() {
        renderCallback(calToken)
    }
}

function createCalendar(name, online, offline, rcToken, renderCallback) {
    let startTime = new Date();
    startTime.setHours(online.split(":")[0]);
    let endTime = new Date();
    endTime.setHours(offline.split(":")[0]);

    const postHttp = new XMLHttpRequest()
    postHttp.open('POST', `${baseUrl}/calendars`)
    postHttp.setRequestHeader('Content-type', 'application/json')
    postHttp.responseType = "json";
    let data = {
          name:name,
          online:startTime,
          offline:endTime,
          rcToken:rcToken,
    }
    postHttp.send(JSON.stringify(data))
    postHttp.onload = function() {
        renderCallback(postHttp.response.viewOnlyToken, postHttp.response.editToken);
    }
}

