package org.springframework.security.core;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import cn.aijiamuyingfang.server.it.domain.UserAuthority;

@JsonDeserialize(as = UserAuthority.class)
public interface GrantedAuthority extends Serializable {

  String getAuthority();
}
