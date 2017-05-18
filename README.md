# Eureka! Clinical I2b2 Integration Service
[Atlanta Clinical and Translational Science Institute (ACTSI)](http://www.actsi.org), [Emory University](http://www.emory.edu), Atlanta, GA

## What does it do?
It provides RESTful APIs for managing i2b2 users and accessing data.

## Version 1.0 development series
Latest release: [![Latest release](https://maven-badges.herokuapp.com/maven-central/org.eurekaclinical/eurekaclinical-i2b2-integration-service/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.eurekaclinical/eurekaclinical-i2b2-integration-service)

## Build requirements
* [Oracle Java JDK 8](http://www.oracle.com/technetwork/java/javase/overview/index.html)
* [Maven 3.2.5 or greater](https://maven.apache.org)

## Runtime requirements
* [Oracle Java JRE 8](http://www.oracle.com/technetwork/java/javase/overview/index.html)
* [Tomcat 7](https://tomcat.apache.org)
* One of the following relational databases:
  * [Oracle](https://www.oracle.com/database/index.html) 11g or greater
  * [PostgreSQL](https://www.postgresql.org) 9.1 or greater
  * [H2](http://h2database.com) 1.4.193 or greater (for testing)

## REST endpoints

### `/api/protected/users`
Manages registering a user with this service for authorization purposes.

#### Role-based authorization
Call-dependent

#### Requires successful authentication
Yes

#### User object
Properties:
* `id`: unique number identifying the user (set by the server on object creation, and required thereafter).
* `username`: required username string.
* `roles`: array of numerical ids of roles.

#### Calls
All calls use standard names, return values and status codes as specified in the [Eureka! Clinical microservice specification](https://github.com/eurekaclinical/dev-wiki/wiki/Eureka%21-Clinical-microservice-specification)

##### GET `/api/protected/users`
Returns an array of all User objects. Requires the `admin` role.

##### GET `/api/protected/users/{id}`
Returns a specified User object by the value of its id property, which is unique. Requires the `admin` role to return any user record. Otherwise, it will only return the user's own record.

##### GET `/api/protected/users/me`
Returns the User object for the currently authenticated user.

##### POST `/api/protected/users/`
Creates a new user. The User object is passed in as the body of the request. Returns the URI of the created User object. Requires the `admin` role.

##### PUT `/api/protected/users/{id}`
Updates the user object with the specified id. The User object is passed in as the body of the request. Requires the `admin` role.

##### GET or POST `/api/protected/users/auto`
Will auto-register the user with this service if there is a user template with `autoAuthorize` equal to `true`, and if the template's `criteria` property is either empty, or the user's CAS attributes satisfy the specified criteria. If auto-authorization fails, a 403 status code is returned.

### `/api/protected/roles`
Manages roles for this service. It is read-only.

#### Role-based authorization
No.

#### Requires successful authentication
Yes

#### Role object
Properties:
* `id`: unique number identifying the role.
* `name`: the role's name string.

#### Calls
All calls use standard names, return values and status codes as specified in the [Eureka! Clinical microservice specification](https://github.com/eurekaclinical/dev-wiki/wiki/Eureka%21-Clinical-microservice-specification)

##### GET `/api/protected/roles`
Returns an array of all User objects.

##### GET `/api/protected/roles/{id}`
Returns a specified Role object by the value of its id property, which is unique.

### `/api/protected/usertemplates`
Manages templates for auto-registering users and assigning authorization.

#### Role-based authorization
Requires the `admin` role.

#### Requires successful authentication
Yes

#### UserTemplate object
Properties:
* `id`: unique number identifying the template (set by the server on object creation, and required thereafter).
* `name`: required template name string.
* `roles`: array of numerical ids of the Roles to assign users.
* `groups`: array of numerical ids of the Groups to assign users.
* `autoAuthorize`: whether this template should be applied when auto-authorization is requested. There should only be one template that has a value of `true` for this property.
* `criteria`: a Freemarker expression that evaluates the user's CAS attributes to return `true` if the user should be auto-authorized and `false` if not. For example, the expression `type != "student" && organization == "Hanford University"` specifies that the user can be auto-authorized only if the person's `type` attribute does not have the value "student" and the person's `organization` attribute has the value "Hanford University".

#### Calls
All calls use standard names, return values and status codes as specified in the [Eureka! Clinical microservice specification](https://github.com/eurekaclinical/dev-wiki/wiki/Eureka%21-Clinical-microservice-specification)

##### GET `/api/protected/usertemplates`
Returns an array of all UserTemplate objects.

##### GET `/api/protected/usertemplates/{id}`
Returns a specified UserTemplate object by the value of its id property, which is unique.

##### POST `/api/protected/usertemplates/`
Creates a new template. The UserTemplate object is passed in as the body of the request. Returns the URI of the created UserTemplate object.

##### PUT `/api/protected/usertemplates/{id}`
Updates the UserTemplate object with the specified id. The UserTemplate object is passed in as the body of the request.

## Building it
The project uses the maven build tool. Typically, you build it by invoking `mvn clean install` at the command line. For simple file changes, not additions or deletions, you can usually use `mvn install`. See https://github.com/eurekaclinical/dev-wiki/wiki/Building-Eureka!-Clinical-projects for more details.

## Performing system tests
You can run this project in an embedded tomcat by executing `mvn tomcat7:run -Ptomcat` after you have built it. It will be accessible in your web browser at https://localhost:8443/eurekaclinical-i2b2-integration-service/. Your username will be `superuser`.

## Installation
### Database schema creation
A [Liquibase](http://www.liquibase.org) changelog is provided in `src/main/resources/dbmigration/` for creating the schema and objects. [Liquibase 3.3 or greater](http://www.liquibase.org/download/index.html) is required.

Perform the following steps:
1) Create a schema in your database and a user account for accessing that schema.
2) Get a JDBC driver for your database and put it the liquibase lib directory.
3) Run the following:
```
./liquibase \
      --driver=JDBC_DRIVER_CLASS_NAME \
      --classpath=/path/to/jdbcdriver.jar:/path/to/eurekaclinical-i2b2-integration-service.war \
      --changeLogFile=dbmigration/changelog-master.xml \
      --url="JDBC_CONNECTION_URL" \
      --username=DB_USER \
      --password=DB_PASS \
      update
```
4) Add the following Resource tag to Tomcat's `context.xml` file:
```
<Context>
...
    <Resource name="jdbc/EurekaClinicalI2b2IntegrationService" auth="Container"
            type="javax.sql.DataSource"
            driverClassName="JDBC_DRIVER_CLASS_NAME"
            factory="org.apache.tomcat.jdbc.pool.DataSourceFactory"
            url="JDBC_CONNECTION_URL"
            username="DB_USER" password="DB_PASS"
            initialSize="3" maxActive="20" maxIdle="3" minIdle="1"
            maxWait="-1" validationQuery="SELECT 1" testOnBorrow="true"/>
...
</Context>
```

The validation query above is suitable for PostgreSQL. For Oracle and H2, use
`SELECT 1 FROM DUAL`.

### Configuration
This service is configured using a properties file located at `/etc/ec-i2b2-integration/application.properties`. It supports the following properties:
* `eurekaclinical.i2b2integrationservice.callbackserver`: https://hostname:port
* `eurekaclinical.i2b2integrationservice.url`: https://hostname:port/eurekaclinical-i2b2-integration-service
* `cas.url`: https://hostname.of.casserver:port/cas-server

A Tomcat restart is required to detect any changes to the configuration file.

### WAR installation
1) Stop Tomcat.
2) Remove any old copies of the unpacked war from Tomcat's webapps directory.
3) Copy the warfile into the tomcat webapps directory, renaming it to remove the version. For example, rename `eurekaclinical-i2b2-integration-webapp-1.0.war` to `eurekaclinical-i2b2-integration-webapp.war`.
4) Start Tomcat.

## Maven dependency
```
<dependency>
    <groupId>org.eurekaclinical</groupId>
    <artifactId>eurekaclinical-i2b2-integration-service</artifactId>
    <version>version</version>
</dependency>
```

## Developer documentation
* [Javadoc for latest development release](http://javadoc.io/doc/org.eurekaclinical/eurekaclinical-i2b2-integration-service) [![Javadocs](http://javadoc.io/badge/org.eurekaclinical/eurekaclinical-i2b2-integration-service.svg)](http://javadoc.io/doc/org.eurekaclinical/eurekaclinical-i2b2-integration-service)

## Getting help
Feel free to contact us at help@eurekaclinical.org.

