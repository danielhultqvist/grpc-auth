# grpc-auth
This project demonstrates how to do gRPC authentication + annotation based authorization.

If you have any suggestions for improvements, please feel free to make a PR.

## Overview
These are the modules included in the project.

### schema
Contains proto files describing a gRPC service. The schema is compiled to Java using a Maven plugin (see pom.xml)

### server
A gRPC server running the services described in `schema` module.

The interesting parts lives in the `auth` package.

#### AuthenticationInterceptor.java
This is where all magic happens. It intercepts all gRPC calls and checks if a user has been provided.
If so, it also checks whether a user has access rights to the requested endpoint.

The interceptor accepts a username in plaintext, so the security model isn't advanced. It is trivial to accept
a JWT token or similar to perform additional authentication if wanted.

When registering a service to the gRPC server, it also needs to be registered in the `AuthenticationInterceptor` instance.
This allows the interceptor to map the implementation methods to gRPC endpoint methods. As this is done on startup time, 
it does not affect performance during actual runtime.

### cli
Simple CLI tool for communicating with the gRPC server. Outputs responses in JSON.

#### Usage
Start the `server` application in your editor or `java -jar server/target/server-0-SNAPSHOT.jar` (remember to `mvn package` first)
```bash
java -jar cli/target/cli-0-SNAPSHOT.jar --user editor put --author "Peter Pan" --text "I can fly!"

java -jar cli/target/cli-0-SNAPSHOT.jar --user reader get --id 1
```

Verify authorization by doing something naughty
```bash
java -jar cli/target/cli-0-SNAPSHOT.jar --user reader put --author "Bad Boy" --text "I am not allowed to write posts :-(!"
```

# What now?
Feel free to use in any way you want!
