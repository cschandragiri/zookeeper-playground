

# Distributed Barrier Example
In distributed systems, leader election is a process where a group of nodes elect a leader from among themselves to perform a specific task or coordinate the activities of the group. The leader is responsible for making decisions, maintaining consistency, and ensuring the integrity of the system..

A distributed barrier is a type of barrier that is used in distributed systems, where processes or nodes are distributed across multiple physical machines. In a distributed barrier, each process waits for all other processes to reach the same barrier before continuing execution.

Example of distributed barrier in zookeeper with apache curator. In this example, each instance (of the 5 instances) is waiting to reach the same barrier before continuing the execution.

# Setup
- Build & run the docker container:
  ```docker-compose up --build```
- To check ZK znodes see visit http://localhost:9000/ ; connection string: `zoo1:2181,zoo2:2181,zoo3:2181`

