package se.typedef.grpc.server.auth;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.*;

import static se.typedef.grpc.server.auth.AccessRight.*;

public class DummyAccessControl implements AccessControl {

  private static final Set<AccessRight> NO_RIGHTS = ImmutableSet.of();
  private static final Map<String, Set<AccessRight>> ACCESS_RIGHTS_BY_USER =
    ImmutableMap.of(
      "admin",
      ImmutableSet.of(READ, WRITE, DELETE),
      "editor",
      ImmutableSet.of(READ, WRITE),
      "reader",
      ImmutableSet.of(READ));

  private DummyAccessControl() {
    // no-op
  }

  public static DummyAccessControl create() {
    return new DummyAccessControl();
  }

  @Override
  public Set<AccessRight> accessForUser(final String user) {
    return ACCESS_RIGHTS_BY_USER.getOrDefault(user, NO_RIGHTS);
  }
}
