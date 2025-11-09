# my-app

Simple app to demonstrate knowledge of the Java programming language.
I followed the following guides:


- Get a basic [Maven](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html) project up and running.
- Setup a SQLite database following [these instructions](https://github.com/xerial/sqlite-jdbc). The Download section was essential for Maven.
- Learned about `Record`, `var`, and `multi-line strings` [here](https://dev.to/adamthedeveloper/java-isnt-verbose-you-are-1di3).

# Setup and Execution
1. Install the latest JDK.
1. Install Maven.
1. To build run `mvn clean package`
1. To execute run `mvn exec:java -Dexec.mainClass="com.mycompany.app.App"`
1. To distribute run `mvn clean compile assembly:single`. This will create a single JAR that can be ran using `java -jar my-app-1.0-SNAPSHOT-jar-with-dependencies.jar`