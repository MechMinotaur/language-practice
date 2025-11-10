# my-app

Simple app to demonstrate knowledge of the Java programming language.
I followed the following guides:


- Get a basic [Maven](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html) project up and running.
- Setup a SQLite database following [these instructions](https://github.com/xerial/sqlite-jdbc). The Download section was essential for Maven.
- Learned about `record`, `var`, and `multi-line strings` [here](https://dev.to/adamthedeveloper/java-isnt-verbose-you-are-1di3).
- [This](https://youtu.be/9oq7Y8n1t00?si=BlucUhVeFVmZHwuX) video detailing how to call a REST API in Java.

# Setup and Execution
1. Install the latest JDK.
1. Install Maven.
1. To perform initial build run `mvn clean package`
1. To execute run `mvn exec:java -Dexec.mainClass="com.mycompany.app.App"`
1. To distribute run `mvn clean compile assembly:single`. This will create a single JAR that can be ran using `java -jar my-app-1.0-SNAPSHOT-jar-with-dependencies.jar`

# TODO
1. Describe what the app does. Consider a made up user story.
1. Create a simple Python server that responds to REST requests.
1. Send a REST request to the Python server from this app to update missing phone numbers.
1. Create a unit test and automate it through github.
1. Create section which describes development environment.