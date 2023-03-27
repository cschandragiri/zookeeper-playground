# Distributed Reentrant Lock Example

A distributed re-entrant mutex lock is needed in a distributed system where multiple processes running on different
nodes need to access a shared resource in a coordinated way. This is a common scenario in many real-world applications
such as database management systems, distributed file systems, and cloud computing platforms.

#### Use Case:

Let's consider an example of a distributed system that processes incoming requests from clients and needs to ensure that
only one request at a time can access a shared database. Suppose the system consists of multiple nodes, each running a
server process that processes requests from clients. When a client sends a request to the system, it is forwarded to one
of the nodes that is responsible for handling the request.

To ensure that only one request at a time can access the shared database, the system can use a distributed re-entrant
mutex lock to coordinate access to the database. Each server process can hold a local re-entrant mutex to protect access
to the database. When a request comes in, the server process acquires the local mutex to ensure that no other request
can access the database at the same time.

However, since the system is distributed, it is possible that the same client sends multiple requests to different
nodes. To ensure that the same client can access the database multiple times in a re-entrant manner, the system needs to
use a distributed re-entrant mutex lock. This allows a client to acquire the lock multiple times, even if its requests
are processed by different nodes, as long as it releases the lock the same number of times it acquires it.

# Setup

- Build & run the docker container:
  ```docker-compose up --build```
- To check ZK znodes see visit http://localhost:9000/ ; connection string: `zoo1:2181,zoo2:2181,zoo3:2181`
- To clean up old containers: ``docker ps -aq | xargs docker stop | xargs docker rm``
