#!/bin/bash
docker-compose -f ./db/docker-compose-dev.yml down
docker rm -f -v $(docker ps -a |  grep "nutrido-alimento")
docker rmi $(docker images -a |  grep "nutrido-alimento")
