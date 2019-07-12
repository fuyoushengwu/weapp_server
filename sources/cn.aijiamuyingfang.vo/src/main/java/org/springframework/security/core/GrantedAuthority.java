package org.springframework.security.core;

import java.io.Serializable;

//@JsonDeserialize(as = UserAuthority.class)
public interface GrantedAuthority extends Serializable {
  String getAuthority();
}
