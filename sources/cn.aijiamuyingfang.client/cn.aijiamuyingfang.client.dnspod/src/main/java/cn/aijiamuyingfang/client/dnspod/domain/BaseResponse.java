package cn.aijiamuyingfang.client.dnspod.domain;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * [描述]:
 * <p>
 * DNS POD响应
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-06-09 07:08:07
 */
@Data
@NoArgsConstructor
public abstract class BaseResponse {
  protected Status status;

  @Getter
  @Setter
  public static class Status {
    private String code;

    private String message;
    
    @SerializedName("created_at")
    private String createdAt;
  }
}
