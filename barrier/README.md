

# Distributed Barrier Example
In distributed computing, a barrier is a synchronization mechanism that ensures that all participating processes or threads reach a certain point in the execution before any of them can proceed further.

A distributed barrier is a type of barrier that is used in distributed systems, where processes or nodes are distributed across multiple physical machines. In a distributed barrier, each process waits for all other processes to reach the same barrier before continuing execution.

Example of distributed barrier in zookeeper with apache curator. In this example, each instance (of the 5 instances) is waiting to reach the same barrier before continuing the execution.

# Setup
- Build & run the docker container:
  ```docker-compose up --build```
- To check ZK znodes see visit http://localhost:9000/ ; connection string: `zoo1:2181,zoo2:2181,zoo3:2181`

