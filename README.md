# Opencraft

This is the repository of Opencraft.
Opencraft is a fork of [Glowstone](https://atlarge.ewi.tudelft.nl/gitlab/opencraft/minecraft-like-games/collector-glowstone).

![](https://atlarge.ewi.tudelft.nl/gitlab/opencraft/opencraft/badges/development/pipeline.svg)

## Updating Opencraft

When compiling for the first time, you'll likely need to pull new commits from Glowstone's repository. Merge this code into the `dev` branch without fast-forwarding. E.g.,

```
git remote add upstream https://atlarge.ewi.tudelft.nl/gitlab/opencraft/minecraft-like-games/collector-glowstone.git
git fetch --all
git checkout dev
git merge --no-ff upstream/dev
```

There is a reasonable chance that this results in conflicting files—Glowstone's developers may have edited the code were inspecting or monitoring. **Merge these conflicts carefully.** Our existing modifications can be crucial for somebody else's experiments. If you don't know what to do, contact one of the other Opencraft team-members.

## Changing the Code

We use [git flow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow) to create and maintain a clean commit history.

If you're adding a new feature, please create a feature branch.

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

1. From the commit you used to build the version of Glowstone used in your experiments, create a tag using `git tag -a YYYYMMDD-X -m "project-[your-project-name-here]"`.
2. Push the tag to Gitlab using `git push origin YYYYMMDD-X`.
2. Go to [Glowstone's Gitlab page](https://atlarge.ewi.tudelft.nl/gitlab/opencraft/glowstone/collector-glowstone) and navigate to the tag you just created.
3. Attach `YYYYMMDD-X.jar` and `YYYYMMDD-X.zip` to the tag, creating a _release_.


[^1]: If you're own Windows, you might not have `make`. In that case run `./scripts/archive.sh` directly. Read the contents of `Makefile` to see what the `make` command does exactly.
[^2]: The dependencies are found in the `.m2` directory in the zip file.
