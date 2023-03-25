

# Distributed Barrier Example
In distributed computing, a barrier is a synchronization mechanism that ensures that all participating processes or threads reach a certain point in the execution before any of them can proceed further.

A distributed barrier is a synchronization primitive that allows a set of processes or threads to synchronize at a certain point in their execution before proceeding to the next stage. The idea is that each process or thread waits at the barrier until all other processes or threads have arrived, and then they all continue executing. This can be useful, for example, to ensure that a certain stage of a distributed computation is complete before proceeding to the next stage.

A distributed double barrier is a variation of the distributed barrier that provides more control over the synchronization process. With a double barrier, the processes or threads synchronize at two points in their execution: once before executing a certain stage of the computation, and again after the stage is complete. This ensures that all processes or threads have reached a consistent state before the computation begins, and that all processes or threads have finished the computation before moving on to the next stage.
Example of distributed barrier in zookeeper with apache curator. In this example, each instance (of the 5 instances) is waiting to reach the same barrier before continuing the execution.

# Setup
- Build & run the docker container:
  ```docker-compose up --build```
- To check ZK znodes see visit http://localhost:9000/ , connection string: `zoo1:2181,zoo2:2181,zoo3:2181`

