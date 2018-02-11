package se.typedef.grpc.server.auth;

import java.util.Set;

public interface AccessControl {

  Set<AccessRight> accessForUser(String user);
}
