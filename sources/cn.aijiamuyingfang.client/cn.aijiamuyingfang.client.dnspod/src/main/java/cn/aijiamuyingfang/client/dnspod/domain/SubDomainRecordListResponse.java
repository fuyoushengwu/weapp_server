package cn.aijiamuyingfang.client.dnspod.domain;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * [描述]:
 * <p>
 * 請求列出子域名的A记录的響應
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-06-09 07:34:18
 */
@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class SubDomainRecordListResponse extends DomainResponse {
  private List<Record> records;

  @Data
  @NoArgsConstructor
  public static class Record {
    private String id;

    private String area;

    private String value;

    @SerializedName("record_type")
    private String recordType;

    @SerializedName("sub_domain")
    private String subDomain;
  }
}
