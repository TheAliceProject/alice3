# Alice 3

[Alice](https://www.alice.org) is an innovative block-based programming environment that makes it easy to create animations, build interactive narratives, or program simple games in 3D.

## Building Alice 3 from the source

Download and install the following build tools
* J2SE 1.8 JDK
  * Set JAVA_HOME accordingly, and add $JAVA_HOME/bin to your PATH
* Maven
* git
* Install4J 7
  * Only required to build the installers

---

Clone the Alice 3 repository into a local directory, (`${alice3}`)

    cd ${alice3}
    git clone https://github.com/TheAliceProject/alice3.git

Compile Alice to run locally:

    cd ${alice3}
    mvn compile

Launch Alice

    cd alice/alice-ide
    mvn exec:java -Dentry-point

Run unit tests

    cd ${alice3}
    mvn test

Build Alice jars, NetBeans plugin, and installers (the last rely on Install4J):

    cd ${alice3}
    mvn install

The Alice installers are built using Install4J, which requires a license.
You may skip this build step.

    cd ${alice3}
    mvn -Dinstall4j.skip install

## Working without the Sims

The compile and install phases can all be limited to not include the Sims assets.
To do that disable the `includeSims` maven profile. It is a good idea to apply `clean`
if you have previously made a full build to prevent leftover Sims artifacts getting bundled in.

    cd ${alice3}
    mvn -DincludeSims=false clean compile
Or:

    cd ${alice3}
    mvn -DincludeSims=false clean install


## How to contribute

We appreciate contributions from the Alice community.

To make it easier to merge in new work, when submitting PRs please:
* Keep each one small and focused
* Make individual commits of smaller chunks with clear descriptions
* Follow the established coding style
