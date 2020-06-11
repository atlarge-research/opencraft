# Opencraft

This is the repository of Opencraft.
Opencraft is a fork of [Glowstone](https://atlarge.ewi.tudelft.nl/gitlab/opencraft/minecraft-like-games/collector-glowstone).

![](https://atlarge.ewi.tudelft.nl/gitlab/opencraft/opencraft/badges/development/pipeline.svg)

## Configuration and setup for the Opencraft messaging system

Opencraft allows you to configure the messaging system. The messaging system in Opencraft determines the way the communication occurs between the client and server. For instance, it determines to which clients each message needs to be sent and it determines how this communication takes place. The messaging systems consists of the following three core parts: a broker, policy and filter. These three parts can all be configured.

The broker ensures that all subscribers that are interested in a specific topic receive all messages that are related to that topic. The policy decides which topics get created and who is subscribed to each topic. And the filter filters out messages to subscribers if they are not supposed to receive them. For instance, a client is not supposed to receive their own block break animation message, because it will result in visual glitches when breaking a block.

### Broker configuration and setup

The configuration options can be editted in the `opencraft.yml` file located in the `config` folder of the server. This section wil provide more information about how to configure each broker for Opencraft.

#### ActiveMQ broker

The ActiveMQ broker uses [ActiveMQ](https://activemq.apache.org/) under the hood to manage the communication. ActiveMQ runs separately from the Opencraft server and thus needs to be setup separately.

##### ActiveMQ Setup

To setup ActiveMQ follow [these instructions](https://activemq.apache.org/getting-started) for ActiveMQ version 5.15.

##### ActiveMQ Configuration

The broker has the following configuration options for ActiveMQ. These options can be editted in the `opencraft.yml` file located in the `config` folder of the server.

```yaml
opencraft:
    broker:
        type: activemq
        host: localhost # The host of ActiveMQ. Be sure to change this if ActiveMQ is not running on the same machine as the Opencraft server.
        port: 61616 # The port Opencraft should try to access ActiveMQ on.
        virtualHost: '' # Irrelevant for ActiveMQ.
        username: '' # Username required to connect, leave blank if no username/password is setup.
        password: '' # Password required to connect, leave blank if no username/password is setup.
        channel: # Irrelevant for ActiveMQ.
            type: unsafe # Irrelevant for ActiveMQ.
            parallelismThreshold: 4 # Irrelevant for ActiveMQ.
```

#### RabbitMQ broker

##### RabbitMQ Setup

To setup RabbitMQ follow [these instructions](https://www.rabbitmq.com/download.html) for RabbitMQ version 2.1. To run the RabbitMQ broker it is also required to install the `rabbitmq_jms_topic_exchange` plugin. This can be done using the following command:

```bash
rabbitmq-plugins enable rabbitmq_jms_topic_exchange
```

##### RabbitMQ Configuration

The broker has the following configuration options for RabbitMQ.

```yaml
opencraft:
    broker:
        type: rabbitmq
        host: localhost # The host of RabbitMQ. Be sure to change this if RabbitMQ is not running on the same machine as the Opencraft server.
        port: 5672 # The port Opencraft should try to access RabbitMQ on.
        virtualHost: '/' # The virtual host Opencraft should connect to. See https://www.rabbitmq.com/vhosts.html for more information.
        username: 'guest' # Username required to connect, by default RabbitMQ creates a guest login.
        password: 'guest' # Password required to connect, by default RabbitMQ creates a guest login.
        channel: # Irrelevant for RabbitMQ.
            type: unsafe # Irrelevant for RabbitMQ.
            parallelismThreshold: 4 # Irrelevant for RabbitMQ.

```

## Changing the Code

We use [git flow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow) to create and maintain a clean commit history.

If you're adding a new feature, please create a feature branch.

```
git checkout dev
git pull origin/dev
git flow feature start
```

Once you have completed your feature, you can make a merge request to merge it into `dev`. If you have not yet pushed your branch to opencraft repository, you will have to do that now. You can do that by issuing the following git command:

```bash
git push -u origin <your-local-branch-name-here>
```

Once that is completed a merge request can be created using the GitLab interface.

## Updating Opencraft with Glowstone

When compiling for the first time, you'll likely need to pull new commits from Glowstone's repository. Merge this code into the `dev` branch without fast-forwarding. E.g.,

```
git remote add upstream https://atlarge.ewi.tudelft.nl/gitlab/opencraft/minecraft-like-games/collector-glowstone.git
git fetch --all
git checkout dev
git merge --no-ff upstream/dev
```

There is a reasonable chance that this results in conflicting files—Glowstone's developers may have edited the code were inspecting or monitoring. **Merge these conflicts carefully.** Our existing modifications can be crucial for somebody else's experiments. If you don't know what to do, contact one of the other Opencraft team-members.

## Running Experiments

Presumably you are modifying the code to support your experiments. After modifying the code, you can build the project to create a JAR that includes all dependencies.

After running the experiments, you **must** complete the steps listed here for reproducibility purposes:

### Building the Code

To build the code, simply run `make`.[^1] This creates two files:

1. `YYYYMMDD-X.zip`
2. `YYYYMMDD-X.jar`

`YYYYMMDD` is a timestamp that includes the current year, month, and day.

The zip file contains Glowstone's source code and its dependencies[^2] used to compile the project. We include the dependencies because Glowstone does not offer reproducible builds. Specifically, Glowstone dependencies `Glowkit` and `network` are hosted on a third-party maven repositories which keeps only their most recent versions. Adding these JARs in the zip file allows us to compile the code, even after these versions are no longer available online.

The JAR file is the executable that you can use in your experiments.

The `X` is simply an ever-increasing counter that prevents multiple builds on the same day from overwriting each other.

## Allowing Others to Reproduce Your Experiments

1. From the commit you used to build the version of Glowstone used in your experiments, create a tag using `git tag -a YYYYMMDD-X -m "project-[your-project-name-here]"`.
2. Push the tag to Gitlab using `git push origin YYYYMMDD-X`.
3. Go to [Glowstone's Gitlab page](https://atlarge.ewi.tudelft.nl/gitlab/opencraft/glowstone/collector-glowstone) and navigate to the tag you just created.
4. Attach `YYYYMMDD-X.jar` and `YYYYMMDD-X.zip` to the tag, creating a _release_.


[^1]: If you're on Windows, you might not have `make`. In that case run `./scripts/archive.sh` directly. Read the contents of `Makefile` to see what the `make` command does exactly.
[^2]: The dependencies are found in the `.m2` directory in the zip file.
