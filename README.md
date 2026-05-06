# Models Project

### Prerequisites

- Java 25 or higher
- Maven 3.6+
- SQL Server database (or configure your own database)

## Building and Publishing

To make the models available for other projects:

```bash
# Build and install to local Maven repository
mvn clean install

# For remote repository publishing, configure your pom.xml with distributionManagement
mvn clean deploy
```

## Usage in Other Projects

To use the models in another Spring Boot project (e.g., `android_test_service`):

 **Add as a Maven Dependency**:
   ```xml
   <dependency>
       <groupId>dev.themajorones</groupId>
       <artifactId>models</artifactId>
       <version>0.0.1-SNAPSHOT</version>
   </dependency>
   ```
