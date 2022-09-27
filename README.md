This project is a personal RC calendar viewer.

It only shows the current week's events.  You can create a calendar with your RC calendar token, and then view the events you are subscribed to.  You can also add extra events.  

## Usage limitations
- If you delete an event, you have to open the page in a new tab to view the remaining events
- 12 hour calendar time spans are most likely to render correctly

## Development
Run locally with docker compose to spin up a local database:
`docker compose build`
`docker compose up`
You will need to update the baseUrl in app.js.

To update the database schema, delete the db from the fly dashboard, and (recreate)[https://fly.io/docs/reference/postgres/].

## TODO
- [ ] Fix delete event (currently have to open the page in a new tab after deleting)
- [ ] Fix CORS
- [ ] Return reasonable HTTP responses in case of failure
- [ ] Persist logs
- [ ] Style create & delete panels
- [ ] Fix event widths bug
- [ ] Deal with the UI only supporting 12h days
- [ ] Move day name labels up
- [ ] Organise use of tokens/ cal ids better (+ validate)