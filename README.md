# Opencraft

This is the repository of the Opencraft server.
Opencraft is a fork of [Glowstone](https://atlarge.ewi.tudelft.nl/gitlab/opencraft/minecraft-like-games/collector-glowstone).

![](https://atlarge.ewi.tudelft.nl/gitlab/opencraft/opencraft/badges/development/pipeline.svg)

# Configuration and setup for the Opencraft messaging system

Opencraft allows you to configure the messaging system. The messaging system in Opencraft determines the way the communication occurs between the client and server. For instance, it determines to which clients each message needs to be sent and it determines how this communication takes place. The messaging systems consists of the following three core parts: a broker, policy and filter. The first two parts can be configured.

The broker ensures that all subscribers that are interested in a specific topic receive all messages that are related to that topic. The policy decides which topics get created and who is subscribed to each topic. And the filter filters out messages to subscribers if they are not supposed to receive them. For instance, a client is not supposed to receive their own block break animation message, because it will result in visual glitches when breaking a block.

Besides the Minecraft specific configuration options, there are also configuration options for the Opencraft server internals. These configuration options can be edited in the `opencraft.yml` file located in the `config` folder of the server. The following options are available for configuring the Opencraft server:

```yaml
opencraft:
    collector: false # Determines whether to enable the yardstick collector. See https://atlarge.ewi.tudelft.nl/gitlab/opencraft/yardstick for more information.
    policy: chunk
    broker:
        type: readwrite
        host: localhost
        port: 0
        virtualHost: ''
        username: ''
        password: ''
        channel:
            type: unsafe
            parallelismThreshold: 4
```

**IMPORTANT: All of these options should be present in the config file, even if they are irrelevant for your server configuration!**

The following sections will provide more information about all the configuration options.

## Broker configuration and setup

All the brokers for Opencraft fall into one of two categories. Java Message System (JMS) brokers and Channel brokers. The main difference between JMS brokers and Channel brokers is that the JMS brokers are run in a separate process, while the Channel brokers are run internally in the Opencraft server.

### JMS brokers

As mentioned previously, the JMS brokers use and implement the [JMS API](https://www.oracle.com/technical-resources/articles/java/intro-java-message-service.html). The advantage of JMS is that only the underlying backend has to change and not the entire broker, whenever you want to use a different backend.

The JMS brokers are run in a separate process. This even allows the brokers to be run on a different machine than the server.

The advantage of this approach is that the Opencraft server and the brokers can scale independently. For instance, both backends currently available for the JMS broker can scale over multiple machines. 

The main disadvantage of this approach is that there are extra serialization and deserialization steps required compared to the Channel brokers. In the future it might be possible to skip this extra (de)serialization, if the messages can be sent directly from the broker to the client, without the server being in between the client and broker.

#### ActiveMQ broker

The ActiveMQ broker uses [ActiveMQ](https://activemq.apache.org/) under the hood to manage the communication. ActiveMQ runs separately from the Opencraft server and thus needs to be setup separately.

To setup ActiveMQ follow [these instructions](https://activemq.apache.org/getting-started) for ActiveMQ version 5.15. Once you have setup ActiveMQ you can start configuring it.

The ActiveMQ broker has the following **relevant** configuration options. These options can be edited in the `opencraft.yml` file located in the `config` folder of the server.

```yaml
opencraft:
    broker:
        type: activemq # Select this type if you want to use the ActiveMQ broker.
        host: localhost # The host of ActiveMQ. Be sure to change this if ActiveMQ is not running on the same machine as the Opencraft server.
        port: 61616 # The port Opencraft should try to access ActiveMQ on.
        username: '' # Username required to connect, leave blank if no username/password is setup.
        password: '' # Password required to connect, leave blank if no username/password is setup.
```

#### RabbitMQ broker

To setup RabbitMQ follow [these instructions](https://www.rabbitmq.com/download.html) for RabbitMQ version 2.1. To run the RabbitMQ broker it is also required to install the `rabbitmq_jms_topic_exchange` plugin. This can be done using the following command:

```bash
rabbitmq-plugins enable rabbitmq_jms_topic_exchange
```

Once you have setup RabbitMQ, you can start configuring it. The RabbitMQ broker has the following **relevant** configuration options.

```yaml
opencraft:
    broker:
        type: rabbitmq # Select this type if you want to use the RabbitMQ broker.
        host: localhost # The host of RabbitMQ. Be sure to change this if RabbitMQ is not running on the same machine as the Opencraft server.
        port: 5672 # The port Opencraft should try to access RabbitMQ on.
        virtualHost: '/' # The virtual host Opencraft should connect to. See https://www.rabbitmq.com/vhosts.html for more information.
        username: 'guest' # Username required to connect, by default RabbitMQ creates a guest login.
        password: 'guest' # Password required to connect, by default RabbitMQ creates a guest login.

```

### Channel brokers

In contrast to the JMS brokers, the Channel brokers are actually part of the Opencraft server and thus do not run in a separate process. The Channel brokers mainly differ in how the concurrency is implemented. For instance, the Concurrent broker uses a concurrent hashmap as internal data structure, while the ReadWrite broker uses a read-write lock to manage asynchronous access to the broker.

The Channel brokers can even be configured further, by selecting the type of channel the broker should use internally. See the [Channel configuration and setup](#channel-configuration-and-setup) section for more information.

#### Concurrent broker

The Concurrent broker internally uses a concurrent hashmap, which means that it rarely blocks on any incoming calls due to locking. This broker is useful whenever there is a large amount of (un)subscribing and publishing happening at the same time.

To enable the concurrent broker set the `broker type` to `concurrent`.

#### ReadWrite broker

The ReadWrite broker uses a read-write lock to manage asynchronous access to the broker. This broker is useful whenever there is not a large amount of (un)subscribing and publishing happening at the same time. Threads only require a read lock when only publishing messages. The advantage of that is that there can be multiple read locks active at the same time, therefore no blocking occurs when only publishing messages.

To enable the ReadWrite broker set the `broker type` to `readwrite`.

#### Channel configuration

There are multiple types of channels available that the Channel brokers can use. Whenever a message is published on a channel all subscribers that are subscribed to that channel will receive that message. A channel can not only have multiple subscribers, but also multiple publishers. Each channel only handles messages that are related to each other. Which messages are sent over each channel is determined by the policy. For instance, the chunk policy creates a channel for each chunk over which all entity and block update messages are sent.

There are currently four types of channels:

1. **ConcurrentChannel:** This channel will publish messages concurrently if there are more subscribers that need to receive the message than is configured. This threshold can be configured by changing the `parallelismThreshold` setting in the config.
2. **GuavaChannel:** This channel uses the [EventBus](https://github.com/google/guava/wiki/EventBusExplained) from the Guava library for managing the publishing and subscribing.
3. **ReadWriteChannel:** This channel uses a read-write lock to handle asynchronous access. The characteristics of this channel are similar to the characteristics of the similarly named ReadWriteBroker.
4. **UnsafeChannel:** This channel does not guarantee any thread-safety. In theory this should not be a problem, since all Channel brokers should not access the same channel at the same time. 

Each of these brokers can be enabled by changing the `channel type` to respectively: `concurrent`, `guava`, `readwrite` or `unsafe`.

## Policy configuration

The policy of the messaging system determines for which topics channels are created and which subscribers are subscribed to a topic. Furthermore, it also determines which messages are sent to each topic.

Currently, only the Chunk policy has been implemented. The policy interface is implemented in such a way that it is easy to create new policies in the future. The Chunk policy creates separate channels for each chunk. Each player subscribes to chunks that are within their view distance. Whenever an update occurs within a chunk, the update message will be sent to the channel of that chunk.

The policy can be configured by changing the `policy` setting. Currently, it can only be set to `chunk`.

# Changing the Code

We use [git flow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow) to create and maintain a clean commit history.

If you're adding a new feature, please create a feature branch.

```
git checkout dev
git pull origin/dev
git flow feature start
```

Once you have completed your feature, you can make a merge request to merge it into `dev`. If you have not yet pushed your branch to the Opencraft repository, you will have to do that now. You can do that by issuing the following git command:

```bash
git push -u origin <your-local-branch-name-here>
```

Once that is completed a merge request can be created using the GitLab interface.

# Updating Opencraft with Glowstone

When compiling for the first time, you'll likely need to pull new commits from Glowstone's repository. Merge this code into the `dev` branch without fast-forwarding. E.g.,

```bash
git remote add upstream https://atlarge.ewi.tudelft.nl/gitlab/opencraft/minecraft-like-games/collector-glowstone.git
git fetch --all
git checkout dev
git merge --no-ff upstream/dev
```

There is a reasonable chance that this results in conflicting files, Glowstone's developers may have edited the code were inspecting or monitoring. **Merge these conflicts carefully.** Our existing modifications can be crucial for somebody else's experiments. If you don't know what to do, contact one of the other Opencraft team-members.
