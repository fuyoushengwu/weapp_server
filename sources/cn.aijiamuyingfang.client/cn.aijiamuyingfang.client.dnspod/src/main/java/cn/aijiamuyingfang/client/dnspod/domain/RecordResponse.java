package cn.aijiamuyingfang.client.dnspod.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * [描述]:
 * <p>
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-06-10 06:43:22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class RecordResponse extends BaseResponse {

  private Record record;

  @Data
  @NoArgsConstructor
  public static class Record {
    private String id;

    private String name;

    private String value;
  }
}
