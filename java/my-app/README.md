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

# User Story
Company X has a database of several customers. Company Y, a larger company, also has a database of customers. Company X shares some of the same customers as Company Y. Create a Java App using the Model View Controller Architecture that allows Company X to achieve the following goals:
 - Query Company Y's REST api to update customers missing details.
 - Perform CRUD operations on Company X's database including.
    - Creating the initial database.
    - Reading and displaying the list of customers to the user.
    - Updating Company X's database with missing entries upon receipt of Company Y's REST results.
    - Delete the database each time to ensure consistency.
 - Create a distributable JAR.
 - Write unit tests which verify functionality and get as close to 100% coverage as possible.

# TODO
1. Send a REST request to the Python server from this app to update missing phone numbers.
1. Create a unit test and automate it through github.
1. Create section which describes development environment.