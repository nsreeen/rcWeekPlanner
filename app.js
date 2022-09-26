let baseUrl = "http://0.0.0.0:8080" //"https://rccal.fly.dev"

function parseEvents(events) {
    let eventsToRender = [];
    for (let i = 0; i < events.length; i++) {
          console.log(events[i]);
          let dayOfWeek = events[i]["dayOfWeek"];
          let summary = (events[i].id > 0) ? `${events[i]["summary"]} (id:${events[i]["id"]})` : events[i]["summary"];
          let sd = new Date(events[i]["start"])
          let start = `${sd.getHours() > 12 ? sd.getHours() - 12 : sd.getHours()}:${sd.getMinutes().toString().padStart(2, '0')}${sd.getHours() < 12 ? "am" : "pm"}`
          let ed = new Date(events[i]["end"])
          let end = `${ed.getHours() > 12 ? ed.getHours() - 12 : ed.getHours()}:${ed.getMinutes().toString().padStart(2, '0')}${ed.getHours() < 12 ? "am" : "pm"}`
          eventsToRender.push([dayOfWeek, start, summary, "#c0c0c0", end]);
    }
    return eventsToRender
}

function getCalendar(calToken, renderCallback) {
      console.log("hello")
      const xMLHttpRequest = new XMLHttpRequest();
      xMLHttpRequest.responseType = "json";
      xMLHttpRequest.open("GET", `${baseUrl}/calendars/${calToken}`);
      xMLHttpRequest.responseType = "json";
      xMLHttpRequest.send();
      xMLHttpRequest.onload = function () {
            console.log(xMLHttpRequest.response);
            if (xMLHttpRequest.response) {
                console.log("here", typeof xMLHttpRequest.response);
                let eventsToRender = parseEvents(xMLHttpRequest.response.events);
                var calendar = eventsToRender
            } else {
                var calendar = null
            }
            renderCallback(calendar)
      }
}

function createEvent(calToken, summary, start, end, renderCallback) {
      const postHttp = new XMLHttpRequest()
      postHttp.open('POST', `${baseUrl}/events`)
      postHttp.setRequestHeader('Content-type', 'application/json')
      postHttp.responseType = "json";
      let data = {
            summary: summary,
            start: start,
            end: end,
            calToken: calToken,
      }
      postHttp.send(JSON.stringify(data))
      postHttp.onload = function() {
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
    const postHttp = new XMLHttpRequest()
    postHttp.open('POST', `${baseUrl}/calendars`)
    postHttp.setRequestHeader('Content-type', 'application/json')
    postHttp.responseType = "json";
    let data = {
          name:name,
          online:online,
          offline:offline,
          rcToken:rcToken,
    }
    postHttp.send(JSON.stringify(data))
    postHttp.onload = function() {
        let calToken = postHttp.response.calToken;
        renderCallback(calToken);
    }
}

