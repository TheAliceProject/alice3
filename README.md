# Alice 3

[Alice](https://www.alice.org) is an innovative block-based programming environment that makes it easy to create animations, build interactive narratives, or program simple games in 3D.

## Building Alice 3 from the source

Download and install the following build tools
* J2SE 1.8 JDK
  * Set $JAVA_HOME accordingly, and add $JAVA_HOME/bin to your PATH
* [Apache Maven](https://maven.apache.org/install.html)
* git
* [git-lfs](https://help.github.com/en/articles/installing-git-large-file-storage)
* [Install4J 7](https://www.ej-technologies.com/products/install4j/overview.html) (Only required to build the installers)

---

Clone the Alice 3 repository into a local directory, (`${alice3}`)

    cd ${alice3}
    git clone --recurse-submodules https://github.com/TheAliceProject/alice3.git
    
Alice 3 uses a submodule for the Tweedle language, the internal representation of Alice code.
If you do not use the `--recurse-submodules` flag above it can be pulled in explicitly.

    git submodule init
    git submodule update

Compile and jar the Alice code. This will also build the NetBeans plugin:

    mvn -Dinstall4j.skip install

## Executing, testing, and building

Launch the Alice IDE

    cd alice-ide
    mvn exec:java -Dentry-point

Run unit tests

    cd ${alice3}
    mvn test

Build the Alice installers, which  requires Install4J 7:

    cd ${alice3}
    mvn install

## Working without the Sims*

The compile, package, and install phases can all be limited to not include the Sims assets.
To do that disable the `includeSims` maven profile.

It is a good idea to `clean` if you have previously made a full build.
This may prevent leftover Sims artifacts getting bundled in.

    cd ${alice3}
    mvn -DincludeSims=false -Dinstall4j.skip clean package
Or:

    cd ${alice3}
    mvn -DincludeSims=false clean install


**This is still experimental, so there may be errors.*

## How to contribute

We appreciate contributions from the Alice community.

To make it easier to merge in new work, when submitting PRs please:
* Keep each one small and focused
* Make individual commits of smaller chunks with clear descriptions
* Follow the established coding style

### Development tools

This repository previously included a number of projects that were experiments, test beds, and development aids.

They have been relocated to the [Alice 3 Tools](https://github.com/TheAliceProject/alice3-tools) repo.
(NB If you are looking for historic versions of any of those projects, the richer git history is in this repo. The history did not migrate due to the way filter-branch was applied.)
