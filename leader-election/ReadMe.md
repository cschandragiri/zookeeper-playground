

# Leader Election Example
In distributed systems, leader election is a process where a group of nodes elect a leader from among themselves to perform a specific task or coordinate the activities of the group. The leader is responsible for making decisions, maintaining consistency, and ensuring the integrity of the system. Zookeeper's leader election algorithm is based on the ZAB (Zookeeper Atomic Broadcast) consensus protocol.

In this protocol, each node in the system is assigned a monotonically increasing unique ID, which is used to order the nodes in a sequence. The node with the smallest ID is elected as the leader. To check the node assignment, run the docker container and visit http://localhost:9000/. Under the permanent znode, look
for ephemeral znodes which are assigned a monotonically increasing suxxix `-lock-{monotonically_increasing_number}`.


# Setup
- Build & run the docker container:
  ```docker-compose up --build```
- To check ZK znodes see visit http://localhost:9000/ ; connection string: `zoo1:2181,zoo2:2181,zoo3:2181`

