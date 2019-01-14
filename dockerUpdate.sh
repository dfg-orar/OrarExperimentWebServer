#!/bin/bash
mvn package ;
cp target/OrarExperiment-0.0.1-SNAPSHOT-jar-with-dependencies.jar ~/dockersandbox/docker-orar-server/app/Orar_server.jar ;
cd ~/dockersandbox/docker-orar-server ;
docker build -t orar-server-2019 . ;
docker tag orar-server-2019 orarhub/server:demo ;
docker push orarhub/server:demo ;
