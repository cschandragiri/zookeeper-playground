### Playground for Apache Zookeeper recipes using [Apache curator](https://curator.apache.org/)

Each recipe has a containerized example where we run 3 zookeeper instances and multiple service
instances running the curator client and the recipe to execute. The temporary and ephemeral znodes for each recipe
created by zookeeper
can be visualized using zoonavigator portal running at port 9000.

#### List of curator recipes:

- [Distributed Double Barrier](barrier/)
- [Leader Election](leader-election/)
- [Distributed Reentrant Lock](reentrant-lock/)
- [Service Discovery](service-discovery/)
