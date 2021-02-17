## Week 7 Notes

### Kafka

Kafka is a distributed system, so it runs on a cluster of multiple machines.  Instead of being a distributed filesystem (HDFS), a distrbuted resource negotiator that allows us to schedule jobs (YARN), a distributed database (postgres, sometimes!), Kafka is a distributed event streaming platform.  This means that Kafka handles the input and output of streams of events occuring across your organization.  Kafka is meant to be used similar to a hub, sitting in the middle of many applications/projects in an organization and communicating with all of them.  Kafka is used at the NYT, it stores all their history of articles, and is used in the process for approving/editing/publishing new articles.  Many teams across NYT use Kafka for different purposes.  Those teams can retrieve articles as they are produced (streaming), and they can send their own streams of data back into Kafka.

#### What is an Event?

An "event" in Kafka is just a key,value pair that exists in an ordered stream.  There are multiple ordered streams in Kafka called "topics".  So an event is some bit of data, as a key,value pair, in order, in a topic.  So why are they called "events" and not "messages" or "pairs"?  It's a bit philosophical.  We call them events because they are meant to represent events occuring somewhere in your organization, instead of being messages used to pass information or directions to specific targets.

One way to think about this "events" vs "messages" is that we want each part of our organization to only have to concern itself with the events that are happening to it + putting those events into the appropriate Kafka channels.  It shouldn't have to worry about who/how those events will be received and acted upon.  This is because in a large org, worrying about all the teams that are going to use some bits of information you produce becomes intractable.  It's the responsibility of kafka producers to report the events occuring, not to make sure those reports arrive at specific destinations.

#### Some Context for Kafka: Publisher/Subscriber (Pub Sub)

Pub Sub is a very common design pattern, especially in distributed systems.  It lets us decouple the applications/processes that produce data from the applications/processes that consume that data.  In kafka we decouple the event producers from event consumers.  In other systems, we decouple message producers from message consumers.  Publishers publish to some intermediate entity (channel is common), subscribers read from that channel.  The subscriber doesn't need to know anything about the publishers that ultimately produced the information it receives, and the publisher doesn't need to know anything about who is receiving their output.

It's a common pattern in software development to write applications as a network of distributed services that communicate, instead of writing a large application that handles all tasks itself.  There are many advantages to this approach, but one major drawback is that your services need to communicate over a network, hopefully efficiently.  For direct traffic from one service to another, HTTP is good.  HTTP is not pub/sub, instead HTTP is a client-server model of communication, where a client sends a request to a server and the server sends a response back to the client.  This 1-on-1 communication is good for many purposes, but is very inefficient to distribute information that must be widely shared.  Instead of sending out 1 request, our clients would need to send out 50 or 100 or 1000 requests.  Pub/sub is much better for this.  The producer sends information to a channel, all the interested parties subscribe to that channel and receive the information.

Another pattern to contrast with Kafka is a Messaging Queue.  Messaging queues typically have multiple topics/channels that contain messages, and messaging queues decouple the producers of messages from their consumers.  The difference between a messaging queue and a pub/sub setup is the expected behaviour of consumers.  In pub/sub there aren't expectations and many consumers might (and often will) receive the same messages.  All the subscribers to a given topic/channel will receive the messages/events posted to that topic/channel.  Messages passed in a messaging queue are expected to be resolved, in some way, by the consumer.  Typically a message going through a messaging queue will only go to one subscriber, and the expectation is that that subscriber resolves the message, then requests that the message be deleted from the queue.  Contrast with MQs is another reason we work with "events" in Kafka.  MQs are *very* often used in data storage systems that achieve BASE, because they allow one part of the system to make a change to the underlying data + place a message in the queue to make that data consistent in the future.

#### Pub/Sub and Kafka

Kafka provides pub/sub functionality.  Kafka *producers* send *events* to *topics*, thsoe events are read from topics by *consumers*.  Each topic has a *log* that contains all events associated with the topic.  This log by default contains events going back a week, but can and sometimes does contain all the events since the beginning of the topic.

The machines in a kafka cluster are *brokers* and there isn't a single master.  Zookeeper is necessary to manage elections, but otherwise some brokers act as masters for partitions of topics, and they become masters via broker elections.  Out network of brokers is responsible for receiving + making available events, and maintaining resilience/HA of our pub/sub architecture.  The network of brokers has replications of each topic, and has procedures to recover from failure.

A distinction we haven't mentioned yet in pub/sub is push-based vs pull-based subscriptions.  A push-based system has the topic/channel push events/messages to each subscriber when they occur.  A pull-based system requires that the subscriber/consumer retrieve messages from their topics.  Kafka is pull-based.  This works well with the log associated with each topic, since kafka consumers aren't in real danger of missing events because they pull infrequently.  A major advantage of pull-based pub/sub is that we don't need to worry about overwhelming consumers/subscribers, since the consumer/subscriber is free to complete processing before requesting new data.

One more distinction to discuss for pub/sub: delivery guarantees.  Kafka and other pub/sub systems need to decide how to handle the potential for failing consumers.  This is a configurable in Kafka, so different delivery guarantees are possible.  We can have at-most-once delivery, where we're guaranteed that consumers will receive an event 0 or 1 times.  We can have at-least-once delivery, where we're guaranteed that consumers will receive an event 1 or more times.  We can also have exactly-once delivery, where we're guaranteed that consumers will receive an event exactly 1 time (or effectively-once delivery, where they receive it more than once but it is only handled once).  This is much harder to make happen than the first 2.

At most once delivery is achieved by having the broker provide an event to each consumer exactly once, if the consumer fails while retrieving the event, then that consumer doesn't get the event.  In this way we have 1 delivery if successful, 0 deliveries if failure.

At least once delivery is achieved by having the broker provide an event to each consumer until it gets some acknowledgement of success.  If the consumer continues to fail to acknowledge receiving the event, then the consumer will continue to recieve that event.

Kafka provides the tools to achieve effectively-once delivery by providing unique identifiers for messages and making message retrieval idempotent.  Getting effectively-once delivery requires cooperation from the consumer.  The consumer retrieves events under the at-least-once delivery scheme, then checks those events' identifiers vs. the identifiers it already has.  In this way it avoids processing any given event twice.

Transactions are also possible which provide exactly-once delivery, but most often we're happy with effectively-once delivery.

Each consumer of a kafka topic has an *offset* in that topic, the position in time/the log that it is currently reading from.  In at-most-once delivery, the broker keeps track of the offset and increments it as it sends out new events.  In at-least-once delivery, the consumer keeps track of the offset and updates the offset only after processing is successful.  (to be continued...)




