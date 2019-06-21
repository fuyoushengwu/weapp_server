package cn.aijiamuyingfang.client.dnspod.domain;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * [描述]:
 * <p>
 * 请求解析记录的响应
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-06-09 07:23:05
 */
@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class RecordListResponse extends DomainResponse {
  private Info info;

  private List<Record> records;

  @Data
  @NoArgsConstructor
  public static class Info {
    /**
     * 指定域名下所有记录的总数
     */
    @SerializedName("sub_domains")
    private int subDomains;

    /**
     * 指定域名下符合查询条件的记录总数
     */
    @SerializedName("record_total")
    private int recordTotal;

    /**
     * 返回的 records 列表里的记录数目
     */
    @SerializedName("records_num")
    private int recordsNum;
  }

  @Getter
  @Setter
  static class Record {
    private String id;

    private int ttl;

    private String value;

    private int enabled;

    private String status;

    @SerializedName("updated_on")
    private String updatedOn;

    private String name;

    private String line;

    @SerializedName("line_id")
    private String lineId;

    private String type;

    @SerializedName("monitor_status")
    private String monitorStatus;

    private String remark;

    @SerializedName("use_aqb")
    private String useAqb;

    private int mx;

    private String hold;
  }
}
