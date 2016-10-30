#!/bin/bash

set -e 

echo "Adding new"
curl -X POST -H "Content-Type:application/json" -d '{ "distanceMeters": 3000, "timeSeconds": 600 }' http://localhost:8080/jogEntries
echo ""
echo "All entries are:"
curl -X GET -H "Content-Type:application/json" http://localhost:8080/jogEntries
echo ""
curl http://localhost:8080/jogEntries/search/findByDateBetween?from=22-08-2012&to=22-08-2017
echo ""
