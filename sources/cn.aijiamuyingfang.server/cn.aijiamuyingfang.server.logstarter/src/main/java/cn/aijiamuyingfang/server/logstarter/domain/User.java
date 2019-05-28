package cn.aijiamuyingfang.server.logstarter.domain;

import lombok.Data;

@Data
public class User {

  /**
   * 用户的Id
   */
  private String id;

  public String getUsername() {
    return this.id;
  }
}
