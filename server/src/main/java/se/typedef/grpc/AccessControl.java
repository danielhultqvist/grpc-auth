package se.typedef.grpc;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.*;

import static se.typedef.grpc.AccessControl.AccessRight.*;

public class AccessControl {
  public enum AccessRight {
    READ,
    WRITE,
    DELETE
  }

  private static final Set<AccessRight> NO_RIGHTS = ImmutableSet.of();
  private static final Map<String, Set<AccessRight>> ACCESS_RIGHTS_BY_USER =
      ImmutableMap.of(
          "admin",
          ImmutableSet.of(READ, WRITE, DELETE),
          "editor",
          ImmutableSet.of(READ, WRITE),
          "reader",
          ImmutableSet.of(READ));

  public static Set<AccessRight> accessForUser(final String user) {
    return ACCESS_RIGHTS_BY_USER.getOrDefault(user, NO_RIGHTS);
  }
}
