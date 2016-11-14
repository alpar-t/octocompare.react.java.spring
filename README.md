About
=====

This is a project meant for learning and experimenting with technologies for implementing a 
(single page) modern web app.

This implementation uses:
    - Spring
        - [Boot](https://spring.io/guides/gs/spring-boot/) 
        - [Security](http://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle)
        - [Data](http://projects.spring.io/spring-data/) 
        - [Data Rest](http://docs.spring.io/spring-data/rest/docs/current/reference/html/)
    - [React.js](https://facebook.github.io/react/)
    - Redux in the back-end to track sport activities
    - [material-ui](http://www.material-ui.com/)
    - [webpack](https://webpack.github.io/)

The goal is to implement the same with other technologies / frameworks and compare results.

Users of the activity tracker can:
 - Create an account and log in
 - When logged in, see, edit and delete entries
 - Users can only access their own entries
 - entries can be made even if the back-end is off-line
 - A management API is [also accessible](http://127.0.0.1:8080/manage/) only from local host
including over [ipv6](http://[::1]:8080/manage).
 - user management API ( CRUD users )
 - content management API ( CRUD entries for all users )

How To run
==========

JDK Version 8 is required. Everything else is set up automatically.
Start the app with:
    
    ./gradlew bootRun

Navigate to (http://127.0.0.1:8080/).
Authenticate with user name 'admin' and password 'password' when asked. 

There's an [API browser](http://127.0.0.1:8080/browser/index.html#/) as well.

Notes
=====

The only pre-requisite to build and run is JDK version 8 and an internet connection.
This includes the front-end.

To run all tests on both ends:

    ./gradlew check

Or to run the fast unit tests only: 

    ./gradlew test

Set up git hooks to automate running tests on commit and check on push:
    
    ./git/create_hooks.sh

