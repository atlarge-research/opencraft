# Opencraft's Glowstone Fork

This is Opencraft's fork of [Glowstone](https://github.com/GlowstoneMC/Glowstone). We benchmark Glowstone using Yardstick. We created a fork for two reasons:

1. We need to add a small amount of code to allow us to monitor the game through Prometheus.
2. Glowstone does not provide stable releases. Its dependencies, even for releases, are often `*-SNAPSHOT` versions. Instead of  taking over versioning for Glowstone and all its sub-projects, we add scripts to this repository to download all dependencies to a local directory which we can include in our releases. This improves experiment reproducibility.

## Changing the Code

In case you need to insert extra code in Glowstone for additional monitoring, please follow these instructions.

We use [git flow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow) to create and maintain a clean commit history.

### Updating Glowstone

When compiling for the first time, you'll likely need to pull new commits from Glowstone's repository. Merge this code into the `dev` branch without fast-forwarding. E.g.,

```
git fetch --all
git checkout dev
git merge --no-ff upstream/dev
```

There is a reasonable chance that this results in conflicting files—Glowstone's developers may have edited the code were inspecting or monitoring. **Merge these conflicts carefully.** Our existing modifications can be crucial for somebody else's experiments. If you don't know what to do, contact one of the other Opencraft team-members.

### Adding Your Own Code

If you're adding a new feature, such as additional support for monitoring tools, please create a feature branch.

```
git checkout dev
git pull origin/dev
git flow feature start
```

Add code to your liking until it does what you need it to do. Then run `git flow feature finish` to merge these changes back into the `dev` branch.

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

From the commit you used to build the version of Glowstone used in your experiments, create a tag in git:

```
git tag -a YYYYMMDD-X -m "project-[your-project-name-here]"
git push origin YYYYMMDD-X
```

[^1]: If you're own Windows, you might not have `make`. In that case run `./scripts/archive.sh` directly. Read the contents of `Makefile` to see what the `make` command does exactly. 