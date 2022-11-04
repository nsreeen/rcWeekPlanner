# rcWeekPlanner

I used this project to plan my weeks while in batch at the Recurse Center.  

It allows a user to view events from an ICS feed as well as custom events.  It only shows the current week's events.  

## Development
Run locally with docker compose to spin up a local database:
`docker compose build`
`docker compose up`
You will need to update the baseUrl in app.js.

To update the database schema, delete the db from the fly dashboard, and [recreate](https://fly.io/docs/reference/postgres/).
