# Service Discovery Example

In a distributed system, service discovery refers to the process of discovering and locating services that are running across the different nodes of the system.

# Setup

- Build & run the docker container:
  ```docker-compose up --build```
- To check ZK znodes see visit http://localhost:9000/ ; connection string: `zoo1:2181,zoo2:2181,zoo3:2181`
- To clean up old containers: ``docker ps -aq | xargs docker stop | xargs docker rm``
