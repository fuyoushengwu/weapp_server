package cn.aijiamuyingfang.server.dnspod.api;

import java.util.Map;

import cn.aijiamuyingfang.client.rest.annotation.HttpApi;
import cn.aijiamuyingfang.server.dnspod.domain.DomainResponse;
import cn.aijiamuyingfang.server.dnspod.domain.RecordListResponse;
import cn.aijiamuyingfang.server.dnspod.domain.RecordResponse;
import cn.aijiamuyingfang.server.dnspod.domain.SubDomainListResponse;
import cn.aijiamuyingfang.server.dnspod.domain.SubDomainRecordListResponse;
import cn.aijiamuyingfang.server.dnspod.interceptor.HeaderInterceptor;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

/**
 * [描述]:
 * <p>
 * dns pod操作接口
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-06-09 04:31:21
 */
@HttpApi(baseurl = "https://dnsapi.cn/", interceptor = { HeaderInterceptor.class })
public interface DNSPodApi {

  /**
   * 获取域名信息
   * 
   * @param request
   * @return
   */
  @POST("/Domain.Info")
  @Multipart
  public Call<DomainResponse> getDomainInfo(@PartMap Map<String, RequestBody> request);

  /**
   * 记录列表
   * 
   * @param request
   * @return
   */
  @POST("/Record.List")
  @Multipart
  public Call<RecordListResponse> getRecordList(@PartMap Map<String, RequestBody> request);

  /**
   * 列出包含A记录的子域名
   * 
   * @param request
   * @return
   */
  @POST("/Monitor.Listsubdomain")
  @Multipart
  public Call<SubDomainListResponse> getSubDomainList(@PartMap Map<String, RequestBody> request);

  /**
   * 列出子域名的A记录
   * 
   * @param request
   * @return
   */
  @POST("/Monitor.Listsubvalue")
  @Multipart
  public Call<SubDomainRecordListResponse> getSubDomainRecod(@PartMap Map<String, RequestBody> request);

  /**
   * 更新动态DNS记录
   * 
   * @param request
   * @return
   */
  @POST("/Record.Ddns")
  @Multipart
  public Call<RecordResponse> updateDynamicDNS(@PartMap Map<String, RequestBody> request);

  /**
   * 添加记录
   * 
   * @param request
   * @return
   */
  @POST("/Record.Create")
  @Multipart
  public Call<RecordResponse> createRecord(@PartMap Map<String, RequestBody> request);
}
