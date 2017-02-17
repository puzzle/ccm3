CCM3 Viewer
===========
[![Build Status](https://travis-ci.org/puzzle/ccm3.svg?branch=master)](https://travis-ci.org/puzzle/ccm3)

Simple Java EE based webapplication which exposes data of a given
database as REST API. The REST API will be accessed by a HTML5 / jQuery
based client.

## Prerequisites
* JBosss EAP 7 or Wildfly 10 with configured datasource
* JDK 8
* Maven 3

## Techstack
* Java EE 7
* JAX-RS, JPA 2.1, Bean Validation 1.1
* Java 8
* HTML 5, jQuery 3.1, Bootstrap 3, datatables.net 2.1
* MySQL Database

## Architecture
* Java Packages are structured according to the Entity-Control-Boundary Pattern
* Userinterface is decoupled by a REST interface. The data is serialized as JSON.
* Entities will be serialized directly as JSON
* Transaction demarcation is made directly on the boundaries
* Only stateless EJBs are used
* Filtering, pagination and sorting is done on the server-side
* Packaging as a single WAR-file including Backend, Client and Swagger

## How to build the application
To build the application Apache Maven 3 and Java 8 is required and
properly configured.
```
$ mvn clean install
```

## Application Server configuration

### Security Domain
The application expects a security domain with the name `ccm3` configured in JBoss application server.

JBoss Security-Domain configuration snippet:
```xml
<security-domain name="ccm3">
    <authentication>
        <login-module code="org.jboss.security.auth.spi.UsersRolesLoginModule" flag="required">
            <module-option name="usersProperties" value="${jboss.server.config.dir}/application-users.properties"/>
            <module-option name="rolesProperties" value="${jboss.server.config.dir}/application-roles.properties"/>
        </login-module>
    </authentication>
</security-domain>
```

Add a local application user and role:
```
$ echo "username=password" >> application-users.properties
$ echo "username=ccm3-user" >> application-roles.properties
```

### Datasource
To start the application a datasource needs to be configured in the
application server. This can either be a in memory or a persistent
database.
    
JDBC Driver
Deploy the JDBC Driver to the application Server as a managed deployment:
```
${JBOSS_HOME}/bin/jboss-cli.sh -c --command="deploy mysql-connector-java-5.1.40-bin.jar"
```

JBoss Datasource configuration snippet:
```xml
<datasource jta="true" jndi-name="java:jboss/datasources/ccm3" pool-name="ccm3" enabled="true" use-ccm="true" statistics-enabled="false">
    <connection-url>jdbc:mysql://localhost:3306/ccm3</connection-url>
    <driver-class>com.mysql.jdbc.Driver</driver-class>
    <driver>mysql-connector-java-5.1.40-bin.jar_com.mysql.jdbc.Driver_5_1</driver>
    <security>
        <user-name>REPLACE-BY-USER</user-name>
        <password>REPLACE-BY-PASS</password>
    </security>
</datasource>
```

#### Database Schema
A SQL script to create the initial database schema is provided under
```
${project-root}/src/main/sql/schema.sql
```

### Swagger
Swagger can be configured by system properties:

Configure basepath (defaults to 'ccm3-viewer/api/v1'):
```
-Dswagger.api.basepath=/application/api-url/v42
```

Configure host (defaults to 'localhost:8080'):
```
-Dswagger.api.basepath=8.8.8.8:80
```

## Deployment of the application
The deployment of the application into a local running JBoss EAP 7 /
Wildlfy 10 instance can be done by Maven:
```
$ mvn wildfly:deploy
```

Redeploy the application:
```
$ mvn wildfly:redeploy
```

Undeploy the application:
```
$ mvn wildfly:undeploy
```

## REST API
Root of the REST Endpoint
```
http://localhost:8080/ccm3-viewer/api/v1
```

Swagger UI, Documentation of the Endpoints including a REST Client
```
http://localhost:8080/ccm3-viewer/swagger
```
