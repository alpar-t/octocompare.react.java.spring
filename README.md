About
=====

joggr.io is an application that tracks jogging times of users.

Features:
 - Create an account and log in
 - When logged in, user can see, edit and delete his times he entered
 - Implement at least three roles with different permission levels: 
     - a regular user would only be able to CRUD on their owned records
     - a user manager would be able to CRUD users
     - an admin would be able to CRUD all records and users.
- Each time entry when entered has a date, distance, and time
- When displayed, each time entry has an average speed
- Filter by dates from-to
- Report on average speed & distance per week
- REST API. 
  - possible to perform all user actions via the API, including authentication.
  - In any case you should be able to explain how a REST API works and 
    demonstrate that by creating functional tests that use the REST Layer directly. 
    Please be prepared to use REST clients like Postman, cURL, etc for this purpose.
All actions need to be done client side using AJAX, refreshing the page is not acceptable. 
Bonus: unit and e2e tests!
You will not be marked on graphic design, however, do try to keep it as tidy as possible.

Bonus feature: jog entries can be entered while the server is down and will be sent as it comes back
up.

How To use 
==========

JDK Version 8 is required.
Start the app with:
    
    ./gradlew bootRun

Navigate to (http://127.0.0.1:8080/).
Authenticate with user name 'admin' and password 'password' when asked. 
You can then register additional users, or use the same on the login prompt. 
The first authentication is not part of the app, but it was kept to prevent 
accidental exposure. 
Thus the double authentication.

Known issues and limitations:
------------------------------

The application is not as it stand ready for production.
It's meant to be a proof of concept for initial validation.
Taking the application to production will require additional steps.

- No [CORS](https://developer.mozilla.org/en-US/docs/Web/HTTP/Access_control_CORS) support.
- No browser support matrix is defined, no cross browser testing was done 
- uses volatile embedded database, changes are not persisted across application restarts
- end to end tests are minimal
- removing job entires are not correctly carried across different clients of the same user.
    - the client pulls for updates
- the app was not particularity tested for large scale. 
    - the client pulling can particularity prove limiting. A push model would be desirable.
- the back-end has no pagination support 
    - it's assumed that the per user volumes will not exceed network transfer and browser
        capabilities (the volume of per user data is of acceptable size to send over the network and
        keep in browser memory).
    - clients use local storage to prevent from re-fetching content that did not change.
- back-end could use more functionality driven integration tests ( existing ones favor security ) 
- no out of the box setup for a relational database ( but easily achievable ) 
- frontend is not production ready: optimizations might still be needed, source-maps are exposed,
    etc.
- security aspects that need to be considered for production
    - credentials are sent in clear over the network ( http basic authentication ) 
    - credentials are persisted in the front-end in clear ( stored to local storage as well )
    - JWT tokens should be implemented as a next step.
- the application is not stopped after running end to end tests
    - it's left running in the background and has to be killed manually

Developer Notes
===============

The only pre-requisite to build and run is JDK version 8 and an internet connection.

To start the application:

    ./gradlew bootRun

Then navigate to: http://localhost:8080/

To run all tests on both ends:

    ./gradlew check

Set up git hooks to make sure commits and pushes are sane:
    
    ./git/create_hooks.sh

Front End
---------

Single page [react](https://facebook.github.io/react/) with [redux](http://redux.js.org/) to manage state.
No router is used. 
UX is implemented using [material-ui](http://www.material-ui.com/).
Everything is built with [webpack](https://webpack.github.io/).

The front-end is meant to be decoupled. 
It manages it's own state and has it's own model.
Interaction with the back-end is via pooling for server state periodically to merge in new
entries, and pushing state changes back to the server when the client state changes. 

The back-end also servers up the files for the front-end, but this is just as a convenience. 
The front-end could be fully served from a CDN.

Back End
--------

The back-end is a [spring boot](https://spring.io/guides/gs/spring-boot/) application
using _RestControllers_. 

The persistence is dealt with using [spring-data](http://projects.spring.io/spring-data/).
This assures that the underlying data store can be easily changed going forward. 
The overall goal is to keep the application as independent as possible from the actual data store and access technology. 
This will assure us flexibility to pick the best solutions going forward as the project grows.
In addition [spring-data-rest](http://docs.spring.io/spring-data/rest/docs/current/reference/html/) provides implementations for CRUD APIs,
allowing us to show an initial demo sooner, and save on development cost without adding technical debt. 

Authentication and roles are implemented using
[spring-security](http://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle).

A management API is [also accessible](http://127.0.0.1:8080/manage/) only from local host
including over [ipv6](http://[::1]:8080/manage).

Todo
----

Development is cumbersome right now, because we can not use the webpack dev server. 
A Docker compose should be set up to run the frontend and backend independently and 
connect them with a reverse proxy for increased developer productivity.
 
