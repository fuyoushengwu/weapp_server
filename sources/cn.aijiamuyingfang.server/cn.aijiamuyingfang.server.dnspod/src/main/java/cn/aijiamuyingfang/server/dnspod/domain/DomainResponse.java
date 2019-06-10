package cn.aijiamuyingfang.server.dnspod.domain;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * [描述]:
 * <p>
 * 包含Domain信息的响应
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-06-09 07:11:16
 */
@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class DomainResponse extends BaseResponse {
  private Domain domain;

  @Data
  @NoArgsConstructor
  public static class Domain {
    private String id;

    private String name;

    private String punycode;

    private String grade;

    @SerializedName("grade_title")
    private String gradeTitle;

    private String status;

    @SerializedName("ext_status")
    private String extStatus;

    private String records;

    @SerializedName("group_id")
    private String groupId;

    @SerializedName("is_mark")
    private String isMark;

    private String remark;

    @SerializedName("is_vip")
    private String isVip;

    @SerializedName("searchengine_push")
    private String searchenginePush;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("created_on")
    private String createdOn;

    @SerializedName("updated_on")
    private String updatedOn;

    private int ttl;

    @SerializedName("min_ttl")
    private int minTTL;

    @SerializedName("cname_speedup")
    private String cnameSpeedup;

    private String owner;

    @SerializedName("dnspod_ns")
    private List<String> dnspodNS;
  }
}
